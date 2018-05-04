package pragya.in.smartscanner.service;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import pragya.in.smartscanner.model.SDInfo;

/**
 * Created by Pragya on 02-05-2018.
 */
public class FileScannerTask extends CustomAsyncTask<String, Integer, SDInfo> {
    private int extCount = 5;
    private int fileCount = 10;
    private SDInfo sdInfo;

    public FileScannerTask(Context mContext, LinearLayout progressBar) {
        super(mContext, progressBar);
    }

    @Override
    protected SDInfo doInBackground(String... params) {
        if (isCancelled()) {
            return null;
        }
        FileScanner fileScanner = new FileScanner(this);
        fileScanner.processScan(params[0]);
        this.sdInfo = new SDInfo(SDHelper.getFirstFileInfos(fileScanner.getFileInfoList(),
                this.fileCount), SDHelper.getFirstFileExts(fileScanner.getExtInfoList(),
                this.extCount), fileScanner.getAvgFileSise());
        return this.sdInfo;
    }

    public void updateProgress(int progress) {
        publishProgress(progress);
    }

    public SDInfo getSDInfo() {
        return this.sdInfo;
    }
}
