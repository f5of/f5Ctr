package endint.content;

import arc.math.*;
import arc.util.*;
import endint.annotations.TemperatureBlock;
import endint.type.*;
import endint.world.blocks.*;
import endint.world.blocks.crafting.*;
import endint.world.blocks.distribution.DoubleConveyor;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;

import java.lang.reflect.Field;

import static mindustry.type.ItemStack.with;

public class ModBlocks{
    public static Block monolith, midas, criniteFurnace, mixer, wither, furnace, crystallizer, heater, boiler, engine,
            forcedEngine, block, conv;

    public static void load(){
        conv = new DoubleConveyor("conv-test"){{
            requirements(Category.distribution, ItemStack.with(ModItems.crinite, 40, Items.copper, 100, Items.titanium, 50,
                    ModItems.goldPowder, 100));

            size = 1;
            health = 10;
            capacity = 3;
        }};

        block = new SeptangoMultiCrafter("block"){{
            requirements(Category.crafting, ItemStack.with(ModItems.crinite, 40, Items.copper, 100, Items.titanium, 50,
                    ModItems.goldPowder, 100));

            health = 300;
            itemCapacity = 30;

            size = 4;

            consume = new ConsumeAll(
                    new ConsumeAll.Recipe(){{
                        powerIn = 3f;
                        itemsIn = with(Items.coal, 10);
                        powerOut = 60f;
                        craftTime = 60;
                    }},
                    new ConsumeAll.Recipe(){{
                        powerIn = 60f;
                        itemsIn = with(Items.pyratite, 20);
                        powerOut = 6000f;
                        craftTime = 5;
                    }},
                    new ConsumeAll.Recipe(){{
                        powerIn = 100f;
                        itemsIn = with(Items.plastanium, 20);
                        itemsOut = with(Items.thorium, 1);
                        craftTime = 6;
                    }});
        }};

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

            powerOut = 200f / 60f;
        }};

        midas = new SeptangoMultiCrafter("midas"){{
            requirements(Category.crafting, ItemStack.with(ModItems.crinite, 40, Items.copper, 100, Items.titanium, 50,
            ModItems.goldPowder, 100));

            health = 300;
            itemCapacity = 30;

            size = 4;

            consume = new ConsumeAll(ModRecipes.lead);
        }};

        criniteFurnace = new SeptangoMultiCrafter("crinite-furnace"){{
            requirements(Category.crafting, ItemStack.with(ModItems.aluminium, 20, Items.copper, 50, Items.titanium, 40));

            health = 150;
            itemCapacity = 20;

            size = 2;

            consume = new ConsumeAll(ModRecipes.crinite);
        }};

        mixer = new SeptangoMultiCrafter("mixer"){{
            requirements(Category.crafting, ItemStack.with(ModItems.crinite, 60, Items.titanium, 240));

            health = 500;
            itemCapacity = 20;
            liquidCapacity = 100;

            size = 3;

            consume = new ConsumeAll(ModRecipes.fireCompound);
        }};

        wither = new SeptangoMultiCrafter("wither"){{
            requirements(Category.crafting, ItemStack.with(ModItems.crinite, 40, Items.titanium, 20, ModItems.salt, 100));

            health = 400;
            itemCapacity = 20;
            liquidCapacity = 50;

            size = 3;

            consume = new ConsumeAll(ModRecipes.dalcite);
        }};

        furnace = new SeptangoMultiCrafter("furnace"){{
            requirements(Category.crafting, ItemStack.with(ModItems.crinite, 60, Items.titanium, 240));

            health = 500;
            itemCapacity = 10;
            liquidCapacity = 50;

            size = 1;

            consume = new ConsumeAll(ModRecipes.water);
        }};

        crystallizer = new SeptangoMultiCrafter("crystallizer"){{
            requirements(Category.crafting, ItemStack.with(ModItems.crinite, 50, ModItems.salt, 20,
            ModItems.aluminium, 30, ModItems.carbon, 10, ModItems.goldPowder, 10));

            health = 500;
            itemCapacity = 15;
            liquidCapacity = 50;

            size = 2;

            consume = new ConsumeAll(ModRecipes.carbon);
        }};

        boiler = new SeptangoMultiCrafter("boiler"){{
            requirements(Category.crafting, ItemStack.with(ModItems.aluminium, 20, Items.titanium, 30,
            ModItems.crinite, 40));

            size = 3;
            health = 100;

            liquidCapacity = 60;

            consume = new ConsumeAll(ModRecipes.oilHeat, ModRecipes.fireCompoundHeat);
        }};

        engine = new SeptangoMultiCrafter("engine"){{
            requirements(Category.crafting, ItemStack.with(Items.copper, 20, Items.titanium, 180,
            ModItems.crinite, 20));

            size = 3;
            health = 200;

            liquidCapacity = 30f;

            consume = new ConsumeAll(ModRecipes.energyEngine);
        }};

        forcedEngine = new SeptangoMultiCrafter("forced-engine"){{
            requirements(Category.crafting, ItemStack.with(ModItems.carbon, 10, ModItems.salt, 30,
                    ModItems.crinite, 30, Items.titanium, 200));

            size = 3;
            health = 200;

            liquidCapacity = 60f;

            consume = new ConsumeAll(ModRecipes.energyForcedEngineOil, ModRecipes.energyForcedEngineFireCompound);
        }};
        
        addTemperatureBar();
    }

    static void addTemperatureBar(){
        for (Field field : ModBlocks.class.getDeclaredFields()) {
            try {
                if(field.get(null) instanceof Block)
                    Log.info(((Block) field.get(null)).buildType.get());
                    if(((Block) field.get(null)).buildType.get() instanceof Temperaturec) {
                        Log.info(1);
                        ((Block) field.get(null)).addBar("temperature", (Building e) -> {
                            Temperaturec entity = e.as();
                            return new Bar(
                                    () -> Strings.fixed(entity.temperature(), 1),
                                    () -> Liquids.cryofluid.barColor(),
                                    () -> Mathf.clamp(entity.temperature() - entity.minWorkableTemperature(), 0,
                                            entity.maxWorkableTemperature() - entity.minWorkableTemperature()) /
                                            entity.maxWorkableTemperature() + entity.minWorkableTemperature());
                        });
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
