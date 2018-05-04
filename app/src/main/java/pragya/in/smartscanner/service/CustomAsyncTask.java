package pragya.in.smartscanner.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import pragya.in.smartscanner.R;

/**
 * Created by Pragya on 02-05-2018.
 */
public abstract class CustomAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private static final String TAG = "CustomAsyncTask";
    private TextView textViewProgress;
    private AsynkTaskListener asynkTaskListener = null;
    private Context mContext;
    private ProgressBar progressBar;
    private int length;

    public interface AsynkTaskListener {
        void onTaskPeformed(AsyncTask<?, ?, ?> asyncTask);
    }

    CustomAsyncTask(Context mContext, LinearLayout layoutProgress) {
        this.mContext = mContext;
        progressBar = layoutProgress.findViewById(R.id.progressBar);
        textViewProgress = layoutProgress.findViewById(R.id.textViewProgress);
        length = new FileScanner().getAllFilesLength(Environment.getExternalStorageDirectory().getAbsolutePath());
        progressBar.setMax(length);
        progressBar.setProgress(0);
    }

    public void setAsynkTaskListener(AsynkTaskListener asynkTaskListener) {
        this.asynkTaskListener = asynkTaskListener;
    }

    public void removeAsynkTaskListener(AsynkTaskListener asynkTaskListener) {
        if (this.asynkTaskListener == asynkTaskListener) {
            this.asynkTaskListener = null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        progressBar.setVisibility(View.GONE);

        doTaskPerformed();
    }

    @Override
    protected void onProgressUpdate(Progress[] values) {
        super.onProgressUpdate(values);
        int value = (Integer) values[0];
        progressBar.setProgress(value);
        double progress = (double) value / length * 100;
        textViewProgress.setText((int) progress + "%");

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    private void doTaskPerformed() {
        if (this.asynkTaskListener != null) {
            this.asynkTaskListener.onTaskPeformed(this);
        }
    }
}
