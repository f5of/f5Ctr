package endint.tools;

import arc.util.*;
import endint.type.*;
import mindustry.*;
import mindustry.gen.*;

public class TemperatureController{
    public static float temperatureRegressionPerTic = 0.5f / Time.toSeconds;

    public static <T extends Building&Temperaturec> void updateBuilding(T building){
        int range = building.temperatureRange();
        float delta;
        Building owner;
        for(int x = -range + 1; x < range; x++){
            for(int y = -range + 1; y < range; y++){
                if(x * x + y * y <= range * range){
                    owner = Vars.world.build(building.tileX() + x, building.tileY() + y);
                    if(owner instanceof Temperaturec temperatureOwner){
                        if(temperatureOwner.temperature() < building.temperature()){
                            delta = building.temperature() - temperatureOwner.temperature();
                            if(delta > 1f){
                                delta = 1f;
                            }
                            temperatureOwner.addTemperature(delta);
                            building.addTemperature(-delta);
                        }
                    }
                }
            }
        }
        owner = null;
        building.addTemperature(-temperatureRegressionPerTic * Time.delta);
    }
}
