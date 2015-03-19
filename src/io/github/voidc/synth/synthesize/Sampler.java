package io.github.voidc.synth.synthesize;

public class Sampler implements ISampleProvider {
    private Instrument ins;
    double freq;

    public Sampler(Instrument ins, double freq) {
        this.ins = ins;
        this.freq = freq;
    }

    @Override
    public int getSamples(long sampleIndex, byte[] sampleBuffer) {
        int bufferIndex = 0;
        for (int i = 0; i < sampleBuffer.length / 2; i++) {
            double sample = sample(sampleIndex + i);
            short shortSample = (short) Math.round(sample * Short.MAX_VALUE);
            sampleBuffer[bufferIndex++] = (byte) (shortSample >> 8);
            sampleBuffer[bufferIndex++] = (byte) (shortSample & 0xFF);
        }
        return bufferIndex;
    }

    private double sample(long sampleIndex) {
        double time = sampleIndex / (double) Player.SAMPLE_RATE;
        return ins.synthesize(time, freq, 1);
    }
}
