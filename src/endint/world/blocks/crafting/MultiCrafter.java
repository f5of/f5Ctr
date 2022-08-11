package endint.world.blocks.crafting;

import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import endint.type.ConsumeAll;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.consumers.Consume;
import mindustry.world.meta.BlockStatus;

public class MultiCrafter extends Block {
    public ConsumeAll consume;

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
            if(consume.canWork(this) == BlockStatus.active) {
                progress += 1f / consume.getRecipe(currentRecipe).craftTime
                        * consume.getEfficiency(this);

            }
            else progress = 0f;

            if(progress >= 1f) {
                consume.handleCraft(this);
                progress = 0f;
            }


            for (ItemStack itemStack : consume.getRecipe(currentRecipe).itemsOut) {
                dump(itemStack.item);
            }
            for (LiquidStack liquidStack : consume.getRecipe(currentRecipe).liquidsOut) {
                dumpLiquid(liquidStack.liquid);
            }
        }

        @Override
        public void buildConfiguration(Table table) {
            consume.buildConfiguration(this, table);
        }

        @Override
        public BlockStatus status() {
            return consume.canWork(this);
        }

        @Override
        public float getPowerProduction() {
            return consume.getRecipe(currentRecipe).powerOut / consume.getRecipe(currentRecipe).craftTime
                    * consume.getEfficiency(this);
        }

        @Override
        public float efficiency() {
            return consume.getEfficiency(this);
        }
    }
}
