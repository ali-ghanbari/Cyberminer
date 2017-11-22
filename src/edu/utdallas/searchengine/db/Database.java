package edu.utdallas.searchengine.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import edu.utdallas.kwicsystem.Line;
import edu.utdallas.searchengine.SearchContext;
import edu.utdallas.util.Triple;

public class Database implements SearchContext {
	private static Database instance = null;
	private List<Triple<Integer, Line, String>> linesURL; //sorted by Line
	private Map<String, Double> urlBalance;
	private Map<String, Integer> visitFrequency;
	
	private Database() {
		linesURL = new ArrayList<>();
		urlBalance = new HashMap<>();
		visitFrequency = new HashMap<>();
	}
	
	public static Database v() {
		if(instance == null) {
			instance = new Database();
		}
		return instance;
	}
	
	public boolean updateURLBalance(String url, double value) {
		if(!urlBalance.containsKey(url)) {
			return false;
		}
		urlBalance.put(url, value);
		return true;
	}
	
	public Double getBalanceForURL(String url) {
		return urlBalance.get(url);
	}
	
	public Integer getVisitFrequencyForURL(String url) {
		return visitFrequency.get(url);
	}
	
	public boolean incrementURLVisitFrequenct(String url) {
		if(visitFrequency.containsKey(url)) {
			visitFrequency.put(url, visitFrequency.get(url) + 1);
			return true;
		}
		return false;
	}
	
	public void addBatch(List<Triple<Integer, Line, String>> batch) {
		linesURL = merge(linesURL, batch);
		batch.stream()
			.filter(t -> urlBalance.get(t.third()) == null)
			.forEach(t -> urlBalance.put(t.third(), 0.));
		batch.stream()
			.filter(t -> visitFrequency.get(t.third()) == null)
			.forEach(t -> visitFrequency.put(t.third(), 0));
	}
	
	@Override
	public Set<Triple<Line, String, Double>> select(Predicate<Line> predicate) {
		return linesURL.parallelStream()
				.filter(t -> predicate.test(t.second()))
				.map(t -> new Triple<>(t.second(), t.third(), urlBalance.get(t.third())))
				.collect(Collectors.toSet());
	}
	
	public void deleteRowByURL(String url) {
		urlBalance.remove(url); 
		linesURL = linesURL.parallelStream()
				.filter(t -> !url.equals(t.third()))
				.collect(Collectors.toList());
	}
	
	@Override
	public Set<Triple<Line, String, Double>> all() {
		return linesURL.parallelStream()
				.map(t -> new Triple<>(t.second(), t.third(), urlBalance.get(t.third())))
				.collect(Collectors.toSet());
	}
	
	private List<Triple<Integer, Line, String>> merge(List<Triple<Integer, Line, String>> left,
			List<Triple<Integer, Line, String>> right) {
		if (left.isEmpty()) {
			return right;
		}
		if (right.isEmpty()) {
			return left;
		}
		final int base = Math.max(left.size(), right.size());
		List<Triple<Integer, Line, String>> out = new ArrayList<>();
		int l = 0;
		int r = 0;
		while (l < left.size() && r < right.size()) {
			Triple<Integer, Line, String> a = left.get(l);
			Triple<Integer, Line, String> b = right.get(r);
			a.setFirst(a.first() + base);
			b.setFirst(b.first() + base);
			int c = a.second().compareTo(b.second());
			if (c < 0) { // left[l][Line] < right[r][Line]
				out.add(a);
				l++;
			} else if (c > 0) { // left[l][Line] > right[r][Line]
				out.add(b);
				r++;
			} else { // left[l][Line] = right[r][Line]
				out.add(a);
				out.add(b);
				l++;
				r++;
			}
		}
		while (l < left.size()) {
			out.add(left.get(l++));
		}
		while (r < right.size()) {
			out.add(right.get(r++));
		}
		return out;
	}
	
	
}

