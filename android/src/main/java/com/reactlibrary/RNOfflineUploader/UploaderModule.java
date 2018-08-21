package com.reactlibrary.RNOfflineUploader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class UploaderModule extends ReactContextBaseJavaModule {

    private static final String PATH = "com.reactlibrary.RNOfflineUploader." + UploaderJob.JOB_ID_UPLOAD;

    public UploaderModule(ReactApplicationContext reactContext) {
        super(reactContext);

        BroadcastReceiver uploaderJobReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String response = intent.getStringExtra("response");
                UploaderModule.this.sendEvent(response);
            }
        };

        LocalBroadcastManager
                .getInstance(reactContext)
                .registerReceiver(uploaderJobReceiver, new IntentFilter("UploaderJobBroadcast"));
    }

    @Override
    public String getName() {
        return "RNOfflineUploader";
    }

    @ReactMethod
    public void startUpload(ReadableMap options) {
        String url = options.getString("url");
        String json = options.getString("data");

        Intent intent = new Intent("UploaderModuleBroadcast");
        LocalBroadcastManager.getInstance(getReactApplicationContext()).sendBroadcast(intent);

        SharedPreferences sharedPref = getReactApplicationContext().getSharedPreferences(PATH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("json", json);
        editor.commit();

        UploaderJob.scheduleJob(getReactApplicationContext(), UploaderJob.DEADLINE_FIRST_TIME, url, PATH);
    }

    private void sendEvent(String response) {
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("RNOfflineUploader-completed", response);
    }

}

