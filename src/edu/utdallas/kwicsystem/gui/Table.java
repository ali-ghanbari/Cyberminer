package edu.utdallas.kwicsystem.gui;

import java.awt.Font;
import java.awt.TextArea;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Table extends TextArea {
	private static final long serialVersionUID = 1L;
	private static Font font = new Font("Monospaced", Font.PLAIN, 20);
	private final String[] headers;
	private List<String[]> tuples;
	private final int[] max;
	
	public Table(String... headers) {
		super();
		setFont(font);
		if(headers.length < 1) {
			throw new IllegalArgumentException("A table must have at least one column");
		}
		this.headers = headers;
		this.max = new int[headers.length];
		for(int i = 0; i < headers.length; i++) {
			final String header = this.headers[i];
			this.max[i] = header.length();
			this.headers[i] = header.toUpperCase();
		}
		tuples = new ArrayList<>();
		setEditable(false);
	}
	
	public void deleteMatching(final int column, final String value) {
		tuples = tuples.parallelStream()
				.filter(tuple -> !tuple[column].equals(value))
				.collect(Collectors.toList());
		updateMax();
	}
	
	public void clearContents() {
		for(int i = 0; i < headers.length; i++) {
			final String header = this.headers[i];
			this.max[i] = header.length();
		}
		tuples.clear();
	}
	
	public List<String[]> tuples() {
		return tuples;
	}
	
	private void updateMax(String[] components) {
		for(int i = 0; i < components.length; i++) {
			final int len = components[i].length();
			if(max[i] < len) {
				max[i] = len;
			}
		}
	}
	
	private void updateMax() {
		tuples.forEach(tuple -> updateMax(tuple));
	}
	
	private String fill(String pattern, int count) {
		String result = "";
		while(count-- > 0) {
			result += pattern;
		}
		return result;
	}
	
	private String alignCenter(String text, int fieldSize) {
		final int textLen = text.length();
		String padding = "";
		if(textLen < fieldSize) {
			padding = fill(" ", (fieldSize - textLen) / 2);
		}
		return padding + text;
	}
	
	private String field(int fieldSize, String text, boolean center) {
		String result = center ? alignCenter(text, fieldSize) : text;
		int paddingSize = fieldSize - result.length();
		return result + fill(" ", paddingSize);
	}
	
	public void addTuple(String... tuple) {
		if(tuple.length != headers.length) {
			throw new IllegalArgumentException("Components count must match headers count");
		}
		updateMax(tuple);
		tuples.add(tuple);
	}
	
	private String hLine() {
		String hLine = "";
		for(int n : max) {
			hLine += "|" + fill("_", n);
		}
		return hLine + "|\n";
	}
	
	public void update() {
		String table = "";
		if(tuples.size() > 0) {
			for(int n : max) {
				table += "_" + fill("_", n);
			}
			table += "_\n";
			for(int i = 0; i < headers.length; i++) {
				table += "|" + field(max[i], headers[i], true);			
			}
			table += "|\n";
			table += hLine();
			for(String[] tuple : tuples) {
				for(int i = 0; i < tuple.length; i++) {
					table += "|" + field(max[i], tuple[i], false);
				}
				table += "|\n";
				table += hLine();
			}
		}
		setText(table);
	}
}
