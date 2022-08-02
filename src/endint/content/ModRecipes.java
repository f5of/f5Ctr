package endint.content;

import endint.world.type.Recipe;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;

public class ModRecipes {
    public static Recipe lead, crinite, fireCompound, dalcite, water, carbon, oilHeat, fireCompoundHeat, energyEngine;

    public static void load(){
        lead = new Recipe(){{
            itemsIn = ItemStack.with(ModItems.goldPowder, 15);
            itemsOut = ItemStack.with(Items.lead, 1);
            craftTime = 13f * 60f;
            energyIn = 200f;
        }};

        crinite = new Recipe(){{
            itemsIn = ItemStack.with(Items.copper, 3, Items.titanium, 1);
            itemsOut = ItemStack.with(ModItems.crinite, 1);
            energyIn = 40f;
            craftTime = 5f * 60f;
        }};

        fireCompound = new Recipe(){{
            itemsIn = ItemStack.with(ModItems.dalcite, 10);
            liquidsIn = LiquidStack.with(Liquids.oil, 30);
            liquidsOut = LiquidStack.with(ModLiquids.fireCompound, 40);
            energyIn = 200;
            craftTime = 3f * 60f;
        }};

        dalcite = new Recipe(){{
            itemsIn = ItemStack.with(ModItems.salt, 5);
            liquidsIn = LiquidStack.with(Liquids.oil, 10);
            itemsOut = ItemStack.with(ModItems.dalcite, 5);
            energyIn = 44f;
            craftTime = 5f * 60f;
        }};

        water = new Recipe(){{
            itemsIn = ItemStack.with(ModItems.ice, 1);
            liquidsOut = LiquidStack.with(Liquids.water, 10);
            energyIn = 5f;
            craftTime = 2f * 60f;
        }};

        carbon = new Recipe(){{
            itemsIn = ItemStack.with(Items.titanium, 5);
            liquidsIn = LiquidStack.with(Liquids.oil, 20);
            itemsOut = ItemStack.with(ModItems.carbon, 2);
            energyIn = 20f;
            craftTime = 5f * 60f;
        }};

        oilHeat = new Recipe(){{
            liquidsIn = LiquidStack.with(Liquids.oil, 20);
            temperatureProd = 5f;
            craftTime = 60f;
        }};

        fireCompoundHeat = new Recipe(){{
            liquidsIn = LiquidStack.with(ModLiquids.fireCompound, 20);
            temperatureProd = 10f;
            craftTime = 60f;
        }};

        energyEngine = new Recipe(){{
            liquidsIn = LiquidStack.with(Liquids.oil, 10);
            energyOut = 100;
            temperatureProd = 1f;
            craftTime = 60f;
        }};
    }
}
