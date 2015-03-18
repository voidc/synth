package io.github.voidc.synth.synthesize;

public class ADSREnvelope {
    private final double attackDuration;
    private final double decayDuration;
    private final double sustainLevel;
    public final double releaseDuration;

    private final double attackSlope;
    private final double decaySlope;
    private final double decayYOff;
    private final double releaseSlope;
    private final double releaseYOff;

    public ADSREnvelope(double attackLevel, double attackDuration, double decayDuration, double sustainLevel, double releaseDuration) {
        this.attackDuration = attackDuration;
        this.decayDuration = decayDuration;
        this.sustainLevel = sustainLevel;
        this.releaseDuration = releaseDuration;

        // pre compute constants
        this.attackSlope = attackLevel / attackDuration;
        this.decaySlope = (sustainLevel - attackLevel) / decayDuration;
        this.decayYOff = attackLevel - decaySlope * attackDuration;
        this.releaseSlope = -sustainLevel / releaseDuration;
        this.releaseYOff = sustainLevel - releaseSlope;
    }

    public ADSREnvelope(double attack, double decay, double release) {
        this(1, attack, decay, 0.7, release);
    }

    public double getAmplitude(double t) {
        if(t == 0) return 0;
        double value;
        if(t <= attackDuration) {
            value = attackSlope * t;
        } else if(t <= attackDuration + decayDuration){
            value = decaySlope * t + decayYOff;
        } else if (t <= 1) {
            value = sustainLevel;
        } else {
            value = releaseSlope * t + releaseYOff;
        }
        return Math.min(Math.max(value, 0), 1);
    }
}
