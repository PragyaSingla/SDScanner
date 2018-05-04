package pragya.in.smartscanner.model;

import java.util.List;

import pragya.in.smartscanner.utils.FileUtils;

/**
 * Created by Pragya on 02-05-2018.
 */
public class SDInfo {
    private final long avgFileSize;
    private final List<ExtInfo> extInfoList;
    private final List<FileInfo> fileInfoList;

    public SDInfo(List<FileInfo> fileInfoList, List<ExtInfo> extInfoList, long avgFileSize) {
        this.fileInfoList = fileInfoList;
        this.extInfoList = extInfoList;
        this.avgFileSize = avgFileSize;
    }

    public List<FileInfo> getFileInfoList() {
        return this.fileInfoList;
    }

    public List<ExtInfo> getExtInfoList() {
        return this.extInfoList;
    }

    public long getAvgFileSize() {
        return this.avgFileSize;
    }

    public String toString() {
        StringBuffer sbResult = new StringBuffer();
        sbResult.append("\nList of 10 biggest files: \n");
        for (FileInfo fileInfo : this.fileInfoList) {
            sbResult.append(fileInfo).append("\n");
        }
        sbResult.append("\nAverage File Size: \n");
        sbResult.append(FileUtils.getFileSize(this.avgFileSize)).append("\n");
        sbResult.append("\nMost used file types: \n");
        for (ExtInfo extInfo : this.extInfoList) {
            sbResult.append(extInfo).append("\n");
        }
        return sbResult.toString();
    }
}
