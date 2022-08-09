package endint;

import mindustry.ctype.*;
import mindustry.world.blocks.power.PowerGraph;
import mma.*;
import mma.annotations.ModAnnotations.*;

import java.lang.reflect.Field;

import static endint.EndlessInterstellarVars.*;

@MainClass
public class EndlessInterstellar extends MMAMod {

    public EndlessInterstellar() {
        super();
        EndlessInterstellarVars.load();
    }

    @Override
    protected void modContent(Content content) {
        super.modContent(content);
    }

    public void init() {
        if (!loaded)
            return;
        super.init();
        if (neededInit)
            listener.init();
    }

    /**
     * All content types should be loaded into loadContent from {@link EndlessInterstellarVars}
     * There is you can load extra things like ModMusic or ModSounds etc.
     */
    public void loadContent() {
        super.loadContent();
    }
}
