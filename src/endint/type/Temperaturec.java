package endint.type;

import mindustry.gen.*;

public interface Temperaturec extends Buildingc{
    float temperature();
    void addTemperature(float value);
    default int temperatureRange() {return block().size * 3;}
    default float maxWorkableTemperature(){return 1000f;}
    default float minWorkableTemperature(){return -200f;}
    default boolean canWork(){
        return minWorkableTemperature() <= temperature() &&
            temperature() <= maxWorkableTemperature();
    }
}
