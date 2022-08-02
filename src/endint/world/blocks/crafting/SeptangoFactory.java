package endint.world.blocks.crafting;

import endint.tools.*;
import endint.world.type.*;
import mindustry.world.blocks.production.*;

public class SeptangoFactory extends GenericCrafter {
    public float temperaturePerSecond = 0.25f;

    public SeptangoFactory(String name) {
        super(name);
    }

    public class SeptangoFactoryBuild extends GenericCrafterBuild implements Temperaturec {
        public float temperature = 0f;

        @Override
        public float getTemperature() {
            return temperature;
        }

        @Override
        public void addTemperature(float value) {
            temperature += value;
        }

        @Override
        public float getTemperatureProduction() {
            return warmup > 0 ? temperaturePerSecond : 0f;
        }

        @Override
        public float getStatsTemperatureProduction() {
            return temperaturePerSecond;
        }

        @Override
        public void updateTile() {
            super.updateTile();
            TemperatureController.handleBuilding(this);
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
