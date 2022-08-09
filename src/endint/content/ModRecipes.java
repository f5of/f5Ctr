package endint.content;

import endint.type.Recipe;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;

public class ModRecipes {
    public static Recipe lead, crinite, fireCompound, dalcite, water, carbon, oilHeat, fireCompoundHeat, energyEngine,
            energyForcedEngineOil, energyForcedEngineFireCompound;

    public static void load(){
        lead = new Recipe(){{
            itemsIn = ItemStack.with(EIItems.goldPowder, 15);
            itemsOut = ItemStack.with(Items.lead, 1);
            craftTime = 13f * 60f;
            energyIn = 200f;
        }};

        crinite = new Recipe(){{
            itemsIn = ItemStack.with(Items.copper, 3, Items.titanium, 1);
            itemsOut = ItemStack.with(EIItems.crinite, 1);
            energyIn = 40f;
            craftTime = 5f * 60f;
        }};

        fireCompound = new Recipe(){{
            itemsIn = ItemStack.with(EIItems.dalcite, 10);
            liquidsIn = LiquidStack.with(Liquids.oil, 30);
            liquidsOut = LiquidStack.with(ModLiquids.fireCompound, 40);
            energyIn = 200;
            craftTime = 3f * 60f;
        }};

        dalcite = new Recipe(){{
            itemsIn = ItemStack.with(EIItems.salt, 5);
            liquidsIn = LiquidStack.with(Liquids.oil, 10);
            itemsOut = ItemStack.with(EIItems.dalcite, 5);
            energyIn = 44f;
            craftTime = 5f * 60f;
        }};

        water = new Recipe(){{
            itemsIn = ItemStack.with(EIItems.ice, 1);
            liquidsOut = LiquidStack.with(Liquids.water, 10);
            energyIn = 5f;
            craftTime = 2f * 60f;
        }};

        carbon = new Recipe(){{
            itemsIn = ItemStack.with(Items.titanium, 5);
            liquidsIn = LiquidStack.with(Liquids.oil, 20);
            itemsOut = ItemStack.with(EIItems.carbon, 2);
            energyIn = 20f;
            craftTime = 5f * 60f;
        }};

        oilHeat = new Recipe(){{
            liquidsIn = LiquidStack.with(Liquids.oil, 20);
            temperatureOut = 5f;
            craftTime = 60f;
        }};

        fireCompoundHeat = new Recipe(){{
            liquidsIn = LiquidStack.with(ModLiquids.fireCompound, 20);
            temperatureOut = 10f;
            craftTime = 60f;
        }};

        energyEngine = new Recipe(){{
            liquidsIn = LiquidStack.with(Liquids.oil, 10);
            energyOut = 100;
            temperatureOut = 1f;
            craftTime = 60f;
        }};

        energyForcedEngineOil = new Recipe(){{
            liquidsIn = LiquidStack.with(Liquids.oil, 20);
            energyOut = 240;
            temperatureProd = 2f;
            craftTime = 60f;
        }};

        energyForcedEngineFireCompound = new Recipe(){{
            liquidsIn = LiquidStack.with(ModLiquids.fireCompound, 20);
            energyOut = 480;
            temperatureProd = 3f;
            craftTime = 60f;
        }};
    }
}
