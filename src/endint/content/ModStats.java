package endint.content;

import mindustry.world.meta.*;

public class ModStats{
    public static final StatUnit temperatureUnit = new StatUnit("temperature");

    public static final StatCat temperatureCat = new StatCat("temperature");

    public static final Stat maxwt = new Stat("maxwt", temperatureCat), minwt = new Stat("minwt", temperatureCat);
}
