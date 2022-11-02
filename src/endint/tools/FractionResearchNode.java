package endint.tools;

import arc.util.Log;
import arc.util.io.Reads;
import arc.util.io.Writes;
import endint.game.Fraction;
import mindustry.content.TechTree;
import mindustry.ctype.UnlockableContent;
import mindustry.type.ItemStack;

import java.lang.reflect.Field;

public class FractionResearchNode {
    public TechTree.TechNode node;
    public Fraction fraction;

    static Field uF;
    static {
        try {
            uF = UnlockableContent.class.getDeclaredField("unlocked");
            uF.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FractionResearchNode(TechTree.TechNode n, Fraction f) {
        node = n;
        fraction = f;
        lock();
        for (int i = 0; i < node.finishedRequirements.length; i++) {
            ItemStack stack = node.finishedRequirements[i];
            stack.amount = 0;
        }
    }

    public void save(Writes write){
        for (int i = 0; i < node.finishedRequirements.length; i++) {
            ItemStack stack = node.finishedRequirements[i];
            write.i(stack.amount);
            write.bool(node.content.unlocked());
        }
    }

    public void load(Reads read){
        for (int i = 0; i < node.finishedRequirements.length; i++) {
            ItemStack stack = node.finishedRequirements[i];
            stack.amount = read.i();
            boolean unlocked = read.bool();
            if (unlocked) {
                unlock();
            }
        }
    }

    public void lock(){
        try {
            uF.set(node.content, false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void unlock(){
        try {
            uF.set(node.content, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return node.content.toString() + " of fraction " + fraction.toString();
    }
}
