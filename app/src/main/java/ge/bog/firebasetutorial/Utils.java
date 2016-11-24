package ge.bog.firebasetutorial;


import android.util.Log;
import android.widget.Toast;

/**
 * Created by alex on 11/24/2016
 */

public class Utils {

    public static void log(Object o) {
        Log.d("FireLog", o + "");
    }

    public static void showToast(String toast) {
        Toast.makeText(App.getInstance(), toast, Toast.LENGTH_SHORT).show();
    }
}
