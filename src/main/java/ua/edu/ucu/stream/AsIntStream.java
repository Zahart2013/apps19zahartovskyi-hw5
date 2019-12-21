package ua.edu.ucu.stream;

import ua.edu.ucu.function.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class AsIntStream implements IntStream {
    private Iterator<Integer> stream;

    public AsIntStream() {
    }

    private AsIntStream(Integer[] values) {
        this.stream = new StreamIter(values);
    }

    public static IntStream of(int... values) {
        Integer[] ints = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            ints[i] = values[i];
        }
        return new AsIntStream(ints);
    }

    @Override
    public Double average() {
        return (double) this.sum() / this.count();
    }

    @Override
    public Integer max() {
        if (this.count() == 0) {
            throw new IllegalArgumentException("Empty array.");
        }
        Iterable<Integer> iter = () -> stream;
        Integer max = null;
        for (Integer i : iter) {
            if (max == null) {
                max = i;
            } else if (max < i) {
                max = i;
            }
        }
        return max;
    }

    @Override
    public Integer min() {
        if (this.count() == 0) {
            throw new IllegalArgumentException("Empty array.");
        }
        Iterable<Integer> iter = () -> stream;
        Integer min = null;
        for (Integer i : iter) {
            if (min == null) {
                min = i;
            } else if (min > i) {
                min = i;
            }
        }
        return min;
    }

    @Override
    public long count() {
        long counter = 0;
        while (stream.hasNext()) {
            stream.next();
            counter++;
        }
        return counter;
    }

    @Override
    public Integer sum() {
        if (this.count() == 0) {
            throw new IllegalArgumentException("Empty array.");
        }
        Iterable<Integer> iter = () -> stream;
        Integer sum = 0;
        for (Integer i : iter) {
            sum += i;
        }
        return sum;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        AsIntStream newStream = new AsIntStream();
        newStream.setStream(new StreamFilter(stream, predicate));
        return newStream;
    }

    @Override
    public void forEach(IntConsumer action) {
        Iterable<Integer> iter = () -> stream;
        for (Integer i : iter) {
            action.accept(i);
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        AsIntStream newStream = new AsIntStream();
        newStream.setStream(new StreamMap(stream, mapper));
        return newStream;
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        AsIntStream newStream = new AsIntStream();
        newStream.setStream(new StreamFlatMap(stream, func));
        return newStream;
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        Iterable<Integer> iter = () -> stream;
        int result = identity;
        for (Integer i : iter) {
            result = op.apply(result, i);
        }
        return result;
    }

    @Override
    public int[] toArray() {
        int[] result = new int[(int) this.count()];
        Iterable<Integer> iter = () -> stream;
        int index = 0;
        for (Integer i : iter) {
            result[index] = i;
            index++;
        }
        return result;
    }

    protected Iterator<Integer> getStream() {
        return stream;
    }

    protected void setStream(Iterator<Integer> stream) {
        this.stream = stream;
    }

    private static class StreamIter implements Iterator<Integer> {
        private Integer[] ints;
        private int index;

        private StreamIter(Integer[] ints) {
            this.ints = ints;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            if (index < ints.length) {
                return true;
            }
            reset();
            return false;
        }

        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Integer next = ints[index];
            index += 1;
            return next;
        }

        public void reset() {
            index = 0;
        }
    }

    private static class StreamFilter implements Iterator<Integer> {
        private Iterator<Integer> iter;
        private IntPredicate action;
        private Integer currentInt;

        private StreamFilter(Iterator<Integer> iter, IntPredicate action) {
            this.iter = iter;
            this.action = action;
        }

        @Override
        public boolean hasNext() {
            while (iter.hasNext()) {
                currentInt = iter.next();
                if (action.test(currentInt)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Integer next() {
            return currentInt;
        }
    }

    private static class StreamMap implements Iterator<Integer> {
        private Iterator<Integer> iter;
        private IntUnaryOperator operator;

        private StreamMap(Iterator<Integer> iter,
                          IntUnaryOperator operator) {
            this.iter = iter;
            this.operator = operator;
        }

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public Integer next() {
            return operator.apply(iter.next());
        }
    }

    private static class StreamFlatMap implements Iterator<Integer> {
        private Iterator<Integer> iter;
        private IntToIntStreamFunction operator;
        private AsIntStream currentStream;

        private StreamFlatMap(Iterator<Integer> iter,
                              IntToIntStreamFunction operator) {
            this.iter = iter;
            this.operator = operator;
        }


        @Override
        public boolean hasNext() {
            if (currentStream == null
                    || !currentStream.getStream().hasNext()) {
                currentStream = null;
                return iter.hasNext();
            }
            return true;
        }

        @Override
        public Integer next() {
            if (currentStream == null) {
                currentStream =
                        (AsIntStream) operator.applyAsIntStream(iter.next());
            }
            return currentStream.getStream().next();
        }


    }

}
