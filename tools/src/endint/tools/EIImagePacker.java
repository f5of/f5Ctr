package endint.tools;

import arc.files.*;
import arc.util.serialization.*;
import arc.util.serialization.Jval.*;
import mindustry.ctype.*;
import mindustry.mod.Mods.*;
import mma.tools.*;
import endint.*;

public class EIImagePacker extends ModImagePacker{

    public EIImagePacker(){
    }

    public static void main(String[] args) throws Exception{
        new EIImagePacker();
    }

    @Override
    protected ModMeta getModMeta(){
        Json json = new Json();
        //Fi.get("")="resources/assets-raw/sprites_out/generated/"
        //go up to "resources/"
        Fi resourcesFolder = Fi.get("../../../");
        Fi metaf = resourcesFolder.child("mod.hjson");
        if(!metaf.exists()){
            metaf = resourcesFolder.child("mod.json");
        }
        return json.fromJson(ModMeta.class, Jval.read(metaf.readString()).toString(Jformat.plain));
    }

    @Override
    protected void start() throws Exception{
        EndlessInterstellarVars.create();

        super.start();
    }

    @Override
    protected void preCreatingContent(){
        super.preCreatingContent();

//        YourPrefixEntityMapping.init();
    }

    @Override
    protected void runGenerators(){
        new EIGenerators();
    }

    @Override
    protected void checkContent(Content content){
        super.checkContent(content);
        /*
        * if you use Load annotation in any class that extends MappableContent,
        * ZelauxModCore will generate YOUR_PREFIXContentRegions class and here you can apply that on instances of those classes
        *
        if(content instanceof MappableContent){
            YOUR_PREFIXContentRegions.loadRegions((MappableContent)content);
        }
        */
    }

}
