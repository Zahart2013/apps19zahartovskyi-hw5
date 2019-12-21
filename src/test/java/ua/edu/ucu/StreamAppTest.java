package ua.edu.ucu;

import ua.edu.ucu.stream.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author andrii
 */
public class StreamAppTest {
    
    private IntStream intStream;

    @Before
    public void init() {
        int[] intArr = {-1, 0, 1, 2, 3};
        intStream = AsIntStream.of(intArr);
    }
    
    @Test
    public void testStreamOperations() {
        System.out.println("streamOperations");
        int expResult = 42;
        int result = StreamApp.streamOperations(intStream);
        assertEquals(expResult, result);        
    }

    @Test
    public void testStreamToArray() {
        System.out.println("streamToArray");
        int[] expResult = {-1, 0, 1, 2, 3};
        int[] result = StreamApp.streamToArray(intStream);
        assertArrayEquals(expResult, result);        
    }

    @Test
    public void testStreamForEach() {
        System.out.println("streamForEach");
        String expResult = "-10123";
        String result = StreamApp.streamForEach(intStream);
        assertEquals(expResult, result);        
    }

    @Test
    public void testStreamMath() {
        System.out.println("streamMath");
        Integer max = intStream.max();
        Integer min = intStream.min();
        Integer sum = intStream.sum();
        Double average = intStream.average();
        long size = intStream.count();
        Integer expectedMin = -1;
        Integer expectedMax = 3;
        Integer expectedSum = 5;
        Double expectedAverage = (double) 1;
        long expectdSize = 5;
        assertEquals(expectedMax, max);
        assertEquals(expectedMin, min);
        assertEquals(expectedSum, sum);
        assertEquals(expectdSize, size);
        assertEquals(expectedAverage, average);
    }
    
}
