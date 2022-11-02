package endint.world.blocks.power;

import endint.game.Fraction;
import endint.world.Fractionc;
import mindustry.Vars;

public class PowerNode extends mindustry.world.blocks.power.PowerNode implements Fractionc {
    public final Fraction fraction;

    public PowerNode(String name, Fraction f) {
        super(name);
        fraction = f;
    }

    @Override
    public Fraction getFraction() {
        return fraction;
    }

    @Override
    public boolean unlockedNow() {
        return unlocked() && Vars.player.team() == fraction.team;
    }

    public class PowerNodeBuild extends mindustry.world.blocks.power.PowerNode.PowerNodeBuild {

    }
}
