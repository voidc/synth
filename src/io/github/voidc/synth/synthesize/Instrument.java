package io.github.voidc.synth.synthesize;

public class Instrument {
    IOscillator amplitudeMod, frequencyMod, oscillator;
    double amFreq, fmFreq = 0;

    public Instrument(IOscillator osc) {
        this.oscillator = osc;
    }

    public double synthesize(double time, double frequency, double amplitude) {
        double fmModValue = 0;
        if(amplitudeMod != null) amplitude *= amplitudeMod.getValue(time * amFreq);
        if(frequencyMod != null) fmModValue = frequencyMod.getValue(time * fmFreq);
        return amplitude * oscillator.getValue(time * frequency + fmModValue / (2 * Math.PI));
    }

    public void setAmplitudeMod(IOscillator amplitudeMod, double amFreq) {
        this.amplitudeMod = amplitudeMod;
        this.amFreq = amFreq;
    }

    public void setFrequencyMod(IOscillator frequencyMod, double fmFreq) {
        this.frequencyMod = frequencyMod;
        this.fmFreq = fmFreq;
    }
}
