package endint.content;

import arc.util.*;
import endint.world.blocks.*;
import endint.world.blocks.crafting.*;
import endint.world.type.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;

import static mindustry.type.ItemStack.with;

public class ModBlocks {
    public static Block monolith, midas, criniteFurnace, mixer, wither, furnace, crystallizer, heater, oilBoiler, fireCompoundBoiler;

    public static void load(){
        monolith = new BuildCoreBlock("monolith"){{
            requirements(Category.effect, with(Items.silicon, 150, Items.thorium, 60));
            outlineColor = Pal.darkOutline;

            range = 240f;
            buildSpeed = 2f;

            alwaysUnlocked = true;

            isFirstTier = true;
            unitType = UnitTypes.alpha;
            health = 1000;
            itemCapacity = 10000;
            size = 5;

            unitCapModifier = 16;

            powerOut = 200f/60f;

            addTemperatureBar(this);
        }};

        midas = new SeptangoFactory("midas"){{
            temperaturePerSecond = 0.6f;
            requirements(Category.crafting, ItemStack.with(ModItems.crinite, 40, Items.copper, 100, Items.titanium, 50,
                    ModItems.goldPowder, 100));

            health = 300;
            itemCapacity = 30;

            size = 4;

            craftTime = 13 * 60;

            consumePower(200f/60f);
            consumeItem(ModItems.goldPowder, 15);
            outputItem = new ItemStack(Items.lead, 1);

            addTemperatureBar(this);
        }};

        criniteFurnace = new SeptangoFactory("crinite-furnace"){{
            requirements(Category.crafting, ItemStack.with(ModItems.aluminium, 20, Items.copper, 50, Items.titanium, 40));

            health = 150;
            itemCapacity = 20;

            size = 2;

            craftTime = 5 * 60;

            consume(new MultiConsumer(ModRecipes.crinite));

            addTemperatureBar(this);
        }};

        mixer = new SeptangoFactory("mixer"){{
            requirements(Category.crafting, ItemStack.with(ModItems.crinite, 60, Items.titanium, 240));

            health = 500;
            itemCapacity = 20;
            liquidCapacity = 100;

            size = 3;

            craftTime = 3 * 60;

            consumePower(200f/60f);
            consumeLiquid(Liquids.oil, 30f/60f);
            consumeItems(ItemStack.with(ModItems.dalcite, 10));
            outputLiquids = LiquidStack.with(ModLiquids.fireCompound, 40f/60f);

            addTemperatureBar(this);
        }};

        wither = new SeptangoFactory("wither"){{
            requirements(Category.crafting, ItemStack.with(ModItems.crinite, 40, Items.titanium, 20, ModItems.salt, 100));

            health = 400;
            itemCapacity = 20;
            liquidCapacity = 50;

            size = 3;

            craftTime = 5 * 60;

            consumeLiquid(Liquids.oil, 10f/60f);
            consumeItems(ItemStack.with(ModItems.salt, 5));
            outputItems = ItemStack.with(ModItems.dalcite, 5);

            addTemperatureBar(this);
        }};

        furnace = new SeptangoFactory("furnace"){{
            requirements(Category.crafting, ItemStack.with(ModItems.crinite, 60, Items.titanium, 240));

            health = 500;
            itemCapacity = 10;
            liquidCapacity = 50;

            size = 1;

            craftTime = 2 * 60;

            consumePower(200f/60f);
            consumeItems(ItemStack.with(ModItems.ice, 1));
            outputLiquids = LiquidStack.with(Liquids.water, 10f/60f);

            addTemperatureBar(this);
        }};

        crystallizer = new SeptangoFactory("crystallizer"){{
            temperaturePerSecond = 0.4f;
            requirements(Category.crafting, ItemStack.with(ModItems.crinite, 50, ModItems.salt, 20,
                    ModItems.aluminium, 30, ModItems.carbon, 10, ModItems.goldPowder, 10));

            health = 500;
            itemCapacity = 15;
            liquidCapacity = 50;

            size = 2;

            craftTime = 5 * 60;

            consumePower(20f/60f);
            consumeItems(ItemStack.with(Items.titanium, 5));
            consumeLiquid(Liquids.oil, 20f/60f);
            outputItems = ItemStack.with(ModItems.carbon, 2);

            addTemperatureBar(this);
        }};

        heater = new SeptangoHeater("heater"){{
            temperaturePerSecond = 5f;
            temperatureRange = 10f;
            requirements(Category.crafting, ItemStack.with(ModItems.aluminium, 30, Items.titanium, 50));

            size = 2;
            health = 50;

            consumePower(400f/60f);

            addTemperatureBar(this);
        }};

        oilBoiler = new SeptangoHeater("oil-boiler"){{
            temperaturePerSecond = 20f;
            temperatureRange = 10f;
            requirements(Category.crafting, ItemStack.with(ModItems.aluminium, 20, Items.titanium, 30,
                    ModItems.crinite, 40));

            size = 3;
            health = 100;

            consumeLiquid(Liquids.oil, 20f / 60f);
            consumePower(2f / 60f);

            addTemperatureBar(this);
        }};
    }

    static void addTemperatureBar(Block build){
        build.addBar("temperature", (Building entity) -> new Bar(
                () -> {
                    if(entity instanceof Temperaturec) {
                        return Strings.fixed(((Temperaturec) entity).getTemperature(), 1);
                    }
                    return "F";
                },
                () -> Liquids.cryofluid.barColor(),
                () -> {
                    if(entity instanceof Temperaturec) {
                        return Math.min(Math.max((((Temperaturec) entity).getTemperature() - ((Temperaturec) entity).minWorkableTemperature()) /
                                (((Temperaturec) entity).maxWorkableTemperature() - ((Temperaturec) entity).minWorkableTemperature()), 0f), 1f);
                    }
                    return 1;
                })
        );
        build.stats.add(ModStats.tempProduct, ((Temperaturec) build.newBuilding()).getStatsTemperatureProduction(), ModStats.temperatureUnit);
    }
}
