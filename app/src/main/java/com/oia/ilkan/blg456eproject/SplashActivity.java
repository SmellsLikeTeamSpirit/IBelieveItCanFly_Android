package com.oia.ilkan.blg456eproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashActivity extends AppCompatActivity {
    int port;
    String ip;
    Button press;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        press = (Button) findViewById(R.id.gecisButton);

        Bundle argsCame = getIntent().getExtras();
        port = argsCame.getInt("port");
        ip = argsCame.getString("ip");

        press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("ip", ip);
                args.putInt("port", port);
                Intent intent = new Intent(SplashActivity.this,ControlActivity.class);
                intent.putExtras(args);
                startActivity(intent);
                finish();

            }
        });


    }
}
