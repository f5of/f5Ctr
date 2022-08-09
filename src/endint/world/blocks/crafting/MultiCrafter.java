package endint.world.blocks.crafting;

import arc.scene.ui.layout.Table;
import endint.type.ConsumeAll;
import mindustry.gen.Building;
import mindustry.type.Liquid;
import mindustry.world.Block;
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
    }

    @Override
    public void setStats() {
        super.setStats();
        consume.setStats(stats);
    }

    @Override
    public void setBars() {
        super.setBars();
        consume.setBars(this);
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
            if(consume.canWork(this) == BlockStatus.active) progress += 1f / consume.getRecipe(currentRecipe).craftTime;
            else progress = 0f;

            if(progress >= 1f) consume.handleCraft(this);
        }

        @Override
        public void buildConfiguration(Table table) {
            consume.buildConfiguration(this, table);
        }

        @Override
        public BlockStatus status() {
            return consume.canWork(this);
        }
    }
}
