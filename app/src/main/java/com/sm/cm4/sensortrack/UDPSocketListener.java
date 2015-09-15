package com.sm.cm4.sensortrack;

import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import com.sm.cm4.sensortrack.MainActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPSocketListener extends Thread {
    byte [] recvBuf = new byte[15000];
    DatagramSocket socket;
    private String ip;
    private int port = 2015;
    private DatagramPacket packet;
    private RestConnection restConnection;
    private String url;
    private boolean running = true;

    public UDPSocketListener(String ip,RestConnection restConnection){
        this.restConnection = restConnection;
	    this.ip = ip;

        try {
            Log.i("listen","listening connection ip "+ip);
            this.socket = new DatagramSocket(port, InetAddress.getByName("0.0.0.0"));
            this.socket.setBroadcast(true);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        packet = new DatagramPacket(recvBuf,recvBuf.length);
    }
    @Override
    public void run(){
	while(running){
        try {
            Log.i("listen","listening connection"+url);
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String received = new String(packet.getData(),0,packet.getLength());
	    handleReceivedMessage(received);
        Log.i("listen","listening connection"+url);
	}
    }
    public String ip(){
	return this.ip;
    }
    public void closeListener(){
        if(socket.isConnected()) {
            socket.close();
        }
        this.running = false;
    }
    private void handleReceivedMessage(String msg){
	if( msg.startsWith("url:")){
	    String []content = msg.split(":");
	    url = content[1];
	}
	System.out.println(url);
    restConnection.setUrl(url);
    if(restConnection.hasValidUrl()){
       restConnection.registerDevice(ip, Build.MODEL);
       closeListener();
    }


    }



}
