package com.edu.itu.smellsliketeamspirit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.erz.joysticklibrary.JoyStick;

public class ControlActivity extends AppCompatActivity implements JoyStick.JoyStickListener {
    JoyStick joyStick1, joyStick2;
    Data data;
    Button behaviorsButton;
    public static Handler handler;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        joyStick1 = (JoyStick) findViewById(R.id.joy1);
        joyStick2 = (JoyStick) findViewById(R.id.joy2);
        behaviorsButton = (Button) findViewById(R.id.behaviorsButton);
        data = new Data();

        //Set JoyStickListener
        joyStick1.setListener(this);
        joyStick2.setListener(this);

        behaviorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControlActivity.this, BehaviorsActivity.class));
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        };
    }

    @Override
    public void onMove(JoyStick joyStick, double angle, double power) {
        double angleToSend;
        if (angle >= -Math.PI / 2 && angle <= Math.PI) {
            angleToSend = Math.PI / 2 - angle;
        } else {
            angleToSend = -((3 * Math.PI) / 2) - angle;
        }
        data.angle = angleToSend;
        data.power = power;
        data.joystick = joyStick == joyStick1 ? (byte) 0x01 : (byte) 0x02;
        if(HandlerService.mServiceHandler != null) {
            Message msg = new Message();
            msg.obj = data;
            HandlerService.mServiceHandler.sendMessage(msg);
        }
    }
}

