package endint.world.blocks.crafting;

import endint.annotations.TemperatureBlock;
import endint.type.Temperaturec;

@TemperatureBlock
public class SeptangoMultiCrafter extends MultiCrafter{
    public float maxWorkableTemperature = -210, minWorkableTemperature = 1000;
    public int temperatureProductRange = -1;

    public SeptangoMultiCrafter(String name) {
        super(name);
    }

    public class SeptangoMultiCrafterBuild extends MultiCrafterBuild implements Temperaturec {
        float temperature = 50;

        @Override
        public float temperature() {
            return temperature;
        }

        @Override
        public void addTemperature(float value) {
            temperature += value;
        }

        @Override
        public int temperatureRange() {
            return temperatureProductRange == -1 ? Temperaturec.super.temperatureRange() : temperatureProductRange;
        }

        @Override
        public float maxWorkableTemperature() {
            return maxWorkableTemperature;
        }

        @Override
        public float minWorkableTemperature() {
            return Temperaturec.super.minWorkableTemperature();
        }
    }
}
