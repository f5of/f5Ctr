package endint.world.blocks.core;

import endint.game.Fraction;
import endint.world.Fractionc;
import mindustry.Vars;
import mindustry.world.blocks.storage.CoreBlock;

public class Core extends CoreBlock implements Fractionc {
    public final Fraction fraction;

    public Core(String name, Fraction f) {
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

    public class CoreBuild extends CoreBlock.CoreBuild {

    }
}
