package pragya.in.smartscanner;

import android.app.Application;

import java.lang.ref.WeakReference;

import pragya.in.smartscanner.model.SDInfo;

/**
 * Created by Pragya on 02-05-2018.
 */
public class App extends Application {
    private SDInfo sdInfo;

    public SDInfo getSdInfo() {
        return this.sdInfo;
    }

    private static WeakReference<App> weakReference;

    @Override
    public void onCreate() {
        super.onCreate();
        weakReference = new WeakReference<App>(this);
    }

    public void setSdInfo(SDInfo sdInfo) {
        this.sdInfo = sdInfo;
    }

    public static App getApp() {
        return weakReference.get();
    }
}
