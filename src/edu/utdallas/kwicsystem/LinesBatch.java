package edu.utdallas.kwicsystem;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 *
 * @author Ali, Dongchen, and Haoliang (ADH Team)
 */
public class LinesBatch {
    protected Line[] lines;
    
    public LinesBatch(Line[] lines) {
        this.lines = lines;
    }
    
    public Line getLine(int index) {
        return lines[index];
    }
    
    public int size() {
        return lines.length;
    }
    
    public Stream<Line> stream() {
        return Arrays.stream(lines);
    }
}
