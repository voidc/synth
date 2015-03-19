package io.github.voidc.synth.compose;

import io.github.voidc.synth.synthesize.ADSREnvelope;
import io.github.voidc.synth.synthesize.ISampleProvider;
import io.github.voidc.synth.synthesize.Instrument;
import io.github.voidc.synth.synthesize.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static io.github.voidc.synth.compose.NoteUtil.*;

public class Sequencer implements ISampleProvider {
    private List<TimedNote> notes = new LinkedList<>();
    private double duration;
    private Instrument ins;
    private ADSREnvelope env;

    public Sequencer(Instrument ins) {
        this.ins = ins;
    }

    public void addNote(TimedNote note) {
        this.notes.add(note);
    }

    public List<TimedNote> updateActive(double time) {
        List<TimedNote> active = new ArrayList<>();
        for(int n = 0; n < notes.size(); n++) {
            TimedNote note = notes.get(n);
            if(note.active) {
                if(time > note.time + note.value + env.releaseDuration * note.value || time < note.time) {
                    note.setActive(false);
                } else {
                    active.add(note);
                }
            } else if(time >= note.time && time - note.time < 0000.1) { //attention
                note.setActive(true);
                active.add(note);
            }
        }
        return active;
    }

    public double trimDuration() {
        double lowerTimeOffset = notes.stream().min((e1, e2) -> (int) Math.round(e1.time - e2.time)).get().time;
        double max = 0;
        for(int n = 0; n < notes.size(); n++) {
            TimedNote note = notes.get(n);
            if(lowerTimeOffset > 0) {
                note = new TimedNote(note.pitch, note.time - lowerTimeOffset, note.value);
                notes.set(n, note);
            }
            if(note.time + note.value > max) {
                max = note.time + note.value;
            }
        }
        duration = max;
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    public void setInstrument(Instrument instrument) {
        this.ins = instrument;
    }

    public void setEnvelope(ADSREnvelope env) {
        this.env = env;
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
        List<TimedNote> notes = updateActive(time % duration); //no release!!
        if(notes.size() == 0) return 0;
        double sum = 0;
        for(int n = 0; n < notes.size(); n++) {
            TimedNote note = notes.get(n);
            double amplitude = env.getAmplitude(((time % duration) - note.time) / note.value);
            double frequency = noteToFrequency(note.pitch);
            sum += ins.synthesize(time, frequency, amplitude);
        }
        return sum / notes.size();
    }

}
