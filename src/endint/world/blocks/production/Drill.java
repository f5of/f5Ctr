package endint.world.blocks.production;

import endint.game.Fraction;
import endint.world.Fractionc;
import mindustry.Vars;

public class Drill extends mindustry.world.blocks.production.Drill implements Fractionc {
    public final Fraction fraction;

    public Drill(String name, Fraction f) {
        super(name);
        fraction = f;
        drawMineItem = false;
    }

    @Override
    public Fraction getFraction() {
        return fraction;
    }

    @Override
    public boolean unlockedNow() {
        return unlocked() && Vars.player.team() == fraction.team;
    }

    public class EIDrillBuild extends DrillBuild {

    }
}
