package edu.utdallas.kwicsystem;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Ali, Dongchen, and Haoliang (ADH Team)
 */
public class Alphabetizer {
    private List<Line> sortedItems;
    private int size;
	
    public Alphabetizer(LinesBatch lines) {
        size = lines.size();
        sortedItems = lines.stream().sorted().collect(Collectors.toList());
    }
    
    public Line getAt(int index) {
        return sortedItems.get(index);
    }
    
    public int itemsCount() {
        return size;
    }
    
    public List<Line> items() {
    	return sortedItems;
    }
}
