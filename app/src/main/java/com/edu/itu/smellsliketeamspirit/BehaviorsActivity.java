package com.edu.itu.smellsliketeamspirit;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BehaviorsActivity extends AppCompatActivity {
    Button btnDistance, btnObst, btnTurn, btnUp, btnBigRed;
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
        btnUp = (Button) findViewById(R.id.btnUp);
        btnBigRed = (Button) findViewById(R.id.btnBigRed);
        editDistance = (EditText) findViewById(R.id.editDistance);
        editAngle = (EditText) findViewById(R.id.editAngle);

        btnDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double pow = editDistance.getText().length() > 0 ? Double.parseDouble(editDistance.getText().toString()) : null;
                if(pow != null)
                    sendDataToService((byte) 0x03, pow, 0.0);
            }
        });

        btnTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double ang = editAngle.getText().length() > 0 ? Double.parseDouble(editAngle.getText().toString()) : null;
                if(ang != null)
                    sendDataToService((byte) 0x04, 0.0, ang);
            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double pow = editDistance.getText().length() > 0 ? Double.parseDouble(editDistance.getText().toString()) : null;
                if(pow != null)
                    sendDataToService((byte) 0x05, pow, 0.0);
            }
        });

        btnObst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToService((byte) 0x06, 0.0, 0.0);
            }
        });

        btnBigRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToService((byte) 0x00, 0.0, 0.0);
            }
        });

    }

}
