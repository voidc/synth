package io.github.voidc.synth.compose;

/**
 * A note is an integer where 0 represents the pitch standard A4 with a frequency of 440 Hz
 */
public class NoteUtil {
    public static final int[] MAJOR_SCALE = {2, 2, 1, 2, 2, 2};
    public static final int[] MINOR_SCALE = {2, 1, 2, 2, 1, 2};
    public static final int[] MELODIC_MINOR_SCALE = {2, 1, 2, 2, 2, 2};
    public static final int[] HARMONIC_MINOR_SCALE = {2, 1, 2, 1, 2, 2};
    public static final int[] PENTATONIC_MAJOR_SCALE = {2, 2, 4, 2};
    public static final int[] PENTATONIC_MINOR_SCALE = {3, 3, 2, 3};

    public static final int[] MAJOR_TRIAD = {4, 4};
    public static final int[] MINOR_TRIAD = {3, 5};
    public static final int[] DIMINISHED_TRIAD = {3, 4};
    public static final int[] AUGMENTED_TRIAD = {4, 5};
    public static final int[] SUSPENDED_TRIAD = {2, 6};

    public static final int[] MAJOR_CHORD = {4, 4, 4};
    public static final int[] MINOR_CHORD = {3, 5, 3};
    public static final int[] DOMINANT_CHORD = {4, 4, 3};
    public static final int[] HALFDIMINISHED_CHORD = {3, 4, 4};
    public static final int[] DIMINISHED_CHORD = {3, 4, 3};

    private static final int NOTES_PER_OCTAVE = 12;
    private static final double STANDARD_PITCH_FREQ = 440.0;

    public static double noteToFrequency(int note) {
        return Math.pow(2, note / (double) NOTES_PER_OCTAVE) * STANDARD_PITCH_FREQ;
    }


    public static int frequencyToNote(double frequency) {
        return (int) Math.round(Math.log(frequency / STANDARD_PITCH_FREQ) * NOTES_PER_OCTAVE / Math.log(2));
    }

    public static int noteToMidi(int note) {
        return note + 69;
    }

    public static int midiToNote(int midi) {
        return midi - 69;
    }

    public static int octave(int note) {
        return note / NOTES_PER_OCTAVE + 4;
    }

    public static String noteToString(int note) {
        return "A A# H C C# D D# E F F# G G#".split(" ")[note % NOTES_PER_OCTAVE];
    }

    public static int[] genScale(int base, int[] pattern) {
        int[] result = new int[pattern.length+1];
        for(int i = 0; i < result.length; i++) {
            result[i] = base % NOTES_PER_OCTAVE;
            base += pattern[i % pattern.length]; //%: quick fix to avoid index out of bounds
        }
        return result;
    }

    public static int parseNote(String noteString) {
        noteString = noteString.toUpperCase().replaceAll(" ", ""); //uppercase and remove spaces
        noteString = noteString.replaceAll("IS", "#").replaceAll("ES", "B");
        if(!noteString.matches("[A-H](#|B)?(-?\\d*)?")) throw new IllegalArgumentException("Not a valid note format");
        if(noteString.charAt(0) == 'B') noteString = noteString.replaceFirst("B", "H"); //replace B notes with H notes
        int noteIndex = "A HC D EF G".indexOf(noteString.charAt(0));
        if(noteString.length() > 1) { //modifier, octave or both
            boolean modifier = true;
            if(noteString.charAt(1) == '#') {
                noteIndex++;
            } else if(noteString.charAt(1) == 'B') {
                noteIndex--;
            } else modifier = false;
            if(!modifier || noteString.length() > 2) { //octave
                int octave = Integer.parseInt(noteString.substring(modifier ? 2 : 1));
                noteIndex = (octave - 4) * NOTES_PER_OCTAVE + noteIndex;
            }
        }
        return noteIndex;
    }

}
