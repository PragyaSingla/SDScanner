package pragya.in.smartscanner.model;

import pragya.in.smartscanner.utils.FileUtils;

/**
 * Created by Pragya on 02-05-2018.
 */
public class FileInfo implements Comparable<FileInfo> {
    private final String name;
    private final long size;

    public FileInfo(String name, long size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return this.name;
    }

    public long getSize() {
        return this.size;
    }

    public int compareTo(FileInfo another) {
        return (int) (another.size - this.size);
    }

    public String toString() {
        return String.format("%s - %s", this.name, FileUtils.getFileSize(size));
    }
}
