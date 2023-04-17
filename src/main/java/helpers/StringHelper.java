package helpers;

public class StringHelper {

    private static final String WHITE = "[#ffffff]";
    private static final String RED = "[#ff0000]";
    private static final String ORANGE = "[#ff8800]";
    private static final String GREEN = "[#00ff00]";
    private static final String BLUE = "[#0000ff]";
    private static final String VIOLET = "[#7f00ff]";

    public static String redString(String s) {
        return RED + s + WHITE;
    }

    public static String greenString(String s) {
        return GREEN + s + WHITE;
    }

    public static String orangeString(String s) {
        return ORANGE + s + WHITE;
    }

    public static String blueString(String s) {
        return BLUE + s + WHITE;
    }

    public static String violetString(String s) {
        return VIOLET + s + WHITE;
    }
}
