package edu.utdallas.kwicsystem;

import java.util.Arrays;
import java.util.List;

public class NoiseWords {
	private static final List<String> noiseWords;
	
	static {
		noiseWords = Arrays.asList("$", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
				"p", "q", "w", "s", "t", "u", "v", "w", "x", "y", "z", "about", "after",
				"all", "also", "an", "and", "another", "any", "are", "as", "at", "be",
				"because", "been", "before", "being", "between", "both", "but", "by",
				"came", "can", "come", "could", "did", "do", "does", "each", "else", "for",
				"from", "get", "got", "had", "has", "have", "he", "her", "here", "him",
				"himself", "his", "how", "if", "in", "into", "is", "it", "its", "just",
				"like", "make", "many", "me", "might", "more", "most", "much", "must", "my",
				"never", "no", "now", "of", "on", "only", "or", "other", "our", "out",
				"over", "re", "said", "same", "see", "should", "since", "so", "some", "still",
				"such", "take", "than", "that", "the", "their", "them", "then", "there",
				"these", "they", "this", "those", "through", "to", "too", "under", "up",
				"use", "very", "want", "was", "way", "we", "well", "were", "what", "when",
				"where", "which", "while", "who", "will", "with", "would", "you", "your");
	}
	
	public static boolean isNoiseWord(String word) {
		final String wordLC = word.toLowerCase();
		return noiseWords.parallelStream()
				.filter(nw -> nw.equals(wordLC))
				.count() > 0;
	}
}
