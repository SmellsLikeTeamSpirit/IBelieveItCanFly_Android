package com.oia.ilkan.blg456eproject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Worker implements Handler.Callback {

    protected Socket socket;
    protected BufferedWriter writer;
    OutputStream out;

    public Worker(String ip, int port) {
        try {
            this.socket = new Socket(ip, port);
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedWriter getWriter(){
        return this.writer;
    }

    public Socket getSocket(){
        return this.socket;
    }


    @Override
    public boolean handleMessage(Message msg) {
        Data data = (Data) msg.obj;
        if (data != null){

        }
        return true;
    }
}
