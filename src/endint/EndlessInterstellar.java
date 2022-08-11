package endint;

import endint.annotations.*;
import endint.gen.*;
import endint.type.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.world.*;
import mindustry.world.blocks.power.PowerGraph;
import mma.*;
import mma.annotations.ModAnnotations.*;

import java.lang.reflect.Field;

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
        if (content instanceof Block block){
            Temperaturec.tryAddTemperatureBar(block);
        }
    }

    public void init() {
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
