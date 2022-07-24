package content;

import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.game.Team;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BuildVisibility;

import static mindustry.type.ItemStack.with;

public class ModBlocks {
    public static Block smallTerminal;

    public static void load(){
        smallTerminal = new CoreBlock("small-terminal"){{
            requirements(Category.effect, with(Items.copper, 1000, Items.lead, 800));
            alwaysUnlocked = true;

            isFirstTier = true;
            unitType = UnitTypes.alpha;
            health = 600;
            itemCapacity = 2000;
            size = 2;

            unitCapModifier = 4;
        }
            @Override
            public boolean canPlaceOn(Tile tile, Team team, int rotation) {
                return !tile.floor().isLiquid && !tile.solid() && !tile.isDarkened();
            }
        };
    }
}
