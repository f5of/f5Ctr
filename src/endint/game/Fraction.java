package endint.game;

import arc.func.Prov;
import arc.graphics.Color;
import mindustry.content.TechTree;
import mindustry.game.Team;

import java.lang.reflect.Constructor;

public class Fraction {
    public Team team;
    public Prov<TechTree.TechNode> root;

    public Fraction(int id, String n, Color color, Prov<TechTree.TechNode> r){
        try {
            Constructor<Team> c = Team.class.getDeclaredConstructor(int.class, String.class, Color.class);
            c.setAccessible(true);
            team = c.newInstance(id, n, color);
            root = r;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return team.name;
    }
}
