package com.edu.itu.smellsliketeamspirit;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BehaviorsActivity extends AppCompatActivity {
    Button btnDistance, btnObst, btnTurn, btnFixedAngle;
    EditText editDistance, editAngle;

    private void sendDataToService(byte joystick, Double power, Double angle)
    {
        if (HandlerService.mServiceHandler != null && power != null &&  angle != null) {
            Message msg = new Message();
            msg.obj = new Data(joystick,power, angle);
            HandlerService.mServiceHandler.sendMessage(msg);
        }
        else {
            Log.d("BEHAVIOR", "Göndermedim abi");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behaviors);
        btnDistance = (Button) findViewById(R.id.btnDistance);
        btnObst = (Button) findViewById(R.id.btnObst);
        btnTurn = (Button) findViewById(R.id.btnTurn);
        btnFixedAngle = (Button) findViewById(R.id.btnFixedAngle);
        editDistance = (EditText) findViewById(R.id.editDistance);
        editAngle = (EditText) findViewById(R.id.editAngle);

        btnDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToService((byte) 0x03, Double.parseDouble(editDistance.getText().toString()), 0.0);
            }
        });

        btnObst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToService((byte) 0x04, 0.0, 0.0);
            }
        });

        btnTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToService((byte) 0x05, 0.0, Double.parseDouble(editAngle.getText().toString()));
            }
        });

        btnFixedAngle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToService((byte) 0x06, Double.parseDouble(editDistance.getText().toString()), Double.parseDouble(editAngle.getText().toString()));
            }
        });

    }

}
