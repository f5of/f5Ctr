package endint.world.blocks.distribution;

import endint.game.Fraction;
import endint.world.Fractionc;
import mindustry.Vars;

public class Router extends mindustry.world.blocks.distribution.Router implements Fractionc {
    public final Fraction fraction;

    public Router(String name, Fraction f) {
        super(name);
        fraction = f;
    }

    @Override
    public boolean unlockedNow() {
        return unlocked() && Vars.player.team() == fraction.team;
    }

    @Override
    public Fraction getFraction() {
        return fraction;
    }

    public class RouterBuild extends mindustry.world.blocks.distribution.Router.RouterBuild {

    }
}
