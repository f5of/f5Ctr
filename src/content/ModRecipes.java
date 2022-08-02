package content;

import classes.world.type.Recipe;
import mindustry.content.Items;
import mindustry.type.ItemStack;

public class ModRecipes {
    public static Recipe crinite;

    public static void load(){
        crinite = new Recipe(){{
            itemsIn = ItemStack.with(Items.copper, 3, Items.titanium, 1);
            itemsOut = ItemStack.with(ModItems.crinite, 1);
        }};
    }
}
