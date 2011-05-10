/**
 * Copyright (C) 2009 aileron.cc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.aileron.generic.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * python の Range および XRange 実装
 * 
 * @author aileron
 */
public class XRange implements Iterable<Integer>
{
    /**
     * XRangeIterator
     * 
     * @author Aileron
     * 
     */
    private static class XRangeIterator implements Iterator<Integer>
    {
        /*
         * (非 Javadoc)
         * 
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext()
        {
            if (step > 0)
            {
                return current < stop;
            }
            return current > stop;
        }

        /*
         * (非 Javadoc)
         * 
         * @see java.util.Iterator#next()
         */
        @Override
        public Integer next()
        {
            final int v = current;
            current += step;
            return v;
        }

        /*
         * (非 Javadoc)
         * 
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        /**
         * 
         * @param x
         */
        public XRangeIterator(final XRange x)
        {
            if (x.step == 0)
            {
                throw new IllegalArgumentException("step must be non-zero");
            }

            this.start = x.start;
            this.stop = x.stop;
            this.step = x.step;
            this.current = this.start;
        }

        private int current;

        private final int start;

        private final int step;

        private final int stop;
    }

    /**
     * @param from
     * @param to
     * @return {@link XRange}
     */
    public static XRange fromTo(final int from, final int to)
    {
        return from <= to ? new XRange(from, to + 1, 1) : new XRange(from,
                to - 1,
                -1);
    }

    /**
     * 
     * @param stop
     * @return {@link XRange}
     */
    public static XRange xrange(final int stop)
    {
        return new XRange(0, stop, 1);
    }

    /**
     * 
     * @param start
     * @param stop
     * @return {@link XRange}
     */
    public static XRange xrange(final int start, final int stop)
    {
        return start <= stop ? new XRange(start, stop, 1) : new XRange(start,
                stop,
                -1);
    }

    /**
     * 
     * @param start
     * @param stop
     * @param step
     * @return {@link XRange}
     */
    public static XRange xrange(final int start, final int stop, final int step)
    {
        return new XRange(start, stop, step);
    }

    /**
     * @return iterator
     */
    @Override
    public Iterator<Integer> iterator()
    {
        return new XRangeIterator(this);
    }

    /**
     * @return array
     */
    public int[] toArray()
    {
        final int size = Math.max((stop - start) / step, 0);
        final int[] array = new int[size];
        int i = start;
        for (final int v : this)
        {
            array[i++] = v;
        }
        return array;
    }

    /**
     * @return list
     */
    public List<Integer> toList()
    {
        final ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = start; i < stop; i += step)
        {
            list.add(i);
        }
        return list;
    }

    /**
     * 
     * @param start
     * @param stop
     * @param step
     */
    private XRange(final int start, final int stop, final int step)
    {
        this.start = start;
        this.stop = stop;
        this.step = step;
    }

    /**
     * start
     */
    final int start;

    /**
     * step
     */
    final int step;

    /**
     * stop
     */
    final int stop;
}