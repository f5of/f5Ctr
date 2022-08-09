package endint.math;

public class ModMath {
    private static final String point = String.valueOf(1f/2f).substring(1, 2);

    public static float fixFloat(float value, int max){
        String temp = String.valueOf(value);
        return Float.parseFloat(temp.lastIndexOf(point) + max < temp.length() ? temp.substring(0, temp.lastIndexOf(point) + max + 1) : temp);
    }
}
