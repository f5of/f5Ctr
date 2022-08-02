package endint;

import endint.content.*;
import mindustry.mod.Mod;
import mma.annotations.ModAnnotations.*;

@MainClass
public class EndlessInterstellar extends Mod {
    public EndlessInterstellar(){

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
