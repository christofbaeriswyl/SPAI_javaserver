package com.pranavpareek.mictest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class MicRecord extends Thread {

    private AudioFormat format;
    private DataLine.Info targetInfo;
    private DatagramSocket udpSocket;
    private int port;

    public MicRecord (AudioFormat format, DataLine.Info targetInfo) throws SocketException, IOException {
        this.format = format;
        this.targetInfo = targetInfo;
        this.port = 12345;
        this.udpSocket = new DatagramSocket(this.port);
    }

    public void run() {
        CircularBuffer bufcirc;
        CircularBufferLocal bufcirclocal;
        TargetDataLine targetDataLine = null;
        try {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetDataLine.open(format);
            targetDataLine.start();
            System.out.println("-- Running Server at " + InetAddress.getLocalHost() + "--");
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        int dataRead = -1;
        byte[] targetData = new byte[targetDataLine.getBufferSize() / 5];
        
        while(true) {
            try {                
            	dataRead = targetDataLine.read(targetData, 0, targetData.length);
            	bufcirc = CircularBuffer.getBufferObject();
            	bufcirclocal = CircularBufferLocal.getBufferObject();
            	if(dataRead == -1) {
                    System.out.println("Error reading microphone input");
                }
                else {
                    //write to buffer
                    bufcirclocal.writeToBuffer(targetData);
                }
                //byte[] buf = new byte[640*2];
            	byte[] buf = new byte[3584]; //44100 , 16 , 1 CH
            	DatagramPacket packet = new DatagramPacket(buf, buf.length);
                udpSocket.receive(packet);
                //String msg = new String(packet.getData()).trim();             
                //System.out.println("Message from " + packet.getAddress().getHostAddress() + ": " + msg);
                //write to buffer
                //bufcirc = CircularBuffer.getBufferObject();
                bufcirc.writeToBuffer(buf);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
}
