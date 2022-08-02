package endint.world.type;

public interface Temperaturec {
    float getTemperature();
    void addTemperature(float value);
    default float getTemperatureRange() {return -1;}
    default float getTemperatureProduction(){return getStatsTemperatureProduction();}
    default float getStatsTemperatureProduction(){return 0f;}
    default float maxWorkableTemperature(){return 1000f;}
    default float minWorkableTemperature(){return -200f;}
    default boolean canWork(){return minWorkableTemperature() <= getTemperature() &&
            getTemperature() <= maxWorkableTemperature();}
}
