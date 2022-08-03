package endint.tools;

import endint.*;
import endint.type.*;
import mindustry.*;
import mindustry.gen.*;

public class TemperatureController{
    public static <T extends Building&Temperaturec> void updateBuilding(T building){
        int range = building.temperatureRange() == -1 ? building.block().size * 3 : (int)(building).temperatureRange();
        Building owner;
        float delta;
        for(int x = -range + 1; x < range; x++){
            for(int y = -range + 1; y < range; y++){
                if(x * x + y * y <= range * range){
                    owner = Vars.world.build(building.tileX() + x, building.tileY() + y);
                    if(owner instanceof Temperaturec){
                        if(((Temperaturec)owner).temperature() < building.temperature()){
                            delta = building.temperature() - ((Temperaturec)owner).temperature();
                            if(delta > 1f){
                                delta = 1f;
                            }
                            owner.<Temperaturec>as().addTemperature(delta);
                            building.addTemperature(-delta);
                        }
                    }
                }
            }
        }
        building.addTemperature(EndlessInterstellarVars.temperatureRegressionPerSecond / -60f);
    }
}
