package com.edu.itu.smellsliketeamspirit;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HandlerService extends Service {
    Object lock = new Object();
    Socket socket;
    int port;
    String ip;
    DataOutputStream out;
    public static Handler mServiceHandler = null;
    Data data;
    NetworkThread thread;
    public HandlerService() {
        mServiceHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                synchronized (lock) {
                    data = (Data) msg.obj;
                    lock.notify();
                }
                Log.d("SERVICE", data.joystick + " " + data.angle + " " + data.power);
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle args = intent.getExtras();
        ip = args.getString("ip");
        port = args.getInt("port");
        thread = new NetworkThread();
        thread.start();
        return START_NOT_STICKY;
    }

    class NetworkThread extends Thread {

        public NetworkThread() {

        }

        @Override
        public void run() {
            Log.d("NetworkThread","Started");
            try {
                socket = new Socket(ip, port);
                if (socket.isConnected()) {
                    if(ControlActivity.handler != null)
                        ControlActivity.handler.sendMessage(null);
                    Log.d("Socket", "Connected");
                    out = new DataOutputStream(socket.getOutputStream());
                    while (!socket.isClosed()) {
                        synchronized (lock) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            try {
                                if (out != null) {
                                    out.writeByte(data.joystick);
                                    out.writeDouble(data.angle);
                                    out.writeDouble(data.power);
                                    out.flush();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            } catch (IOException e) {
                Log.d("Socket", "Can't connect");
            }
        }
    }

}
