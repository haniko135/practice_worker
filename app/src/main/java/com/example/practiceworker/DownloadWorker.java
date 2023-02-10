package com.example.practiceworker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

public class DownloadWorker extends Worker {
    public DownloadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String secToken = "y0_AgAAAABUVpeiAADLWwAAAADXqEoa0KX1_myOSvS6tU-k0yc2A_S4C7o";
        String url = "https://disk.yandex.ru/d/kirIe36-Zxb2Bg";

        Data data;
        Worker.Result result = null;
        try {
            URL urlDownload = new URL(url);
            HttpsURLConnection downConn = (HttpsURLConnection) urlDownload.openConnection();
            downConn.setRequestMethod("GET");
            downConn.setRequestProperty("Authorization: OAuth ", secToken);
            downConn.setRequestProperty("User-Agent","akafist_app/1.0.0");
            downConn.setRequestProperty("Connection", "keep-alive");
            downConn.setConnectTimeout(5000);
            downConn.connect();

            if(downConn.getResponseCode() == 200) {
                InputStream inputStream = downConn.getInputStream();
                String reslt;
                try {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                    String jsonText = readAll(rd);
                    JSONObject json = new JSONObject(jsonText);
                    reslt = json.toString();
                    Log.d("EEEEE", "Success");
                } finally {
                    inputStream.close();
                }
                data = new Data.Builder()
                        .putString("JSON", reslt).build();
                result = Worker.Result.success(data);
            } else {
                Log.e("EEEEE", "Error");
                data = new Data.Builder()
                        .putString("JSON", "Error").build();
                result = Worker.Result.failure(data);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        
        return result;
        
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
