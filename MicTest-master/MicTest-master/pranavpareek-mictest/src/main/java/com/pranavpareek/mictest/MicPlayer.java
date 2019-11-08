package com.pranavpareek.mictest;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class MicPlayer extends Thread {

    AudioFormat format;
    DataLine.Info sourceInfo;

    public MicPlayer(AudioFormat format, DataLine.Info sourceInfo) {
        this.format = format;
        this.sourceInfo = sourceInfo;
    }

    public void run() {
        CircularBuffer buf;
        CircularBufferLocal buflocal;
        SourceDataLine sourceDataLine = null;
        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("_yy_mm_dd_hh_mm_ss");  
        String strDate = dateFormat.format(date);  
        FileOutputStream fileoutput = null;
        FileOutputStream fileoutputlocal = null;
        try {
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
            sourceDataLine.open(format);
            sourceDataLine.start();
            fileoutput = new FileOutputStream("audio" + strDate + ".raw");
            fileoutputlocal = new FileOutputStream("audiolocal" + strDate + ".raw");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        while(true) {
            try {
                buf = CircularBuffer.getBufferObject();
                buflocal = CircularBufferLocal.getBufferObject();
                byte[] tempBuf = buf.readFromBuffer();
                if (tempBuf != null) {
                    //play audio
                    sourceDataLine.write(tempBuf, 0, tempBuf.length);
                    fileoutput.write(tempBuf);
                }
                byte[] tempBuflocal = buflocal.readFromBuffer();
                if (tempBuflocal != null) {
                    //play audio
                    //sourceDataLine.write(tempBuf, 0, tempBuf.length);
                    fileoutputlocal.write(tempBuflocal);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
}



