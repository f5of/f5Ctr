package endint.type;

public interface Temperaturec {
    float temperature();
    void addTemperature(float value);
    default float temperatureRange() {return -1;}
    default float maxWorkableTemperature(){return 1000f;}
    default float minWorkableTemperature(){return -200f;}
    default boolean canWork(){
        return minWorkableTemperature() <= temperature() &&
            temperature() <= maxWorkableTemperature();
    }
}
