package com.oia.ilkan.blg456eproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.erz.joysticklibrary.JoyStick;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ControlActivity extends AppCompatActivity implements JoyStick.JoyStickListener{
    JoyStick joyStick1, joyStick2;
    int port;
    String ip;
    SenderThread sender;
    Double currentAngle = 0.0, currentPower = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        Bundle args = getIntent().getExtras();
        port = args.getInt("port");
        ip = args.getString("ip");
        joyStick1 = (JoyStick) findViewById(R.id.joy1);
        joyStick2 = (JoyStick) findViewById(R.id.joy2);

        sender = new SenderThread();
        sender.start();

        joyStick1.setListener(this);
        //Set JoyStickListener
        joyStick2.setListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(sender.isAlive()) {
            try {
                sender.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMove(JoyStick joyStick, double angle, double power) {
        double angleToSend = 0;
        if(angle >= - Math.PI/2 && angle <= Math.PI) {
            angleToSend = Math.PI/2 - angle;
            //joyStick.setButtonColor(Color.parseColor("#FFCC00"));
        } else {
            angleToSend = -((3*Math.PI)/2) - angle;
            //joyStick.setButtonColor(Color.parseColor("#696969"));
        }


        synchronized (currentAngle) {
            synchronized (currentPower) {
                currentAngle = angleToSend;
                currentPower = power;
                sender.notify();
            }
        }

        Log.d("Joystick On Move", "Power : " + power + " Angle : " + angle + " Angle To Send : " + angleToSend);
    }

    class SenderThread extends Thread {

        private Socket hostThreadSocket;
        private OutputStream out;
        private boolean isPortCreated;
        public SenderThread() {
            isPortCreated = false;
        }

        @Override
        public void run() {
            super.run();
            if(!isPortCreated) {
                try {
                    hostThreadSocket = new Socket(ip, port);
                    isPortCreated = true;
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
            try {
                out = hostThreadSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] bytes = new byte[16];

            synchronized (currentAngle) {
                synchronized (currentPower) {
                    if(currentPower != null && currentAngle != null) {
                        ByteBuffer.wrap(bytes).putDouble(currentAngle);
                        ByteBuffer.wrap(bytes).putDouble(currentPower);
                        try {
                            out.write(bytes);
                            out.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}

