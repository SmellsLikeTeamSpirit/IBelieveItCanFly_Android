package com.edu.itu.smellsliketeamspirit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.net.Socket;

public class MainActivity extends AppCompatActivity  {
    EditText ip;
    Button connect;
    View focusRemover;
    int port = 7575;
    public static Handler handler;
    Handler timeout;
    Runnable timeoutRunner;
    Intent serviceIntent;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ip = (EditText) findViewById(R.id.ip_address);
        connect = (Button) findViewById(R.id.connectButton);
        focusRemover = (LinearLayout) findViewById(R.id.focusHere);

        timeout = new Handler();
        timeoutRunner = new Runnable() {
            @Override
            public void run() {
                if(serviceIntent != null)
                    stopService(serviceIntent);
                progressDialog.dismiss();
            }
        };

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    timeout.removeCallbacks(timeoutRunner);
                    Boolean answer = (Boolean) msg.obj;
                    if(answer == true) {
                        Intent intent = new Intent(MainActivity.this, ControlActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        if(serviceIntent != null)
                            stopService(serviceIntent);
                        progressDialog.dismiss();
                    }
                }
            }
        };



        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("ip", ip.getText().toString());
                args.putInt("port", port);

                serviceIntent = new Intent(MainActivity.this,HandlerService.class);
                serviceIntent.putExtras(args);
                startService(serviceIntent);

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Connecting");
                progressDialog.show();
                focusRemover.requestFocus();
                timeout.postDelayed(timeoutRunner, 30000);
            }
        });

    }


}
