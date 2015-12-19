package com.edu.itu.smellsliketeamspirit;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.edu.itu.smellsliketeamspirit.R;

public class BehaviorsActivity extends AppCompatActivity {
    Button button1, button2, button3, button4;
    EditText edit1, edit2, edit3, edit4;
    Data data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new Data();
        setContentView(R.layout.activity_behaviors);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        edit1 = (EditText) findViewById(R.id.edit1);
        edit2 = (EditText) findViewById(R.id.edit2);
        edit3 = (EditText) findViewById(R.id.edit3);
        edit4 = (EditText) findViewById(R.id.edit4);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.power = Double.parseDouble(edit1.getText().toString());
                data.joystick = (byte) 0x03;
                if(HandlerService.mServiceHandler != null) {
                    Message msg = new Message();
                    msg.obj = data;
                    HandlerService.mServiceHandler.sendMessage(msg);
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.power = Double.parseDouble(edit2.getText().toString());
                data.joystick = (byte) 0x03;
                if(HandlerService.mServiceHandler != null) {
                    Message msg = new Message();
                    msg.obj = data;
                    HandlerService.mServiceHandler.sendMessage(msg);
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.power = Double.parseDouble(edit3.getText().toString());
                data.joystick = (byte) 0x03;
                if(HandlerService.mServiceHandler != null) {
                    Message msg = new Message();
                    msg.obj = data;
                    HandlerService.mServiceHandler.sendMessage(msg);
                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.power = Double.parseDouble(edit4.getText().toString());
                data.joystick = (byte) 0x03;
                if(HandlerService.mServiceHandler != null) {
                    Message msg = new Message();
                    msg.obj = data;
                    HandlerService.mServiceHandler.sendMessage(msg);
                }
            }
        });

    }

}
