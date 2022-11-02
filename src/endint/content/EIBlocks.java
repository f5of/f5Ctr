package endint.content;

import endint.world.blocks.core.Core;
import endint.world.blocks.distribution.Conveyor;
import endint.world.blocks.distribution.Router;
import endint.world.blocks.power.PowerNode;
import endint.world.blocks.production.Drill;
import endint.world.blocks.science.Laboratory;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.*;

import static mindustry.type.ItemStack.with;


public class EIBlocks {
    public static Block fallenCoreT1, fallenDrillT1, fallenLabT1, fallenConveyorT1, fallenRouterT1, fallenPowerNodeT1;

    public static void load() {
        fallenCoreT1 = new Core("fallen-core-t1", EIFractions.fallen){{
            requirements(Category.effect, ItemStack.with(Items.copper, 300, Items.titanium, 100, EIItems.iron, 500));
            health = 1000;
            itemCapacity = 5000;
            size = 2;
            unitType = EIUnitTypes.bd1;
            alwaysUnlocked = true;
            configurable = true;
        }};

        fallenDrillT1 = new Drill("fallen-drill-t1", EIFractions.fallen){{
            requirements(Category.production, with(Items.copper, 35, Items.graphite, 30, Items.silicon, 30, Items.titanium, 20));
            drillTime = 80;
            size = 1;
            hasPower = false;
            tier = 1;
            updateEffect = Fx.pulverizeSmall;
            drillEffect = Fx.mineSmall;
        }};

        fallenLabT1 = new Laboratory("fallen-laboratory-t1", EIFractions.fallen){{
            requirements(Category.production, with(Items.copper, 35, Items.graphite, 30, Items.silicon, 30, Items.titanium, 20));
            size = 2;
        }};

        fallenConveyorT1 = new Conveyor("fallen-conveyor-t1", EIFractions.fallen){{
            requirements(Category.distribution, with(Items.copper, 2, EIItems.stone, 1));
            alwaysUnlocked = true;
            health = 50;
            speed = 2f / 60f;
            itemCapacity = 2;

            hasPower = true;
            consumesPower = true;
            conductivePower = true;

            underBullets = true;
            consumePower(1f / 60f);
        }};

        fallenRouterT1 = new Router("fallen-router-t1", EIFractions.fallen){{
            requirements(Category.distribution, with(Items.copper, 4, EIItems.stone, 2));
            health = 50;
            speed = 8f / 60f;
            itemCapacity = 5;

            hasPower = true;
            consumesPower = true;
            conductivePower = true;

            underBullets = true;
            consumePower(2f / 60f);
        }};

        fallenPowerNodeT1 = new PowerNode("fallen-power-node-t1", EIFractions.fallen){{
            requirements(Category.power, with(Items.copper, 5, EIItems.iron, 3));
            maxNodes = 10;
            laserRange = 10;
            hasPower = true;
            consumesPower = true;
            consumePower(10f / 60f);
            consumePowerBuffered(4000f);
        }};
    }
}