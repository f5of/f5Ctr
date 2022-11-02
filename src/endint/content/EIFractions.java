package endint.content;

import arc.graphics.Color;
import endint.game.Fraction;
import mindustry.game.Team;

public class EIFractions {
    public static Fraction fallen;

    public static void load(){
        fallen = new Fraction(0, "fallen", Color.gray.cpy(), () -> FallenTechTree.root);
    }

    public static Fraction getFraction(Team team){
        Fraction[] fractions = new Fraction[]{fallen};
        for (Fraction fraction : fractions) {
            if (fraction.team == team) return fraction;
        }
        return null;
    }
}
