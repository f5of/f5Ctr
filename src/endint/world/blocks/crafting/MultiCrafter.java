package endint.world.blocks.crafting;

import arc.graphics.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import endint.type.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

public class MultiCrafter extends Block {
    public RecipeConsume consume;

    public MultiCrafter(String name) {
        super(name);
        solid = true;
        update = true;
        destructible = true;
        configurable = true;
    }

    @Override
    public void init() {
        super.init();
        consume.init(this);
        initConsumers();
    }

    void initConsumers(){
        consumers = consumeBuilder.toArray(Consume.class);
        optionalConsumers = consumeBuilder.select(consume -> consume.optional && !consume.ignore()).toArray(Consume.class);
        nonOptionalConsumers = consumeBuilder.select(consume -> !consume.optional && !consume.ignore()).toArray(Consume.class);
        updateConsumers = consumeBuilder.select(consume -> consume.update && !consume.ignore()).toArray(Consume.class);
    }

    @Override
    public void setBars() {
        addBar("health", entity -> new Bar("stat.health", Pal.health, entity::healthf).blink(Color.white));
    }

    @Override
    public void setStats() {
        super.setStats();
        consume.setStats(stats);
    }

    public class MultiCrafterBuild extends Building{
        public int currentRecipe = 0;
        public float progress;

        @Override
        public void displayConsumption(Table table) {
            consume.display(this, table);
        }

        @Override
        public void updateTile() {
            if(status() == BlockStatus.active) {
                //TODO add edelta()
                progress += 1f / consume.recipes[currentRecipe].craftTime
                        * consume.getEfficiency(this) * Time.delta;

            }else{
                progress = 0f;
            }

            if(progress >= 1f) {
                consume.handleCraft(this);
                progress %= 1f;
            }


            for (ItemStack itemStack : consume.recipes[currentRecipe].itemsOut) {
                dump(itemStack.item);
            }
            for (LiquidStack liquidStack : consume.recipes[currentRecipe].liquidsOut) {
                dumpLiquid(liquidStack.liquid);
            }
        }

        @Override
        public void buildConfiguration(Table table) {
            consume.buildConfiguration(this, table);
        }

        @Override
        public BlockStatus status() {
            return consume.status(this);
        }

        @Override
        public float getPowerProduction() {
            return consume.recipes[currentRecipe].powerOut / consume.recipes[currentRecipe].craftTime
                    * consume.getEfficiency(this);
        }

        @Override
        public float efficiency() {
            return consume.getEfficiency(this);
        }
    }
}
