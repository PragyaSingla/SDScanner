package pragya.in.smartscanner.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pragya.in.smartscanner.model.ExtInfo;
import pragya.in.smartscanner.model.FileInfo;

/**
 * Created by Pragya on 02-05-2018.
 */
public class SDHelper {
    public static List<FileInfo> getFirstFileInfos(List<FileInfo> fileInfoList, int count) {
        if (fileInfoList == null) {
            throw new IllegalArgumentException("fileInfoList is null");
        } else if (count < 0) {
            throw new IllegalArgumentException("fileCount must be more then zero");
        } else {
            List<FileInfo> result = new ArrayList<>();
            Collections.sort(fileInfoList);
            for (FileInfo fileInfo : fileInfoList) {
                result.add(fileInfo);
                count--;
                if (count <= 0) {
                    break;
                }
            }
            return result;
        }
    }

    public static List<ExtInfo> getFirstFileExts(List<ExtInfo> extInfoList, int count) {
        if (extInfoList == null) {
            throw new IllegalArgumentException("extInfoList is null");
        } else if (count < 0) {
            throw new IllegalArgumentException("fileCount must be more then zero");
        } else {
            List<ExtInfo> result = new ArrayList<>();
            Collections.sort(extInfoList);
            for (ExtInfo extInfo : extInfoList) {
                result.add(extInfo);
                count--;
                if (count <= 0) {
                    break;
                }
            }
            return result;
        }
    }
}
