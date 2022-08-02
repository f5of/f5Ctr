package endint.tools;

import endint.world.type.*;
import mindustry.*;
import mindustry.gen.*;

public class TemperatureController {
    public static void handleBuilding(Building building){
        if(building instanceof Temperaturec){
            int range = ((Temperaturec) building).getTemperatureRange() == -1 ? building.block().size * 3 : (int) ((Temperaturec) building).getTemperatureRange();
            Building owner;
            float delta;
            for (int x = -range+1; x < range; x++) {
                for (int y = -range+1; y < range; y++) {
                    if(x * x + y * y <= range * range){
                        owner = Vars.world.build(building.tileX() + x, building.tileY() + y);
                        if(owner instanceof Temperaturec){
                            if(((Temperaturec) owner).getTemperature() < ((Temperaturec) building).getTemperature()){
                                delta = ((Temperaturec) building).getTemperature() - ((Temperaturec) owner).getTemperature();
                                if(delta > 1f){
                                    delta = 1f;
                                }
                                ((Temperaturec) owner).addTemperature(delta);
                                ((Temperaturec) building).addTemperature(-delta);
                            }
                        }
                    }
                }
            }
            ((Temperaturec) building).addTemperature(core.Vars.temperatureRegressionPerSecond / -60f);
        }
    }
}
