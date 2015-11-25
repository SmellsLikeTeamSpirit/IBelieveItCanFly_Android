package com.oia.ilkan.blg456eproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.erz.joysticklibrary.JoyStick;

import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity  {
    EditText ip;
    Button connect;
    int port = 7575;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ip = (EditText) findViewById(R.id.ip_address);
        connect = (Button) findViewById(R.id.connectButton);


        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Socket socket;
                /*if(ip.getText().toString().equals(""))
                    return;
                try {
                    socket = new Socket(ip.getText().toString(),port);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                Bundle args = new Bundle();
                args.putString("ip", ip.getText().toString());
                args.putInt("port",port);
                Intent intent = new Intent(MainActivity.this,SplashActivity.class);
                intent.putExtras(args);
                startActivity(intent);
                finish();
            }
        });

    }


}
