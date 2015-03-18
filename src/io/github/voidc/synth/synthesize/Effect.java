package io.github.voidc.synth.synthesize;

/**
 * Created by Dominik on 12.03.2015.
 */
public abstract class Effect implements ISampleProvider {
    private ISampleProvider source;

    public Effect(ISampleProvider source) {
        this.source = source;
    }

    @Override
    public int getSamples(long sampleIndex, byte[] sampleBuffer) {
        int bytesRead = source.getSamples(sampleIndex, sampleBuffer);
        process(sampleBuffer);
        return bytesRead;
    }

    public abstract void process(byte[] sampleBuffer);
}
