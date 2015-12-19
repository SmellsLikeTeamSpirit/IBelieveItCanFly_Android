package com.edu.itu.smellsliketeamspirit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                Bundle args = new Bundle();
                args.putString("ip", ip.getText().toString());
                args.putInt("port", port);
                Intent intent = new Intent(MainActivity.this,ControlActivity.class);

                startActivity(intent);
                Intent serviceIntent = new Intent(MainActivity.this,HandlerService.class);
                serviceIntent.putExtras(args);
                startService(serviceIntent);
                finish();
            }
        });

    }


}
