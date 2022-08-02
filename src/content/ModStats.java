package content;

import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;
import mindustry.world.meta.StatUnit;

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
