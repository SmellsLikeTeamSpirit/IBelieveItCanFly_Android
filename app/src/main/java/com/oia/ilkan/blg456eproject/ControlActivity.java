package com.oia.ilkan.blg456eproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.erz.joysticklibrary.JoyStick;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class ControlActivity extends AppCompatActivity implements JoyStick.JoyStickListener {
    JoyStick joyStick1, joyStick2;
    int port;
    String ip;
    Data data;
    MyThread thread;
    Object lock = new Object();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        Bundle args = getIntent().getExtras();

        port = args.getInt("port");
        ip = args.getString("ip");

        joyStick1 = (JoyStick) findViewById(R.id.joy1);
        joyStick2 = (JoyStick) findViewById(R.id.joy2);
        data = new Data();

        thread = new MyThread();
        thread.start();

        //Set JoyStickListener
        joyStick1.setListener(this);
        joyStick2.setListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(thread.isAlive()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMove(JoyStick joyStick, double angle, double power) {
        double angleToSend;
        if (angle >= -Math.PI / 2 && angle <= Math.PI) {
            angleToSend = Math.PI / 2 - angle;
        } else {
            angleToSend = -((3 * Math.PI) / 2) - angle;
        }
        synchronized (lock) {
            data.angle = angleToSend;
            data.power = power;
            data.joystick = joyStick == joyStick1 ? 1 : 2;
            lock.notify();
        }
        Log.d("Joystick On Move", "Power : " + power + " Angle : " + angle + " Angle To Send : " + angleToSend);
    }

    class MyThread extends Thread {
        Socket socket;
        DataOutputStream out;
        public MyThread() {

        }

        @Override
        public void run() {
            Log.d("run","worked");
            try {
                this.socket = new Socket(ip, port);
                Log.d("socket","created");
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            while(true) {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        out.writeDouble(data.power);
                        out.writeDouble(data.angle);
                        out.writeInt(data.joystick);
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

