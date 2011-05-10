/**
 *
 */
package cc.aileron.workflow.container.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.aileron.generic.util.SkipList;
import cc.aileron.workflow.WorkflowMethod;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author aileron
 */
@Singleton
public class WorkflowTreeContainerImpl implements WorkflowTreeContainer
{
    static class Tree
    {
        public Tree get(final String name)
        {
            return tree.get(name);
        }

        /**
         * @return is dynmaic
         */
        public boolean isDynmaic()
        {
            return this.varnames != null;
        }

        public Map<WorkflowMethod, Integer> map(final String ext)
        {
            return map.get(ext);
        }

        public void put(final String name, final Tree ch)
        {
            tree.put(name, ch);
        }

        /**
         */
        public Tree()
        {
            this.varnames = null;
        }

        /**
         * @param varnames
         */
        public Tree(final List<String> varnames)
        {
            this.varnames = varnames;
        }

        /**
         * この階層に所属させているコンテンツ
         */
        final Map<String, Map<WorkflowMethod, Integer>> map = new HashMap<String, Map<WorkflowMethod, Integer>>();

        /**
         * overrideKey-map
         */
        final Map<String, Integer> overrideKeyMap = new HashMap<String, Integer>();

        /**
         * 下階層
         */
        final Map<String, Tree> tree = new HashMap<String, Tree>();

        /**
         * 動的Urlパラメータキー
         */
        final List<String> varnames;
    }

    /**
     * @author aileron
     */
    static class UriAndExt
    {
        /**
         * @param rawuri 
         */
        public UriAndExt(final String rawuri)
        {
            final int exti = rawuri.lastIndexOf('.');
            final String uri, ext;
            if (exti == -1)
            {
                uri = rawuri;
                ext = "";
            }
            else
            {
                uri = rawuri.substring(0, exti);
                ext = rawuri.substring(exti + 1, rawuri.length());
            }
            this.uri = uri;
            this.ext = ext;
        }

        final String ext;

        final String uri;
    }

    @Override
    public Map<String, Integer> all()
    {
        final HashMap<String, Integer> result = new HashMap<String, Integer>();
        new Object()
        {
            private void each(final String base, final Tree parent)
            {
                for (final Entry<String, Map<WorkflowMethod, Integer>> e : parent.map.entrySet())
                {
                    for (final Entry<WorkflowMethod, Integer> ee : e.getValue()
                            .entrySet())
                    {
                        final WorkflowMethod method = ee.getKey();
                        final String q = method + "\t" + base;
                        result.put(q, ee.getValue());
                    }
                }
                for (final Entry<String, Tree> e : parent.tree.entrySet())
                {
                    each(base + "/" + e.getKey(), e.getValue());
                }
            }

            {
                each("", parent);
            }
        };
        return result;
    }

    @Override
    public int get(final String rawuri, final WorkflowMethod method,
            final Map<String, Object> uriparameter,
            final Set<String> requestParameter)
    {
        final String uri, ext;
        {
            final UriAndExt tmp = new UriAndExt(rawuri);
            uri = tmp.uri;
            ext = tmp.ext;
        }
        final String[] uriTokens = uri.split(delimiter);
        final List<String> uriValues = new SkipList<String>();
        final int size = uriTokens.length;
        Tree tree = parent;
        int i = 0;
        while (tree != null && i < size)
        {
            final String uriToken = uriTokens[i++];
            final Tree ch = tree.get(uriToken);
            if (ch != null)
            {
                tree = ch;
            }
            else
            {
                uriValues.add(uriToken);
                tree = tree.get("*");
            }
        }
        if (tree == null)
        {
            return 0;
        }

        int id = 0;
        if (!tree.overrideKeyMap.isEmpty())
        { // メソッドオーバライドが設定されている場合

            for (final Entry<String, Integer> e : tree.overrideKeyMap.entrySet())
            {
                if (requestParameter.contains(e.getKey()))
                {
                    id = e.getValue();
                    break;
                }
            }
        }
        if (id == 0)
        { // メソッドオーバライドが設定されていない場合か オーバライド先に存在しなかった場合

            final Map<WorkflowMethod, Integer> e = tree.map(ext);
            if (e == null)
            {
                return 0;
            }
            final Integer val = e.get(method);
            id = val == null ? 0 : val;
        }

        /*
         * url parameters
         */
        final List<String> varnames = tree.varnames;
        for (int ui = 0, usize = uriValues.size(); ui < usize; ui++)
        {
            final String key = varnames.get(ui), value = uriValues.get(ui);
            uriparameter.put(key, value);
        }

        return id;
    }

    @Override
    public void put(final String uri, final WorkflowMethod method, final int id)
    {
        put(uri, method, null, id);
    }

    @Override
    public void put(final String rawuri, final WorkflowMethod method,
            final String overrideKey, final int id)
    {
        final String uri, ext;
        {
            final UriAndExt tmp = new UriAndExt(rawuri);
            uri = tmp.uri;
            ext = tmp.ext;
        }
        final String[] uriTokens = uri.split(delimiter);
        final int size = uriTokens.length;

        final List<String> varnames = new SkipList<String>();

        Tree tree = parent;
        int i = 0;
        while (i < size)
        {
            final String uriToken = uriTokens[i++];
            final Tree ch;
            final Matcher matcher = pattern.matcher(uriToken);
            final boolean dynmaic = matcher.find();
            if (dynmaic)
            {
                varnames.add(matcher.group(1));
                ch = tree.get("*");
            }
            else
            {
                ch = tree.get(uriToken);
            }

            if (ch != null)
            {
                tree = ch;
            }
            else
            {
                final Tree newch = new Tree(varnames);
                final String treeName = dynmaic ? "*" : uriToken;
                tree.put(treeName, newch);
                tree = newch;
            }
        }

        if (overrideKey == null || overrideKey.isEmpty())
        { // メソッドオーバライドしない場合
            final Map<WorkflowMethod, Integer> e = tree.map(ext);
            if (e != null)
            {
                e.put(method, id);
            }
            else
            {
                final HashMap<WorkflowMethod, Integer> ne = new HashMap<WorkflowMethod, Integer>();
                ne.put(method, id);
                tree.map.put(ext, ne);
            }
        }
        else
        { // メソッドオーバライド
            tree.overrideKeyMap.put(overrideKey, id);
        }
    }

    /**
     * @param delimiter
     */
    @Inject
    public WorkflowTreeContainerImpl(
            final WorkflowTreeContainerDelimiter delimiter)
    {
        this.delimiter = delimiter.value();
    }

    final String delimiter;
    final Tree parent = new Tree();
    final Pattern pattern = Pattern.compile(Pattern.quote("${") + "(.*?)"
            + Pattern.quote("}"));
}
