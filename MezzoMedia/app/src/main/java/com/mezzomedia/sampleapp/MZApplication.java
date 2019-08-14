package com.mezzomedia.sampleapp;

import android.app.Application;
import android.os.Handler;
import android.os.StrictMode;

public class MZApplication extends Application {
    @Override
    public void onCreate() {
//        new Handler().postAtFrontOfQueue(new Runnable() {
//
//            public void run() {
//				StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//	                    .detectAll()
//	                    .penaltyLog()
//	                    .penaltyDropBox()
//	                    .penaltyFlashScreen()
//	                    .build());
//	            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//	                    .detectAll()
//	                    .penaltyLog()
//	                    .penaltyDropBox()
//	                    //                    .penaltyDeath()
//	                    .build());
//            }
//        });
        super.onCreate();
    }
}
