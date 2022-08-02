package classes.world.blocks.crafting;

import arc.graphics.Color;
import classes.tools.TemperatureController;
import classes.world.type.Temperaturec;
import mindustry.graphics.Drawf;
import mindustry.world.blocks.production.GenericCrafter;

public class SeptangoHeater extends GenericCrafter {
    public float temperaturePerSecond = 1f;
    public float temperatureRange = -1;

    public SeptangoHeater(String name) {
        super(name);
    }

    public class SeptangoHeaterBuild extends GenericCrafterBuild implements Temperaturec {
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
        public float getTemperatureRange() {
            return temperatureRange;
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

        @Override
        public void drawLight() {
            Drawf.light(x, y, temperatureRange, Color.red, 1f);
        }
    }
}
