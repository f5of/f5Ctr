package endint.content;

import mindustry.content.Items;
import mindustry.content.TechTree;
import mindustry.type.ItemStack;

public class FallenTechTree extends TechTree {
    public static TechNode root;

    public static void load(){
        root = nodeRoot("fallen", EIBlocks.fallenCoreT1, () -> {
            node(EIUnitTypes.bd1, () -> {

            });
            node(EIBlocks.fallenDrillT1, ItemStack.with(EIItems.stone, 100, Items.copper, 50), () -> {

            });
            node(EIBlocks.fallenLabT1, () -> {

            });
            node(EIBlocks.fallenConveyorT1, () -> {
                node(EIBlocks.fallenRouterT1, ItemStack.with(EIItems.stone, 20, Items.copper, 50), () -> {

                });
            });
            node(EIBlocks.fallenPowerNodeT1, ItemStack.with(EIItems.iron, 40, Items.copper, 120), () -> {

            });
        });
    }
}
