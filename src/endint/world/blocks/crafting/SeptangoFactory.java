package endint.world.blocks.crafting;

import endint.tools.*;
import endint.type.*;
import mindustry.world.blocks.production.*;

public class SeptangoFactory extends GenericCrafter {

    public SeptangoFactory(String name) {
        super(name);
    }

    public class SeptangoFactoryBuild extends GenericCrafterBuild implements Temperaturec {
        public float temperature = 0f;

        @Override
        public float temperature() {
            return temperature;
        }

        @Override
        public void addTemperature(float value) {
            temperature += value;
        }

        @Override
        public void updateTile() {
            super.updateTile();
            TemperatureController.updateBuilding(this);
        }

        @Override
        public boolean canConsume() {
            return canWork() && super.canConsume();
        }

        @Override
        public void craft() {
            super.craft();
        }
    }
}
