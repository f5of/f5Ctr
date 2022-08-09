package endint.world.blocks.distribution;

import arc.Core;
import arc.func.Prov;
import arc.graphics.Texture;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Log;
import arc.util.Tmp;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.entities.TargetPriority;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.ItemBridge;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

import static mindustry.Vars.itemSize;

public class DoubleConveyor extends Block {
    public float maxSpeed = 1f / 60f;

    public int capacity = 3;

    TextureRegion reg;

    public DoubleConveyor(String name) {
        super(name);
        rotate = true;
        update = true;
        group = BlockGroup.transportation;
        hasItems = true;
        priority = TargetPriority.transport;
        conveyorPlacement = true;
        underBullets = true;

        itemCapacity = capacity;

        unloadable = false;
        noUpdateDisabled = false;
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.itemsMoved, maxSpeed, StatUnit.itemsSecond);
    }

    public class DoubleConveyorBuild extends Building {
        public Seq<ConveyorItem> conveyorItems = new Seq<ConveyorItem>();

        protected Building tempBuild;
        protected ConveyorItem tempItem;

        public float getMaxSpeed(){
            return maxSpeed;
        }

        public float getCapacity(){
            return capacity;
        }

        @Override
        public void updateTile() {
            conveyorItems.each(ConveyorItem::update);
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            if(source.left() == this || source.front() == this){
                return conveyorItems.count(i -> i.place == 0) < capacity;
            }else if(source.right() == this || source.back() == this){
                return conveyorItems.count(i -> i.place == 1) < capacity;
            }
            return false;
        }

        @Override
        public void handleItem(Building source, Item item) {
            if(source.left() == this || source.front() == this){
                conveyorItems.add(new ConveyorItem(item, () -> this, (short) 0));
            }else if(source.right() == this || source.back() == this){
                conveyorItems.add(new ConveyorItem(item, () -> this, (short) 1));
            }
        }

        public void dumpItem(){
            tempItem = conveyorItems.find(i -> i.progress >= 1f);

            tempBuild = front();

            if(tempBuild == null) return;

            if(tempBuild.acceptItem(this, tempItem.item))
                conveyorItems.remove(tempItem);
                tempBuild.handleItem(this, tempItem.item);
        }

        @Override
        public void draw() {
            super.draw();
            conveyorItems.each(ConveyorItem::draw);
        }

        class ConveyorItem {
            public Item item;
            public Prov<DoubleConveyorBuild> owner;
            public float progress = 0f;
            public short place;

            protected ConveyorItem tempItem;

            public ConveyorItem(Item item, Prov<DoubleConveyorBuild> owner, short place){
                this.item = item;
                this.owner = owner;
                this.place = place;
            }

            public void update(){
                if(progress >= 1f) {
                    owner.get().dumpItem();
                    return;
                }

                tempItem = null;

                owner.get().conveyorItems.each(i -> {
                    if(i.place == place)
                        if(i != this && Math.abs(i.progress - progress) <= progress / owner.get().getCapacity()
                                && i.progress > progress) {
                            tempItem = i;
                            Log.info(progress);
                        }
                });

                if(tempItem == null)
                    progress += owner.get().getMaxSpeed();
            }

            public void drawItem(){
                float cos = Mathf.cosDeg(360-owner.get().rotation);
                float sin = Mathf.sinDeg(360-owner.get().rotation);

                Vec2 vec = new Vec2(place == 0 ? sin * (0.5f - progress) : sin * (0.5f + progress),
                        place == 0 ? cos * (0.5f - progress) : cos * (0.5f + progress));

                Draw.rect(item.fullIcon, owner.get().x + 4 + vec.x * 8, vec.y * 8 + owner.get().y + 4, itemSize, itemSize);
            }

            public void draw(){
                drawItem();
            }
        }
    }
}
