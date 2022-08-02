package content;

import arc.graphics.Color;
import mindustry.type.Item;

public class ModItems {
    public static Item salt, goldPowder, aluminium, crinite, dalcite, ice, carbon;

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

        goldPowder = new Item("gold-powder"){{
            explosiveness = 0f;
            flammability = 0f;
            radioactivity = 0f;
            charge = 0f;
            hardness = 10;
            cost = 0.6f;
            color = Color.valueOf("ffd700");
        }};

        aluminium = new Item("aluminium"){{
            explosiveness = 0f;
            flammability = 0f;
            radioactivity = 0f;
            charge = 0f;
            hardness = 2;
            cost = 0.5f;
            color = Color.valueOf("848789");
        }};

        crinite = new Item("crinite"){{
            explosiveness = 0f;
            flammability = 0f;
            radioactivity = 0f;
            charge = 0f;
            hardness = 0;
            cost = 0.8f;
            color = Color.valueOf("cc2233");
        }};

        dalcite = new Item("dalcite"){{
            explosiveness = 0.2f;
            flammability = 0.9f;
            radioactivity = 0f;
            charge = 0f;
            hardness = 0;
            cost = 0.2f;
            color = Color.valueOf("666666");
        }};

        ice = new Item("ice"){{
            explosiveness = 0f;
            flammability = 0f;
            radioactivity = 0f;
            charge = 0f;
            hardness = 0;
            cost = 0.1f;
            color = Color.valueOf("8888ee");
        }};

        carbon = new Item("carbon"){{
            explosiveness = 0f;
            flammability = 0f;
            radioactivity = 0f;
            charge = 0f;
            hardness = 3;
            cost = 0.5f;
            color = Color.valueOf("222222");
        }};
    }
}
