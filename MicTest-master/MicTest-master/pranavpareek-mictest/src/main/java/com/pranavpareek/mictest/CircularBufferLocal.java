package com.pranavpareek.mictest;

public class CircularBufferLocal {

    private final int BUFFER_SIZE = 1024*1024;
    private static CircularBufferLocal buf = null;
    private byte[] buffer = new byte[BUFFER_SIZE];
    private int read_index = 0, write_index = 0;
    private int data_size = 0;
    private boolean data_ready = false;

    private CircularBufferLocal() {}

    public static CircularBufferLocal getBufferObject() {
        if(buf == null) {
            buf = new CircularBufferLocal();
        }
        return buf;
    }

    public synchronized void writeToBuffer(byte[] data) {
        while(data_ready == true) {
            try {
                wait();
            }
            catch(Exception e) {
                System.out.println(e.toString());
            }
        }
        if(data.length > 0 && data.length < BUFFER_SIZE) {
            for (int i = 0; i < data.length; i++) {
                buffer[write_index] = data[i];
                write_index++;
                data_size++;
                if(write_index >= BUFFER_SIZE) {
                    write_index = 0;
                }
            }
            data_ready = true;
            notifyAll();
        }
    }

    public synchronized byte[] readFromBuffer() {
        while(data_ready == false) {
            try {
                wait();
            }
            catch(Exception e) {
                System.out.println(e.toString());
            }
        }
        int size = data_size;
        byte[] tempBuffer = new byte[data_size];
        for(int i=0; i<size; i++) {
            tempBuffer[i] = buffer[read_index];
            read_index++;
            data_size--;
            if(read_index >= BUFFER_SIZE) {
                read_index = 0;
            }
        }

        data_ready = false;
        notifyAll();
        return tempBuffer;
    }

}

