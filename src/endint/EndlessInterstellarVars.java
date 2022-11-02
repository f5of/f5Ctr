package endint;

import arc.*;
import arc.struct.*;
import arc.util.*;
import endint.content.*;
import endint.io.FractionsResearchChunk;
import endint.ui.EIUI;
import mindustry.*;
import mindustry.game.*;
import mma.*;

public class EndlessInterstellarVars extends ModVars{
    private final static Seq<Runnable> onLoad = new Seq<>();
//    public static ModSettings settings;
//    public static ModNetClient netClient;
//    public static ModNetServer netServer;
    public static EIUI ui = new EIUI();
//    public static ModLogic logic;

    static{
        new EndlessInterstellarVars();
    }

    /**
     * Used to load this class to computer memory causing the static block work.
     */
    public static void create(){
        //none
    }

    public static void init(){
    }

    /**
     * Here you can initialize your classes as ModSettings or ModLogic and
     * add listeners to ModListener (listener variable in ModVars)
     */
    public static void load(){
        onLoad.each(Runnable::run);
        onLoad.clear();
        //settings = new ModSettings();
        //if (!headless) listener.add(modUI = new ModUI());
        //listener.add(netClient = new ModNetClient());
        //listener.add(netServer = new ModNetServer());
        //listener.add(logic = new ModLogic());
    }


    public static void modLog(String text, Object... args){
        Log.info("[@] @", modInfo == null ? "end-int" : modInfo.name, Strings.format(text, args));
    }

    @Override
    protected void onLoad(Runnable runnable){
        onLoad.add(runnable);
    }

    @Override
    /**This is where you initialize your content lists. But do not forget about correct order.
     * @note correct order:
     *  EIItems.load()
     *  ModStatusEffects.load()
     *  EILiquids.load()
     *  ModBullets.load()
     *  ModUnitTypes.load()
     *  EIBlocks.load()
     *  EIPlanets.load()
     *  ModSectorPresets.load()
     *  ModTechTree.load()
     * */
    public void loadContent(){
        EIFractions.load();
        EIItems.load();
        EILiquids.load();
        EIUnitTypes.load();
        EIRecipes.load();
        EIBlocks.load();
        FallenTechTree.load();
        ResearchController.init();
        new FractionsResearchChunk();
    }

    @Override
    protected void showException(Throwable exception){
        Log.err(exception);
        if(Vars.headless) return;
        if(modInfo == null || Vars.ui == null){
            Events.on(EventType.ClientLoadEvent.class, event -> {
                Vars.ui.showException(Strings.format("Error in @", modInfo == null ? null : modInfo.meta.displayName), exception);
            });
        }else{
            Vars.ui.showException(Strings.format("Error in @", modInfo.meta.displayName), exception);
        }
    }

    public interface ThrowableRunnable{
        void run() throws Exception;
    }
}
