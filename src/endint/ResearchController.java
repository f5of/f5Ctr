package endint;

import arc.Events;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.io.Reads;
import arc.util.io.Writes;
import endint.content.EIFractions;
import endint.content.FallenTechTree;
import endint.game.Fraction;
import endint.tools.FractionResearchNode;
import mindustry.content.TechTree;
import mindustry.game.EventType;

import java.lang.reflect.Field;

public class ResearchController {
    public static ObjectMap<Fraction, Seq<FractionResearchNode>> tech = new ObjectMap<Fraction, Seq<FractionResearchNode>>();
    public static ObjectMap<Fraction, FractionResearchNode> researching = new ObjectMap<Fraction, FractionResearchNode>();

    public static void init(){
        try {
            for (Field declaredField : EIFractions.class.getDeclaredFields()) {
                if (declaredField.get(null) instanceof Fraction f) {
                    tech.put(f, new Seq<FractionResearchNode>());
                    Log.info("[EI] putted " + f + " in tech ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        researching = new ObjectMap<Fraction, FractionResearchNode>();
        loadChildren(FallenTechTree.root, EIFractions.fallen);
    }

    static {
        Events.on(EventType.WorldLoadEvent.class, m -> {
            init();
        });
    }

    public static void load(Reads read) {
        tech.each((k, s) -> {
            s.each(n -> {
                n.load(read);
            });
        });
    }

    public static void save(Writes write){
        tech.each((k, s) -> {
            s.each(n -> {
                n.save(write);
            });
        });
    }

    public static void loadChildren(TechTree.TechNode root, Fraction fraction){
        FractionResearchNode node = new FractionResearchNode(root, fraction);
        Log.info("[EI] putted " + node + " in tech of fraction " + fraction);
        tech.get(fraction).add(node);
        root.children.each(n -> loadChildren(n, fraction));
    }
}
