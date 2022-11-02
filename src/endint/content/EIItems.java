package endint.content;

import arc.graphics.*;
import mindustry.type.*;

public class EIItems {
    public static Item stone, iron;

    public static void load(){
        iron = new Item("iron", Color.gray.cpy().mul(0.85f)){{
            hardness = 1;
            cost = 0.8f;
            alwaysUnlocked = true;
        }};
        stone = new Item("stone", Color.gray.cpy()){{
            hardness = 1;
            cost = 0.3f;
            alwaysUnlocked = true;
        }};
    }
}
