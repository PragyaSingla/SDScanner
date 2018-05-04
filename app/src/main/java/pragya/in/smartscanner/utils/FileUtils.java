package pragya.in.smartscanner.utils;

import java.text.DecimalFormat;

/**
 * Created by Pragya on 02-05-2018.
 */
public class FileUtils {
    public static String getFileExt(String fileName) {
        String ext = "";
        int dotPos = fileName.lastIndexOf(".");
        if (dotPos != -1) {
            return fileName.substring(dotPos + 1);
        }
        return ext;
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
