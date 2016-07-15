package thsst.calvis.editor.model;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;

import java.util.ArrayList;
import java.util.Set;
import java.util.prefs.Preferences;

/**
 * Created by Jennica on 10/07/2016.
 */
public class Utility {

    public static boolean safeEquals(Object o1, Object o2) {
        if (o1 == o2)
            return true;
        if (o1 == null || o2 == null)
            return false;
        return o1.equals(o2);
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String ltrim(String s) {
        int i = 0;
        while (i < s.length() && Character.isWhitespace(s.charAt(i)))
            i++;
        return s.substring(i);
    }

    public static String rtrim(String s) {
        int i = s.length() - 1;
        while (i >= 0 && Character.isWhitespace(s.charAt(i)))
            i--;
        return s.substring(0, i + 1);
    }

    public static void putPrefs(Preferences prefs, String key, String value, String def) {
        if (value != def && !value.equals(def))
            prefs.put(key, value);
        else
            prefs.remove(key);
    }

    public static void putPrefsInt(Preferences prefs, String key, int value, int def) {
        if (value != def)
            prefs.putInt(key, value);
        else
            prefs.remove(key);
    }

    public static void putPrefsBoolean(Preferences prefs, String key, boolean value, boolean def) {
        if (value != def)
            prefs.putBoolean(key, value);
        else
            prefs.remove(key);
    }

    public static String[] getPrefsStrings(Preferences prefs, String key) {
        ArrayList<String> arr = new ArrayList<String>();
        for (int i = 0; i < 10000; i++) {
            String s = prefs.get(key + (i + 1), null);
            if (s == null)
                break;
            arr.add(s);
        }
        return arr.toArray(new String[arr.size()]);
    }

    public static void putPrefsStrings(Preferences prefs, String key, String[] strings) {
        for (int i = 0; i < strings.length; i++)
            prefs.put(key + (i + 1), strings[i]);

        for (int i = strings.length; prefs.get(key + (i + 1), null) != null; i++)
            prefs.remove(key + (i + 1));
    }

    public static ScrollBar findVScrollBar(Node node) {
        Set<Node> scrollBars = node.lookupAll(".scroll-bar");
        for (Node scrollBar : scrollBars) {
            if (scrollBar instanceof ScrollBar &&
                    ((ScrollBar)scrollBar).getOrientation() == Orientation.VERTICAL)
                return (ScrollBar) scrollBar;
        }
        return null;
    }
}
