package edu.utdallas.searchengine;

import java.util.Set;
import java.util.function.BiFunction;

import edu.utdallas.kwicsystem.Line;
import edu.utdallas.kwicsystem.NoiseWords;
import edu.utdallas.util.Triple;

public class Word implements SearchExpression {
	public final String word;
	private final boolean noise;
	
	public Word(String word) {
		if(word.split("[\\s]+").length > 1) {
			throw new IllegalArgumentException("the word cannot contain white space");
		}
		this.word = word;
		this.noise = NoiseWords.isNoiseWord(word);
	}

	@Override
	public Set<Triple<Line, String, Double>> evaluate(SearchContext context,
			BiFunction<String, String, Boolean> wordComparator,
			boolean caseSensitive) {
		if(noise) {
			return context.all();
		} 
		return context.select(ln -> {
			//final String firstWord = ln.words()[0];
			for(String wordOfLine : ln.words()) {
				if(caseSensitive) {
					if(wordComparator.apply(wordOfLine, word)) {
						return true;
					}
				}
				if(wordComparator.apply(wordOfLine.toLowerCase(), word.toLowerCase())) {
					return true;
				}
			}
			return false;
		});
	}
}
