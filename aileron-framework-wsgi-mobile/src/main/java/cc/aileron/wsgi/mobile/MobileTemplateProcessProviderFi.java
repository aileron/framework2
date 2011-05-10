/**
 * 
 */
package cc.aileron.wsgi.mobile;

import cc.aileron.workflow.WorkflowProcess;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author aileron
 */
@ImplementedBy(MobileTemplateProcessProviderFi.Impl.class)
public interface MobileTemplateProcessProviderFi
{
    /**
     * @author aileron
     */
    interface Css
    {
        Path css(String css);
    }

    /**
     * @author aileron
     */
    @Singleton
    class Impl implements MobileTemplateProcessProviderFi
    {

        @Override
        public Css context()
        {
            return context(null);
        }

        @Override
        public Css context(final Object global)
        {
            return new Css()
            {
                @Override
                public Path css(final String css)
                {
                    return new Path()
                    {
                        @Override
                        public WorkflowProcess<Object> path(final String path)
                                throws Exception
                        {
                            return provider.get(global, path, css);
                        }
                    };
                }
            };
        }

        /**
         * @param provider
         */
        @Inject
        public Impl(final MobileTemplateProcessProvider provider)
        {
            this.provider = provider;
        }

        final MobileTemplateProcessProvider provider;
    }

    /**
     * @author aileron
     */
    interface Path
    {
        WorkflowProcess<Object> path(String path) throws Exception;
    }

    /**
     * @return {@link Path}
     */
    Css context();

    /**
     * @param global
     * @return {@link Path}
     */
    Css context(Object global);
}
