package com.oia.ilkan.blg456eproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.erz.joysticklibrary.JoyStick;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class ControlActivity extends AppCompatActivity implements JoyStick.JoyStickListener{
    JoyStick joyStick1, joyStick2;
    int port;
    String ip;
    Socket socket;
    SenderThread sender;
    Double currentAngle, currentPower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        Bundle args = getIntent().getExtras();
        port = args.getInt("port");
        ip = args.getString("ip");
        joyStick1 = (JoyStick) findViewById(R.id.joy1);
        joyStick2 = (JoyStick) findViewById(R.id.joy2);
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            System.err.println(e);
        }

        sender = new SenderThread(socket);
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
        currentAngle = angleToSend;
        currentPower = power;
        sender.notify();

        Log.d("Joystick On Move", "Power : " + power + " Angle : " + angle + " Angle To Send : " + angleToSend);
    }

    class SenderThread extends Thread {

        private Socket hostThreadSocket;
        private OutputStream out;
        public SenderThread(Socket socket) {
            hostThreadSocket = socket;
        }

        @Override
        public void run() {
            super.run();
            try {
                out = hostThreadSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] bytes = new byte[8];
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

