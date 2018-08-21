package com.reactlibrary.RNOfflineUploader;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploaderJob extends JobService {

    public static final int DEADLINE_FIRST_TIME = 1;
    public static final int DEADLINE_RETRY = 30;
    public static final int JOB_ID_UPLOAD = 0;
    private static final String TAG = "ReactNative";

    private BroadcastReceiver mUploaderModuleReceiver;
    private JobParameters mParams;
    private Call mCall;

    public static void scheduleJob(Context context, int deadline, String url, String path) {
        PersistableBundle extras = new PersistableBundle();
        extras.putString("url", url);
        extras.putString("path", path);

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID_UPLOAD, new ComponentName(context, UploaderJob.class))
                .setExtras(extras)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setMinimumLatency(Math.max(1, deadline/2) * 1000)
                .setOverrideDeadline(deadline * 1000);

        Log.d(TAG, "=> schedule job " + JOB_ID_UPLOAD + " pour dans " + deadline + "s");

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    @Override
    public void onCreate () {
        super.onCreate();

        mUploaderModuleReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(mCall != null) {
                    mCall.cancel();
                }
            }
        };

        LocalBroadcastManager
                .getInstance(getApplicationContext())
                .registerReceiver(mUploaderModuleReceiver, new IntentFilter("UploaderModuleBroadcast"));
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "=> start job " + params.getJobId());

        mParams = params;

        PersistableBundle extras = params.getExtras();
        final String url = extras.getString("url");
        final String path = extras.getString("path");

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(path, Context.MODE_PRIVATE);
        String json = sharedPref.getString("json", null);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        mCall = client.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "=> onResponse");
                jobFinishing();
                UploaderJob.this.sendResponse(response.body().string());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "=> onFailure : " + e.toString());
                jobFinishing();
                if(!mCall.isCanceled()) {
                    UploaderJob.scheduleJob(getApplicationContext(), UploaderJob.DEADLINE_RETRY, url, path);
                }
            }
        });

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "=> stop job " + params.getJobId());
        return false;
    }

    private void jobFinishing() {
        LocalBroadcastManager
                .getInstance(getApplicationContext())
                .unregisterReceiver(mUploaderModuleReceiver);

        jobFinished(mParams, false);
    }

    private void sendResponse(String response) {
        Intent intent = new Intent("UploaderJobBroadcast");
        intent.putExtra("response", response);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}