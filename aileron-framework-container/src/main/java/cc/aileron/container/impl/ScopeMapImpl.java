/**
 *
 */
package cc.aileron.container.impl;

import java.lang.annotation.Annotation;
import java.util.HashMap;

import cc.aileron.container.model.Scope;
import cc.aileron.container.model.ScopeMap;

/**
 * @author aileron
 */
public class ScopeMapImpl implements ScopeMap
{
    @Override
    public Scope get(final Class<? extends Annotation> annotation)
    {
        return map.get(annotation);
    }

    @Override
    public void put(final Class<? extends Annotation> annotation,
            final Scope scope)
    {
        map.put(annotation, scope);
    }

    private final HashMap<Class<? extends Annotation>, Scope> map = new HashMap<Class<? extends Annotation>, Scope>();
}
