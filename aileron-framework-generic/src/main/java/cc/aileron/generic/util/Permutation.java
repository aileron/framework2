/**
 * 
 */
package cc.aileron.generic.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author aileron
 * @param <T> 
 */
public class Permutation<T>
{
    /**
     * @param seed
     * @return choice
     */
    public List<T> choice(final int seed)
    {
        final ArrayList<T> result = new ArrayList<T>();
        for (final int i : permutation.get(seed % seedSize))
        {
            result.add(set.get(i));
        }
        return result;
    }

    /**
     * @param size
     * @return seedSize
     */
    private int seedSize(final int size)
    {
        int number = size;
        int factrial = number--;
        while (number > 0)
        {
            factrial = factrial * number--;
        }
        return factrial;
    }

    /**
     * @param set 
     */
    public Permutation(final List<T> set)
    {
        this.set = set;
        final int size = set.size();
        this.seedSize = seedSize(size);
        this.permutation = new ArrayList<int[]>(seedSize);
        final int[] c = new int[size + 1];
        for (int i = 0; i <= size; i++)
        {
            c[i] = i;
        }
        final int[] objs = new int[size];
        for (int i = 0; i < size; i++)
        {
            objs[i] = i;
        }
        int k = 1;
        while (k < size)
        {
            int i = 0;
            if ((k & 1) != 0)
            {
                i = c[k];
            }
            final int tmp = objs[k];
            objs[k] = objs[i];
            objs[i] = tmp;

            k = 1;
            while (c[k] == 0)
            {
                c[k] = k++;
            }
            c[k]--;
            permutation.add(objs.clone());
        }
    }

    /**
     * @param set 
     */
    public Permutation(final T[] set)
    {
        this(Arrays.asList(set));
    }

    private final ArrayList<int[]> permutation;
    private final int seedSize;
    private final List<T> set;
}