package endint.type;

import arc.math.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.world.*;

public interface Temperaturec extends Buildingc{
     static boolean tryAddTemperatureBar(Block block){
         if (block.buildType==null){
             Reflect.invoke(block,"initBuilding");
//             block.initBuilding();
         }
        Building building = block.buildType.get();
        if(!(building instanceof Temperaturec)) return false;
//        if(!(building instanceof Temperaturec)) throw new IllegalArgumentException("Building of \"" + block.name + "\" should be instanceof Temperaturec");
        block.addBar("temperature", (Building e) -> {
            Temperaturec entity = e.as();
            return new Bar(
            () -> Strings.fixed(entity.temperature(), 1),
            () -> Liquids.cryofluid.barColor(),
            () -> Mathf.clamp(Mathf.map(entity.temperature(), entity.minWorkableTemperature(), entity.maxWorkableTemperature(), 0, 1f)));
        }
        );
        return true;
    }

    float temperature();

    void addTemperature(float value);

    default int temperatureRange(){
        return block().size * 3;
    }

    default float maxWorkableTemperature(){
        return 1000f;
    }

    default float minWorkableTemperature(){
        return -200f;
    }

    default boolean canWork(){
        return minWorkableTemperature() <= temperature() &&
        temperature() <= maxWorkableTemperature();
    }
}
