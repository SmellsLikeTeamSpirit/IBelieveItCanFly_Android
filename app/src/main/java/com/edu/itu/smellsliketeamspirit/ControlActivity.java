package com.edu.itu.smellsliketeamspirit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.erz.joysticklibrary.JoyStick;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ControlActivity extends AppCompatActivity implements JoyStick.JoyStickListener {
    JoyStick joyStick1, joyStick2;
    int port;
    String ip;
    Data data;
    NetworkThread thread;
    Object lock = new Object();
    Socket socket;
    DataOutputStream out;
    ProgressDialog dialog;
    Button behaviorsButton;
    boolean paused = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        Bundle args = getIntent().getExtras();

        port = args.getInt("port");
        ip = args.getString("ip");

        joyStick1 = (JoyStick) findViewById(R.id.joy1);
        joyStick2 = (JoyStick) findViewById(R.id.joy2);
        behaviorsButton = (Button) findViewById(R.id.behaviorsButton);

        dialog = new ProgressDialog(ControlActivity.this);
        dialog.setTitle("Connecting");
        dialog.setMessage("Please Wait");
        dialog.show();
        data = new Data();

        thread = new NetworkThread();
        thread.start();

        //Set JoyStickListener
        joyStick1.setListener(this);
        joyStick2.setListener(this);

        behaviorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControlActivity.this,BehaviorsActivity.class));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*synchronized (lock) {
            paused = true;
            lock.notify();
        }
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(thread.isAlive()) {
            synchronized (lock) {
                paused = true;
                lock.notify();
            }

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
            data.joystick = joyStick == joyStick1 ? (byte)0x01 : (byte)0x02;
            lock.notify();
        }
        Log.d("Joystick On Move", "Power : " + power + " Angle : " + angle + " Angle To Send : " + angleToSend);
    }

    class NetworkThread extends Thread {

        public NetworkThread() {

        }

        @Override
        public void run() {
            Log.d("NetworkThread","Started");
            try {
                socket = new Socket(ip, port);
                if(socket.isConnected()) {
                    Log.d("Socket", "Connected");
                    out = new DataOutputStream(socket.getOutputStream());
                    if(dialog.isShowing())
                        dialog.dismiss();
                    while (true) {
                        synchronized (lock) {
                            try {
                                lock.wait();
                                if(paused) {
                                    Log.d("NetworkThread", "Paused");
                                    break;
                                }
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
                    out.close();
                    socket.close();
                }

            } catch (IOException e) {
                Log.d("Socket", "Can't connect");
            }
        }
    }
}

