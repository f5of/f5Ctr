package core;

import content.ModBlocks;
import content.ModItems;
import content.ModPlanets;
import mindustry.mod.Mod;

public class Core extends Mod {
    public Core(){

    }

    @Override
    public void loadContent() {
        ModItems.load();
        ModBlocks.load();
        ModPlanets.load();
    }
}
