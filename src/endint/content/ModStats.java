package endint.content;

import mindustry.world.meta.*;

public class ModStats{
    public static final StatUnit
            temperatureUnit = new StatUnit("temperature-unit"),
            waterUnit = new StatUnit("water-unit"),
            itemUnit = new StatUnit("item-unit");

    public static final StatCat
            temperature = new StatCat("temperature");

    public static final Stat
            tempProduct = new Stat("tempProduct", temperature);
}
