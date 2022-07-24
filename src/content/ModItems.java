package content;

import arc.graphics.Color;
import mindustry.type.Item;

public class ModItems {
    public static Item salt, gold, aluminium, crinite;

    public static void load(){
        salt = new Item("salt"){{
            explosiveness = 0f;
            flammability = 0f;
            radioactivity = 0f;
            charge = 0f;
            hardness = 1;
            cost = 0.2f;
            color = Color.valueOf("cccccc");
        }};

        gold = new Item("gold"){{
            explosiveness = 0f;
            flammability = 0f;
            radioactivity = 0f;
            charge = 0f;
            hardness = 10;
            cost = 0.2f;
            color = Color.valueOf("ffd700");
        }};

        aluminium = new Item("aluminium"){{
            explosiveness = 0f;
            flammability = 0f;
            radioactivity = 0f;
            charge = 0f;
            hardness = 2;
            cost = 0.2f;
            color = Color.valueOf("848789");
        }};

        crinite = new Item("crinite"){{
            explosiveness = 0f;
            flammability = 0f;
            radioactivity = 0f;
            charge = 0f;
            hardness = 0;
            cost = 0.2f;
            color = Color.valueOf("cc2233");
        }};
    }
}
