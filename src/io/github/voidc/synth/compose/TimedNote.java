package io.github.voidc.synth.compose;

public class TimedNote {
    public final int pitch;
    public final double time;
    public final double value; //maybe add amplitude
    public boolean active;
    public boolean played; //dirty fix

    public TimedNote(int pitch, double time, double value) {
        this.pitch = pitch;
        this.time = time;
        this.value = value;
        this.active = false;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TimedNote)) return false;
        TimedNote n = (TimedNote) obj;
        return n.pitch == this.pitch && n.time == n.time;
    }
}
