package io.github.voidc.synth.synthesize;

public interface ISampleProvider {
    public int getSamples(long sampleIndex, byte[] sampleBuffer);
}
