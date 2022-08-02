package content;

import arc.graphics.Color;
import mindustry.content.Liquids;
import mindustry.content.StatusEffects;
import mindustry.type.Liquid;

public class ModLiquids {
    public static Liquid fireCompound;

    public static void load(){
        fireCompound = new Liquid("fire-compound", Color.valueOf("aa4411")){{
            viscosity = 0.9f;
            flammability = 1.8f;
            explosiveness = 1.8f;
            heatCapacity = 0.9f;
            barColor = Color.valueOf("aa4411");
            effect = StatusEffects.tarred;
            boilPoint = 0.7f;
            gasColor = Color.grays(0.2f);
        }};
    }
}
