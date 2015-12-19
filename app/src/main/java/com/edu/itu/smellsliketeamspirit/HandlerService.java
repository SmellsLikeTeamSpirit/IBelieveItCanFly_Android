package com.edu.itu.smellsliketeamspirit;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class HandlerService extends Service {

    //used for getting the handler from other class for sending messages
    public static Handler mServiceHandler = null;
    //used for keep track on Android running status
    public static Boolean mIsServiceRunning = false;

    public HandlerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    class MyThread extends Thread {

        public MyThread() {

        }

        @Override
        public void run() {

        }
    }

}
