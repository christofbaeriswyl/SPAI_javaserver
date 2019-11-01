package com.pranavpareek.mictest;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class App {
	public static void main(String[] args) {
		System.out.println("Playing");
		
		AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
		DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
		DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);
		try {
			//Thread 1: Record data from mic and copy in a circular buffer
			MicRecord recorder = new MicRecord(format, targetInfo);
			//Thread 2: Read data from ring buffer and play sound
			MicPlayer player = new MicPlayer(format, sourceInfo);
			recorder.start();
			player.start();
			recorder.join();
			player.join();
		} catch (IOException | InterruptedException e1) {			
			e1.printStackTrace();
		}
	}
}
