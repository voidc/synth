package io.github.voidc.synth;

import io.github.voidc.synth.compose.SequenceBuilder;
import io.github.voidc.synth.compose.Sequencer;
import io.github.voidc.synth.synthesize.ADSREnvelope;
import io.github.voidc.synth.synthesize.IOscillator;
import io.github.voidc.synth.synthesize.Instrument;
import io.github.voidc.synth.synthesize.Player;

public class Test {
    private final int PAUSE = 1000;
    private int[] tetrisMelody = {
            10, 5, 6, 8, 6, 5, 3, 3, 6, 10, 8, 6, 5, 5, 6, 8, 10, 6, 3, 3, PAUSE,
            PAUSE, 8, 8, 11, 15, 14, 11, 10, 10, 6, 10, 8, 6, 5, 5, 6, 8, 10, 6, 3, 3, PAUSE
    };
    private double[] tetrisMeasure = {
            4, 8, 8, 4, 8, 8, 4, 8, 8, 4, 8, 8, 4, 8, 8, 4, 4, 4, 4, 4, 4,
            8, 8, 8, 8, 4, 8, 8, 4, 8, 8, 4, 8, 8, 4, 8, 8, 4, 4, 4, 4, 4, 4
    };

    public static void main(String[] args) {
        new Test().testSequencer();
    }

    public void testSequencer() {
        IOscillator sinOsc = p -> Math.sin(2 * Math.PI * p);
        Instrument ins = new Instrument(sinOsc);
        ins.setAmplitudeMod(sinOsc, 4);
        ins.setFrequencyMod(sinOsc, 12);

        SequenceBuilder seqB = new SequenceBuilder();
        seqB.setBpm(120);
        for(int i = 0; i < tetrisMelody.length; i++) {
            if(tetrisMelody[i] == PAUSE) {
                seqB.appendPause(1 / tetrisMeasure[i]);
            } else {
                seqB.appendNote(tetrisMelody[i], 1 / tetrisMeasure[i]);
            }
        }

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
