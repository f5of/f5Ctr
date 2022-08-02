package content;

import arc.util.Strings;
import classes.world.blocks.BuildCoreBlock;
import classes.world.blocks.crafting.SeptangoFactory;
import classes.world.type.MultiConsumer;
import classes.world.type.Temperaturec;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.UnitTypes;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;

import static mindustry.type.ItemStack.with;

public class ModBlocks {
    public static Block monolith, midas, criniteFurnace, mixer, wither, furnace, crystallizer, heater, boiler;

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
            requirements(Category.crafting, ItemStack.with(ModItems.crinite, 40, Items.copper, 100, Items.titanium, 50,
                    ModItems.goldPowder, 100));

            health = 300;
            itemCapacity = 30;

            size = 4;

            craftTime = 13 * 60;

            consumePower(200f/60f);
            consume(new MultiConsumer(ModRecipes.lead));

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
            consume(new MultiConsumer(ModRecipes.fireCompound));

            addTemperatureBar(this);
        }};

        wither = new SeptangoFactory("wither"){{
            requirements(Category.crafting, ItemStack.with(ModItems.crinite, 40, Items.titanium, 20, ModItems.salt, 100));

            health = 400;
            itemCapacity = 20;
            liquidCapacity = 50;

            size = 3;

            craftTime = 5 * 60;

            consume(new MultiConsumer(ModRecipes.dalcite));

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
            consume(new MultiConsumer(ModRecipes.water));

            addTemperatureBar(this);
        }};

        crystallizer = new SeptangoFactory("crystallizer"){{
            requirements(Category.crafting, ItemStack.with(ModItems.crinite, 50, ModItems.salt, 20,
                    ModItems.aluminium, 30, ModItems.carbon, 10, ModItems.goldPowder, 10));

            health = 500;
            itemCapacity = 15;
            liquidCapacity = 50;

            size = 2;

            craftTime = 5 * 60;

            consumePower(20f/60f);
            consume(new MultiConsumer(ModRecipes.carbon));

            addTemperatureBar(this);
        }};

        boiler = new SeptangoFactory("boiler"){{
            requirements(Category.crafting, ItemStack.with(ModItems.aluminium, 20, Items.titanium, 30,
                    ModItems.crinite, 40));

            size = 3;
            health = 100;

            liquidCapacity = 60;

            craftTime = 2f;

            consume(new MultiConsumer(ModRecipes.oilHeat, ModRecipes.fireCompoundHeat));

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
    }
}
