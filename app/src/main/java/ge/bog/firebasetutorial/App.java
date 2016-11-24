package ge.bog.firebasetutorial;

import android.app.Application;

/**
 * Created by alex on 11/24/2016
 */

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
