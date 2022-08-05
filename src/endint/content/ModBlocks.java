package endint.content;

import arc.math.*;
import arc.util.*;
import endint.type.*;
import endint.world.blocks.*;
import endint.world.blocks.crafting.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;

import static mindustry.type.ItemStack.with;

public class ModBlocks{
    public static Block monolith, midas, criniteFurnace, mixer, wither, furnace, crystallizer, heater, boiler, engine,
            forcedEngine, block;

    public static void load(){
        block = new MultiCrafter("block"){{
            requirements(Category.crafting, ItemStack.with(ModItems.crinite, 40, Items.copper, 100, Items.titanium, 50,
                    ModItems.goldPowder, 100));

            health = 300;
            itemCapacity = 30;

            size = 4;

            consume = new ConsumeAll(new ConsumeAll.Recipe(){{
                liquidsIn = LiquidStack.with(ModLiquids.fireCompound, 20, Liquids.oil, 50);
                itemsIn = ItemStack.with(Items.titanium, 20, Items.graphite, 10, Items.lead, 5);
                itemsOut = ItemStack.with(ModItems.goldPowder, 10, ModItems.salt, 20);
                liquidsOut = LiquidStack.with(Liquids.water, 10, Liquids.slag, 10);
                craftTime = 600;
            }},
                    new ConsumeAll.Recipe(){{
                        liquidsIn = LiquidStack.with(Liquids.water, 30, Liquids.oil, 1);
                        itemsIn = ItemStack.with(Items.copper, 10, Items.graphite, 10, Items.lead, 20);
                        itemsOut = ItemStack.with(Items.surgeAlloy, 8, ModItems.salt, 20);
                        liquidsOut = LiquidStack.with(Liquids.water, 1, Liquids.slag, 80);
                        craftTime = 600;
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

            addTemperatureBar(this);
        }};

        midas = new SeptangoFactory("midas"){{
            requirements(Category.crafting, ItemStack.with(ModItems.crinite, 40, Items.copper, 100, Items.titanium, 50,
            ModItems.goldPowder, 100));

            health = 300;
            itemCapacity = 30;

            size = 4;

            craftTime = 13 * 60;

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

        engine = new SeptangoFactory("engine"){{
            requirements(Category.crafting, ItemStack.with(Items.copper, 20, Items.titanium, 180,
            ModItems.crinite, 20));

            size = 3;
            health = 200;

            craftTime = 600f;

            liquidCapacity = 30f;

            consume(new MultiConsumer(ModRecipes.energyEngine));

            addTemperatureBar(this);
        }};

        forcedEngine = new SeptangoFactory("forced-engine"){{
            requirements(Category.crafting, ItemStack.with(ModItems.carbon, 10, ModItems.salt, 30,
                    ModItems.crinite, 30, Items.titanium, 200));

            size = 3;
            health = 200;

            craftTime = 600f;

            liquidCapacity = 60f;

            consume(new MultiConsumer(ModRecipes.energyForcedEngineOil, ModRecipes.energyForcedEngineFireCompound));

            addTemperatureBar(this);
        }};
    }

    static void addTemperatureBar(Block block){
        Building building = block.buildType.get();
        if(!(building instanceof Temperaturec)) throw new IllegalArgumentException("Building of \"" + block.name + "\" should be instanceof Temperaturec");
        block.addBar("temperature", (Building e) -> {
            Temperaturec entity = e.as();
            return new Bar(
            () -> Strings.fixed(entity.temperature(), 1),
            () -> Liquids.cryofluid.barColor(),
            () -> Mathf.clamp(Mathf.map(entity.temperature(), entity.minWorkableTemperature(), entity.maxWorkableTemperature(), 0, 1f)));
        }
        );
    }
}
