package io.github.voidc.synth.compose;

import java.util.LinkedList;
import java.util.List;

public class SequenceBuilder {
    private List<TimedNote> notes = new LinkedList<>();
    private double bpm = 60;
    private double time = 0;

    public void appendNote(int notePitch, double noteValue) {
        double duration = noteValueToDuration(noteValue);
        notes.add(new TimedNote(notePitch, time, duration));
        time += duration;
    }

    public void appendChord(int[] notePitches, double chordValue) {
        double duration = noteValueToDuration(chordValue);
        for(int pitch : notePitches) {
            notes.add(new TimedNote(pitch, time, duration));
        }
        time += duration;
    }

    public void appendPause(double pauseValue) {
        time += noteValueToDuration(pauseValue);
    }

    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    public double noteValueToDuration(double noteValue) {
        return 4 * (60 / bpm) * noteValue;
    }


    public void apply(Sequencer seq) {
        for(TimedNote n : notes) {
            seq.addNote(n);
        }
        seq.setDuration(time);
    }
}
