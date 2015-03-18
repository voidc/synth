package io.github.voidc.synth.synthesize;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class Player extends Thread {
    public static final int SAMPLE_RATE = 22050;
    public static  final int BUFFER_SIZE = 1000;
    public static final int BYTES_PER_SAMPLE = 2;
    //public static double masterVolume = 1.0;
    private static final int SAMPLE_SIZE = 16;
    private AudioFormat format;
    private DataLine.Info info;
    private SourceDataLine line;
    private boolean done;
    private byte[] sampleData = new byte[BUFFER_SIZE];
    private ISampleProvider provider;

    public Player() {
        format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE, 1, true, true);
        info = new DataLine.Info(SourceDataLine.class, format);
    }

    @Override
    public void run() {
        done = false;
        long sampleIndex = 0;

        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            while (!done) {
                int bytesRead = provider.getSamples(sampleIndex, sampleData);
                sampleIndex += bytesRead / BYTES_PER_SAMPLE;
                if (bytesRead > 0) {
                    line.write(sampleData, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            line.drain();
            line.close();
        }
    }

    public void startPlayer() {
        if (provider != null) {
            start();
        }
    }

    public void stopPlayer() {
        done = true;
    }

    public void setSampleProvider(ISampleProvider provider) {
        this.provider = provider;
    }
}
