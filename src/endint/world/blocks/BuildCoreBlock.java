package endint.world.blocks;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import endint.tools.*;
import endint.type.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class BuildCoreBlock extends CoreBlock {
    public float range = 80f;
    public float rotateSpeed = 5;

    public final int timerTarget = timers++, timerTarget2 = timers++;
    public int targetInterval = 15;

    public float buildSpeed = 1f;
    public float buildBeamOffset = 5f;

    public @Nullable UnitType blockUnitType;

    public float elevation = -1f;
    public Color heatColor = Pal.accent.cpy().a(0.9f);

    public float powerOut;

    public BuildCoreBlock(String name) {
        super(name);
        group = BlockGroup.turrets;
        sync = false;
        rotateSpeed = 10f;
        suppressable = true;
        hasPower = true;
        consumesPower = false;
        outputsPower = true;
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        return !tile.floor().isLiquid && !tile.solid() && !tile.isDarkened();
    }

    @Override
    public void setStats(){
        super.setStats();
        stats.add(Stat.basePowerGeneration, powerOut * 60.0f, StatUnit.powerSecond);
    }

    @Override
    public void setBars(){
        super.setBars();

        if(hasPower && outputsPower){
            addBar("power", (BuildCoreBuilding entity) -> new Bar(
                    () -> Core.bundle.format("bar.poweroutput", Strings.fixed(powerOut, 1)),
                    () -> Pal.powerBar,
                    () -> 1)
            );
        }
    }

    @Override
    public void init(){
        super.init();

        if(elevation < 0) elevation = size / 2f;

        blockUnitType = new UnitType("turret-unit-" + name){{
            hidden = true;
            internal = true;
            speed = 0f;
            hitSize = 0f;
            health = 1;
            itemCapacity = 0;
            rotateSpeed = BuildCoreBlock.this.rotateSpeed;
            buildBeamOffset = BuildCoreBlock.this.buildBeamOffset;
            buildRange = BuildCoreBlock.this.range;
            buildSpeed = BuildCoreBlock.this.buildSpeed;
            constructor = BlockUnitUnit::create;
        }};
    }

    public class BuildCoreBuilding extends CoreBuild implements Ranged, ControlBlock, Temperaturec {
        public float rotation = 90;

        public float temperature = 0f;

        @Override
        public float range(){
            return range;
        }

        @Override
        public float getPowerProduction() {
            return powerOut;
        }

        @Override
        public void drawSelect(){
            Drawf.dashCircle(x, y, range(), team.color);
        }

        public BlockUnitc blockUnit;
        public @Nullable
        Unit following;
        public @Nullable
        Teams.BlockPlan lastPlan;
        public float warmup;

        {
            if(blockUnitType != null) blockUnit = (BlockUnitc) blockUnitType.create(team);
            if(blockUnit != null) blockUnit.rotation(90f);
        }

        @Override
        public boolean canControl(){
            return true;
        }

        @Override
        public Unit unit(){
            blockUnit.tile(this);
            blockUnit.team(team);
            return (Unit)blockUnit;
        }

        @Override
        public void updateTile(){
            super.updateTile();

            blockUnit.tile(this);
            blockUnit.team(team);

            rotation = blockUnit.rotation();

            if(blockUnit.activelyBuilding()){
                blockUnit.lookAt(angleTo(blockUnit.buildPlan()));
            }

            checkSuppression();

            blockUnit.buildSpeedMultiplier(potentialEfficiency * timeScale);
            blockUnit.speedMultiplier(potentialEfficiency * timeScale);

            warmup = Mathf.lerpDelta(warmup, blockUnit.activelyBuilding() ? efficiency : 0f, 0.1f);

            if(!isControlled()){
                blockUnit.updateBuilding(true);

                if(following != null){
                    if(!following.isValid() || !following.activelyBuilding()){
                        following = null;
                        blockUnit.plans().clear();
                    }else{
                        blockUnit.plans().clear();
                        blockUnit.plans().addFirst(following.buildPlan());
                        lastPlan = null;
                    }

                }else if(blockUnit.buildPlan() == null && timer(timerTarget, targetInterval)){ //search for new stuff
                    Queue<Teams.BlockPlan> blocks = team.data().plans;
                    for(int i = 0; i < blocks.size; i++){
                        Teams.BlockPlan block = blocks.get(i);
                        if(within(block.x * tilesize, block.y * tilesize, range)){
                            Block btype = content.block(block.block);

                            if(Build.validPlace(btype, blockUnit.team(), block.x, block.y, block.rotation) && (state.rules.infiniteResources || team.rules().infiniteResources || team.items().has(btype.requirements, state.rules.buildCostMultiplier))){
                                blockUnit.addBuild(new BuildPlan(block.x, block.y, block.rotation, content.block(block.block), block.config));
                                //shift build plan to tail so next unit builds something else
                                blocks.addLast(blocks.removeIndex(i));
                                lastPlan = block;
                                break;
                            }
                        }
                    }

                    //still not building, find someone to mimic
                    if(blockUnit.buildPlan() == null){
                        following = null;
                        Units.nearby(team, x, y, range, u -> {
                            if(following  != null) return;

                            if(u.canBuild() && u.activelyBuilding()){
                                BuildPlan plan = u.buildPlan();

                                Building build = world.build(plan.x, plan.y);
                                if(build instanceof ConstructBlock.ConstructBuild && within(build, range)){
                                    following = u;
                                }
                            }
                        });
                    }
                }else if(blockUnit.buildPlan() != null){ //validate building
                    BuildPlan req = blockUnit.buildPlan();

                    //clear break plan if another player is breaking something
                    if(!req.breaking && timer.get(timerTarget2, 30f)){
                        for(Player player : team.data().players){
                            if(player.isBuilder() && player.unit().activelyBuilding() && player.unit().buildPlan().samePos(req) && player.unit().buildPlan().breaking){
                                blockUnit.plans().removeFirst();
                                //remove from list of plans
                                team.data().plans.remove(p -> p.x == req.x && p.y == req.y);
                                return;
                            }
                        }
                    }

                    boolean valid =
                            !(lastPlan != null && lastPlan.removed) &&
                                    ((req.tile() != null && req.tile().build instanceof ConstructBlock.ConstructBuild &&( (ConstructBlock.ConstructBuild)(req.tile().build)).current == req.block) ||
                                            (req.breaking ?
                                                    Build.validBreak(blockUnit.team(), req.x, req.y) :
                                                    Build.validPlace(req.block, blockUnit.team(), req.x, req.y, req.rotation)));

                    if(!valid){
                        //discard invalid request
                        blockUnit.plans().removeFirst();
                        lastPlan = null;
                    }
                }
            }else{ //is being controlled, forget everything
                following = null;
                lastPlan = null;
            }

            //please do not commit suicide
            blockUnit.plans().remove(b -> b.build() == this);

            blockUnit.updateBuildLogic();

            TemperatureController.updateBuilding(this);
        }

        @Override
        public boolean shouldConsume(){
            return true;
        }

        @Override
        public void draw(){
            super.draw();

            //TODO load textures from atlas

            /*Draw.rect(baseRegion, x, y);
            Draw.color();

            Draw.z(Layer.turret);

            Drawf.shadow(region, x - elevation, y - elevation, rotation - 90);
            Draw.rect(region, x, y, rotation - 90);*/

            blockUnit.drawBuilding();
        }

        @Override
        public float warmup(){
            return warmup;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(rotation);
            TypeIO.writePlans(write, blockUnit.plans().toArray(BuildPlan.class));
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            rotation = read.f();
            blockUnit.rotation(rotation);
            blockUnit.plans().clear();
            BuildPlan[] reqs = TypeIO.readPlans(read);
            if(reqs != null){
                for(BuildPlan req : reqs){
                    blockUnit.plans().add(req);
                }
            }
        }

        @Override
        public float temperature() {
            return temperature;
        }

        @Override
        public void addTemperature(float value) {
            temperature += value;
        }
    }
}
