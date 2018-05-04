package pragya.in.smartscanner.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pragya.in.smartscanner.model.ExtInfo;
import pragya.in.smartscanner.model.FileInfo;
import pragya.in.smartscanner.utils.FileUtils;

/**
 * Created by Pragya on 02-05-2018.
 */

public class FileScanner {

    FileScannerTask fileScannerTask;

    public FileScanner(FileScannerTask fileScannerTask) {
        this.fileScannerTask = fileScannerTask;
    }


    public FileScanner() {
    }

    private final List<ExtInfo> extInfoList = new ArrayList<>();
    private Map<String, Integer> fileExts = new HashMap<>();
    private final List<FileInfo> fileInfoList = new ArrayList<>();
    private int fileCount = 0;

    public void processScan(String path) {
        clear();
        scanFiles(path);
        fillInfo();
    }

    private void clear() {
        fileCount = 0;
        this.fileInfoList.clear();
        this.extInfoList.clear();
        this.fileExts.clear();
    }

    private void updateProgress() {
        this.fileCount++;
        fileScannerTask.updateProgress(fileCount);
    }


    private void scanFiles(String path) {
        File[] files = new File(path).listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (fileScannerTask.isCancelled()) {
                    break;
                }

                try {
                    Thread.sleep(1);
                    updateProgress();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (file.isDirectory()) {
                    scanFiles(file.getPath());
                }

                String fileName = file.getName();
                this.fileInfoList.add(new FileInfo(fileName, file.length()));
                String fileExt = FileUtils.getFileExt(fileName);
                if (this.fileExts.containsKey(fileExt)) {
                    this.fileExts.put(fileExt, this.fileExts.get(fileExt) + 1);
                } else {
                    this.fileExts.put(fileExt, 1);
                }
            }
        }
    }

    int lengthCount = 0;

    public int getAllFilesLength(String path) {

        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                lengthCount++;
                File file = files[i];

                if (file.isDirectory()) {
                    getAllFilesLength(file.getAbsolutePath());
                }
            }
            return lengthCount;
        }

        return 0;
    }

    private void fillInfo() {
        for (String fileExt : this.fileExts.keySet()) {
            this.extInfoList.add(new ExtInfo(fileExt, this.fileExts.get(fileExt)));
        }
    }

    public List<FileInfo> getFileInfoList() {
        return this.fileInfoList;
    }

    public List<ExtInfo> getExtInfoList() {
        return this.extInfoList;
    }

    public long getAvgFileSise() {
        long avgFileSise = 0;
        if (this.fileInfoList == null || this.fileInfoList.size() <= 0) {
            return 0;
        }
        for (FileInfo fileInfo : this.fileInfoList) {
            avgFileSise += fileInfo.getSize();
        }
        return avgFileSise / ((long) this.fileInfoList.size());
    }
}
