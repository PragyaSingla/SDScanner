package pragya.in.smartscanner.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.ContextCompat;

/**
 * Created by Pragya on 02-05-2018.
 */
public class Utils {

    /**
     * Android M and above only supports Runtime permissions and since
     * Manifest.permission.READ_EXTERNAL_STORAGE falls in dangerous permission
     * we need to check at runtime if it has been granted before before using it.
     * http://developer.android.com/reference/android/Manifest.permission.html#READ_EXTERNAL_STORAGE
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean checkPermissionsGranted(final Context context, String[] permissions) {
        if (!isVersionMarshMallowOrAbove()) {
            return true;
        }
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static final boolean isVersionMarshMallowOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    public static long getTotalExternalStorageSpace() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getBlockCount();
        return bytesAvailable;
    }
}
