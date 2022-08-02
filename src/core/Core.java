package core;

import content.*;
import mindustry.mod.Mod;

public class Core extends Mod {
    public Core(){

    }

    @Override
    public void loadContent() {
        ModItems.load();
        ModLiquids.load();
        ModRecipes.load();
        ModBlocks.load();
        ModPlanets.load();
    }
}
