package endint.content;

import endint.type.ConsumeAll.Recipe;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;

public class ModRecipes {
    public static Recipe lead, crinite, fireCompound, dalcite, water, carbon, oilHeat, fireCompoundHeat, energyEngine,
            energyForcedEngineOil, energyForcedEngineFireCompound;

    public static void load(){
        lead = new Recipe(){{
            itemsIn = ItemStack.with(ModItems.goldPowder, 15);
            itemsOut = ItemStack.with(Items.lead, 1);
            craftTime = 13 * 60;
            powerIn = 200f;
        }};

        crinite = new Recipe(){{
            itemsIn = ItemStack.with(Items.copper, 3, Items.titanium, 1);
            itemsOut = ItemStack.with(ModItems.crinite, 1);
            powerIn = 40f;
            craftTime = 5 * 60;
        }};

        fireCompound = new Recipe(){{
            itemsIn = ItemStack.with(ModItems.dalcite, 10);
            liquidsIn = LiquidStack.with(Liquids.oil, 30);
            liquidsOut = LiquidStack.with(ModLiquids.fireCompound, 40);
            powerIn = 200;
            craftTime = 3 * 60;
        }};

        dalcite = new Recipe(){{
            itemsIn = ItemStack.with(ModItems.salt, 5);
            liquidsIn = LiquidStack.with(Liquids.oil, 10);
            itemsOut = ItemStack.with(ModItems.dalcite, 5);
            powerIn = 44f;
            craftTime = 5 * 60;
        }};

        water = new Recipe(){{
            itemsIn = ItemStack.with(ModItems.ice, 1);
            liquidsOut = LiquidStack.with(Liquids.water, 10);
            powerIn = 5f;
            craftTime = 2 * 60;
        }};

        carbon = new Recipe(){{
            itemsIn = ItemStack.with(Items.titanium, 5);
            liquidsIn = LiquidStack.with(Liquids.oil, 20);
            itemsOut = ItemStack.with(ModItems.carbon, 2);
            powerIn = 20f;
            craftTime = 5 * 60;
        }};

        oilHeat = new Recipe(){{
            liquidsIn = LiquidStack.with(Liquids.oil, 20);
            temperatureProd = 5f;
            craftTime = 60;
        }};

        fireCompoundHeat = new Recipe(){{
            liquidsIn = LiquidStack.with(ModLiquids.fireCompound, 20);
            temperatureProd = 10f;
            craftTime = 60;
        }};

        energyEngine = new Recipe(){{
            liquidsIn = LiquidStack.with(Liquids.oil, 10);
            powerOut = 100;
            temperatureProd = 1f;
            craftTime = 60;
        }};

        energyForcedEngineOil = new Recipe(){{
            liquidsIn = LiquidStack.with(Liquids.oil, 20);
            powerOut = 240;
            temperatureProd = 2f;
            craftTime = 60;
        }};

        energyForcedEngineFireCompound = new Recipe(){{
            liquidsIn = LiquidStack.with(ModLiquids.fireCompound, 20);
            powerOut = 480;
            temperatureProd = 3f;
            craftTime = 60;
        }};
    }
}
