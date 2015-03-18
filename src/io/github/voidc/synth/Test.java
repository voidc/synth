package io.github.voidc.synth;

import io.github.voidc.synth.compose.SequenceBuilder;
import io.github.voidc.synth.compose.Sequencer;
import io.github.voidc.synth.synthesize.ADSREnvelope;
import io.github.voidc.synth.synthesize.IOscillator;
import io.github.voidc.synth.synthesize.Instrument;
import io.github.voidc.synth.synthesize.Player;

public class Test {
    private int[] tetrisMelody = {10, 5, 6, 8, 6, 5, 3, 3, 6, 10, 8, 6, 5, 5, 6, 8, 10, 6, 3, 3};
    private double[] tetrisMeasure = {0.5, 0.25, 0.25, 0.5, 0.25, 0.25, 0.5, 0.25, 0.25, 0.5, 0.25, 0.25, 0.5, 0.25, 0.25, 0.5, 0.5, 0.5, 0.5, 0.5};
    public static void main(String[] args) {
        new Test().testSequencer();
    }

    public void testSequencer() {
        IOscillator sinOsc = p -> Math.sin(2 * Math.PI * p);
        Instrument ins = new Instrument(sinOsc);
        ins.setAmplitudeMod(sinOsc, 4);
        ins.setFrequencyMod(sinOsc, 12);

        //int[] scale = genScale(parseNote("gis3"), MELODIC_MINOR_SCALE);
        SequenceBuilder seqB = new SequenceBuilder();
        seqB.setBpm(120);
        for(int i = 0; i < tetrisMelody.length; i++) {
            seqB.appendNote(tetrisMelody[i], tetrisMeasure[i]);
        }
        seqB.appendPause(0.5);

        Sequencer seq = new Sequencer(ins);
        seqB.apply(seq);

        ADSREnvelope env = new ADSREnvelope(0.8, 0.2, 0.5, 0.2, 0);
        seq.setEnvelope(env);
        //ISampleProvider sampler = new Sampler(ins, 440);

        Player p = new Player();
        p.setSampleProvider(seq);
        p.startPlayer();
    }

    /*
    ISSUES:
      - clicking or popping noise when frequency changes (see http://stackoverflow.com/a/740654/2554885)
     */
}
