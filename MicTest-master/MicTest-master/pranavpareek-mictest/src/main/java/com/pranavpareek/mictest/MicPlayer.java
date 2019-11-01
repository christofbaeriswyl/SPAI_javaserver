package com.pranavpareek.mictest;

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
        SourceDataLine sourceDataLine = null;

        try {
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
            sourceDataLine.open(format);
            sourceDataLine.start();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        while(true) {
            try {
                buf = CircularBuffer.getBufferObject();
                byte[] tempBuf = buf.readFromBuffer();
                if (tempBuf != null) {
                    //play audio
                    sourceDataLine.write(tempBuf, 0, tempBuf.length);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
}

