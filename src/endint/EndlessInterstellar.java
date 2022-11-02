package endint;

import endint.gen.EIMusics;
import endint.gen.EISounds;
import endint.gen.EITex;
import mindustry.Vars;
import mindustry.ctype.Content;
import mindustry.world.Block;
import mma.MMAMod;
import mma.ModVars;
import mma.annotations.ModAnnotations.MainClass;

import static endint.EndlessInterstellarVars.*;


@MainClass()
public class EndlessInterstellar extends MMAMod {
    public EndlessInterstellar() {
        super();
        EndlessInterstellarVars.load();
    }

    @Override
    protected void modContent(Content content) {
        super.modContent(content);
    }

    @Override
    protected void created(Content content){
        super.created(content);
    }

    public void init() {
        ui.init();
        if (!loaded)
            return;
        EITex.load();
        super.init();
        if (neededInit)
            listener.init();
    }

    /**
     * All content types should be loaded into loadContent from {@link EndlessInterstellarVars}
     * There is you can load extra things like ModMusic or ModSounds etc.
     */
    public void loadContent() {
        if (!Vars.headless){
            ModVars.inTry(EIMusics::load);
            ModVars.inTry(EISounds::load);
        }
        super.loadContent();
    }
}
