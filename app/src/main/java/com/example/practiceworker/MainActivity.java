package com.example.practiceworker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private PeriodicWorkRequest workRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workRequest = new PeriodicWorkRequest.Builder(DownloadWorker.class).build();
        WorkManager.getInstance(this).enqueue(workRequest);
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.getId()).observe(this, workInfo -> {
            String json = workInfo.getOutputData().getString("JSON");
            Log.d("EEEEE", json);
        });
    }
}