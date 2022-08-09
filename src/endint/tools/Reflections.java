package endint.tools;

import mindustry.world.blocks.power.PowerGraph;

import java.lang.reflect.Field;

public class Reflections {
    public static boolean dePrivate(Class<?> type, String name){
        try {
            Field field = type.getDeclaredField(name);
            field.setAccessible(true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static <T> T getValue(Object type, String name){
        try {
            Field field = type.getClass().getField(name);
            return (T) field.get(type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
