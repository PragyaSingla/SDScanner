package pragya.in.smartscanner.ui;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pragya.in.smartscanner.App;
import pragya.in.smartscanner.R;
import pragya.in.smartscanner.adapter.ExtensionListAdapter;
import pragya.in.smartscanner.adapter.FileListAdapter;
import pragya.in.smartscanner.model.ExtInfo;
import pragya.in.smartscanner.model.FileInfo;
import pragya.in.smartscanner.model.SDInfo;
import pragya.in.smartscanner.service.CustomAsyncTask;
import pragya.in.smartscanner.service.FileScannerTask;
import pragya.in.smartscanner.utils.FileUtils;
import pragya.in.smartscanner.utils.Utils;


/**
 * Created by Pragya on 02-05-2018.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;
    public static final String FINISHED_PARAM_NAME = "FINISHED";
    public static final String STARTED_PARAM_NAME = "STARTED";

    @BindView(R.id.btnStart)
    Button buttonStart;

    @BindView(R.id.btnShare)
    TextView buttonShare;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.layoutProgress)
    LinearLayout layoutProgress;

    @BindView(R.id.textViewProgress)
    TextView textViewProgress;

    @BindView(R.id.textAvgFileSize)
    TextView textAvgFileSize;

    @BindView(R.id.textMessage)
    TextView textMessage;

    @BindView(R.id.layoutStartUp)
    LinearLayout layoutStartUp;

    private FileScannerTask fileScannerTask;
    private SDInfo sdInfo;
    private FileListAdapter mAdapter;
    private ExtensionListAdapter mAdapterExtensions;
    private boolean isFinished = false;
    private boolean isStarted = false;
    private int selectedTabPosition = 0;
    private NotificationManager mNotificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        buttonShare.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //checkPermissionsAndScanStorage();

        setUpListener();

    }

    private void setUpListener() {

        buttonStart.setOnClickListener(this);
        buttonShare.setOnClickListener(this);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTabPosition = tab.getPosition();
                if (tab.getPosition() == 0) {
                    textAvgFileSize.setVisibility(View.VISIBLE);
                } else if (tab.getPosition() == 1) {
                    textAvgFileSize.setVisibility(View.GONE);

                }

                if (sdInfo != null) {
                    updateContent();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setAdapter(int selectedTabPosition) {
        if (selectedTabPosition == 0) {
            recyclerView.setAdapter(getFileListAdapter());

        } else if (selectedTabPosition == 1) {
            recyclerView.setAdapter(getExtensionListAdapter());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void checkPermissionsAndScanStorage() {
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Utils.checkPermissionsGranted(this, permissions)) {
            processScanner();
        } else {
            updateScanButton();
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }
    }

    private void scanStorage() {
        if (Utils.isExternalStorageReadable()) {
            updateScanButton();
            showNotification(getString(R.string.notification_text));
            fileScannerTask = new FileScannerTask(this, layoutProgress);
            fileScannerTask.setAsynkTaskListener(customTaskAsyncListener);
            fileScannerTask.execute(Environment.getExternalStorageDirectory().getAbsolutePath());
        } else {
            Toast.makeText(MainActivity.this, R.string.external_storage_not_read_ready, Toast.LENGTH_LONG).show();
        }
    }


    private void showNotification(String title) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(notifyPendingIntent);
        mNotificationManager.notify(0, mBuilder.build());


    }

    private void updateScanButton() {
        if (this.isStarted) {
            layoutProgress.setVisibility(View.VISIBLE);
            if (mAdapter != null) {
                mAdapter.setItems(new ArrayList<FileInfo>());
            }
            if (mAdapterExtensions != null) {
                mAdapterExtensions.setItems(new ArrayList<ExtInfo>());
            }
            textAvgFileSize.setText("");
            buttonStart.setText(R.string.stop);
            textMessage.setText("");
            sdInfo = null;

            recyclerView.setVisibility(View.VISIBLE);
        } else {
            layoutProgress.setVisibility(View.GONE);
            buttonStart.setText(R.string.start);
            textMessage.setText(getString(R.string.rescan_message));
        }
        buttonShare.setEnabled(isFinished);
        if (isFinished) {
            buttonShare.getBackground().setColorFilter(null);
            textMessage.setText("");
        } else {
            buttonShare.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }
    }


    private RecyclerView.Adapter getFileListAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (mAdapter == null) {
            mAdapter = new FileListAdapter(sdInfo.getFileInfoList(), Utils.getTotalExternalStorageSpace());
        } else {
            mAdapter.setItems(sdInfo.getFileInfoList());
            mAdapter.notifyDataSetChanged();
        }

        return mAdapter;
    }


    private RecyclerView.Adapter getExtensionListAdapter() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        if (mAdapterExtensions == null) {
            mAdapterExtensions = new ExtensionListAdapter(sdInfo.getExtInfoList());
        } else {
            mAdapterExtensions.setItems(sdInfo.getExtInfoList());
            mAdapterExtensions.notifyDataSetChanged();
        }

        return mAdapterExtensions;
    }

    private void updateContent() {
        setAdapter(selectedTabPosition);

        textAvgFileSize.setText("Average File Size: " + FileUtils.getFileSize(sdInfo.getAvgFileSize()));

        this.isStarted = false;
        this.isFinished = true;
        updateScanButton();
    }

    @Override
    public void onClick(View view) {
        if (view == buttonStart) {
            checkPermissionsAndScanStorage();
            updateScanButton();
            layoutStartUp.setVisibility(View.GONE);
        } else if (view == buttonShare) {
            share();
        }
    }

    private void processScanner() {
        if (this.isStarted) {
            this.isStarted = false;
            this.isFinished = false;
            stopScan(getString(R.string.scanning_stopped));
            return;
        }
        this.isStarted = true;
        this.isFinished = false;
        scanStorage();
    }

    private void stopScan(String notificationMessage) {
        if (this.fileScannerTask != null && this.fileScannerTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            this.fileScannerTask.cancel(true);
        }
        showNotification(notificationMessage);
    }

    private void share() {
        Intent sharingIntent = new Intent("android.intent.action.SEND");
        sharingIntent.setType("text/plain");
        String shareBody = this.sdInfo.toString();
        sharingIntent.putExtra("android.intent.extra.SUBJECT", "Result of scan SD Card");
        sharingIntent.putExtra("android.intent.extra.TEXT", shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Choose application to share"));
    }

    private CustomAsyncTask.AsynkTaskListener customTaskAsyncListener = new CustomAsyncTask.AsynkTaskListener() {
        @Override
        public void onTaskPeformed(AsyncTask<?, ?, ?> asyncTask) {
            App.getApp().setSdInfo(((FileScannerTask) asyncTask).getSDInfo());
            sdInfo = App.getApp().getSdInfo();
            if (sdInfo != null) {
                updateContent();
                showNotification(getString(R.string.scan_completed));
            }
        }
    };

    @Override
    public void onBackPressed() {
        stopScan(getString(R.string.scanning_interupped));
        super.onBackPressed();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        checkPermissionsAndScanStorage();
                    }

                } else {

                    textMessage.setText(getString(R.string.permission_denied));
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    @Override
    protected void onStop() {

        showNotification(getString(R.string.scanning_interupped));
        if (mNotificationManager != null)
            mNotificationManager.cancel(0);
        super.onStop();
    }
}
