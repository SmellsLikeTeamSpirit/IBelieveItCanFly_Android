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

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class ControlActivity extends AppCompatActivity implements JoyStick.JoyStickListener {
    JoyStick joyStick1, joyStick2;
    int port;
    String ip;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        Bundle args = getIntent().getExtras();
        port = args.getInt("port");
        ip = args.getString("ip");
        joyStick1 = (JoyStick) findViewById(R.id.joy1);
        joyStick2 = (JoyStick) findViewById(R.id.joy2);

        HandlerThread handlerThread = new HandlerThread("myHandlerThread");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        // Create an instance of the class that will handle the messages that are posted
        //  to the Handler
        Worker worker = new Worker(ip, port);
        // Create a Handler and give it the worker instance to handle the messages
        handler = new Handler(looper, worker);

        joyStick1.setListener(this);
        //Set JoyStickListener
        joyStick2.setListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onMove(JoyStick joyStick, double angle, double power) {
        double angleToSend;
        if (angle >= -Math.PI / 2 && angle <= Math.PI) {
            angleToSend = Math.PI / 2 - angle;
            //joyStick.setButtonColor(Color.parseColor("#FFCC00"));
        } else {
            angleToSend = -((3 * Math.PI) / 2) - angle;
            //joyStick.setButtonColor(Color.parseColor("#696969"));
        }
        Message msg = new Message();
        msg.obj = new Data(angleToSend, power);
        handler.sendMessage(msg);
        Log.d("Joystick On Move", "Power : " + power + " Angle : " + angle + " Angle To Send : " + angleToSend);
    }
}

