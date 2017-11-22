package edu.utdallas.kwicsystem;

import java.util.regex.Pattern;

/**
 *
 * @author Ali, Dongchen, and Haoliang (ADH Team)
 */
public class CircularShift extends LinesBatch {
    private LinesBatch originals;

    public CircularShift(LinesBatch originals) {
        super(new Line[0]);
        this.originals = originals;
        generateCircularShifts();
    }
    
    private void add(Line ln) {
        Line[] lines_ext = new Line[lines.length + 1];
        System.arraycopy(lines, 0, lines_ext, 0, lines.length);
        lines_ext[lines.length] = ln;
        lines = lines_ext;
    }
    
    private int countEffectiveWords(String text) {
		return (int) Pattern.compile("[\\s\\p{Punct}]+")
				.splitAsStream(text)
				.filter(word -> !NoiseWords.isNoiseWord(word))
				.count();
	}

    private Line circularShift(Line ln, Line original) {
        String[] words = ln.words();
        String[] shiftedWords = new String[words.length];
        System.arraycopy(words, 1, shiftedWords, 0, words.length - 1);
        shiftedWords[words.length - 1] = words[0];
        return new Line(shiftedWords, original);
    }
    
    private Line sanitize(Line ln, Line original) {
    	String[] words = ln.words();
    	int i = 0;
    	while(NoiseWords.isNoiseWord(words[i++])) {
    		ln = circularShift(ln, original);
    	}
    	return ln;
    }

    private void generateCircularShifts() {
        int j = 0;
        for(int i = 0; i < originals.size(); i++) {
            Line first = originals.getLine(i);
            add(sanitize(first, first));
            j++;
            final int count = countEffectiveWords(first.body());
            for(int k = j; k < count; j++, k++) {
                add(sanitize(circularShift(lines[j-1], first), first));
            }
        }
    }
}
