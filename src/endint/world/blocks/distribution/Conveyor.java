package endint.world.blocks.distribution;

import endint.game.Fraction;
import endint.world.Fractionc;
import mindustry.Vars;

public class Conveyor extends mindustry.world.blocks.distribution.Conveyor implements Fractionc {
    public final Fraction fraction;

    public Conveyor(String name, Fraction f) {
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

    public class ConveyorBuild extends mindustry.world.blocks.distribution.Conveyor.ConveyorBuild {

    }
}
