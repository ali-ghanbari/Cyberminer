package edu.utdallas.kwicsystem;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author Ali, Dongchen, and Haoliang (ADH Team)
 */
public class Line implements Comparable<Line> {
    protected String[] words;
    /*the original line from which this line may be derived, e.g. through circular shift*/
    protected Line original;

    public Line(String[] words, Line original) {
        this.words = words;
        this.original = original;
    }

    public Line(String body) {
        words = body.split("[\\s]+");
        this.original = this;
    }
    
    public String[] words() {
        return words;
    }
        
    public String body() {
    	return Arrays.stream(words).collect(Collectors.joining(" "));
    }

    @Override
    public String toString() {
        return body();
    }

    @Override
    public int compareTo(Line o) {
        int min = Math.min(size(), o.size());
        for (int i = 0; i < min; i++) {
            int cr = words[i].compareToIgnoreCase(o.words[i]);
            if (cr != 0) {
                return cr;
            }
        }
        return size() - o.size();
    }

    public int size() {
        return words.length;
    }
    
    @Override
    public boolean equals(Object o) {
    	if(o == null || !(o instanceof Line)) {
    		return false;
    	}
    	Line other = (Line) o;
    	if(other.words.length != words.length) {
    		return false;
    	}
    	for(int i = 0; i < words.length; i++) {
    		if(!words[i].equals(other.words[i])) {
    			return false;
    		}
    	}
    	return true;
    }
    
    @Override
    public int hashCode() {
    	final int prime = 31;
    	int result = 0;
    	for(String word : words) {
    		result += prime * word.hashCode();
    	}
    	return result;
    }
    
    public Line originalLine() {
    	return original;
    }
}
