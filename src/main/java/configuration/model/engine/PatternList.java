package configuration.model.engine;

/**
 * Created by Goodwin Chua on 16/03/2016.
 */
public final class PatternList {

    public static String hexPattern = "\\b(0[xX][0-9a-fA-F]{1,16})\\b";
    public static String decPattern = "\\b(\\d+)\\b";
    public static String floatingPointPattern = "[-+]?[0-9]*\\.[0-9]+([eE][-+]?[0-9]+)?";
    public static String commentPattern = "(;.*)";
    public static String labelPattern = "[a-zA-Z_][a-zA-Z\\d_]*";
    public static String stringLiteralPattern = "\"([^\"\\\\]|\\\\.)*\"";

    private PatternList() {

    }

}
