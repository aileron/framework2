/**
 *
 */
package cc.aileron.wsgi.extension.template;

import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.template.TemplateCategory;
import cc.aileron.template.parser.ParserMethodNotFoundException;
import cc.aileron.template.reader.TemplateSyntaxEexception;
import cc.aileron.workflow.WorkflowProcess;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author aileron
 */
@Singleton
public class TemplateProcessProviderFiImpl implements TemplateProcessProviderFi
{
    @Override
    public TemplateProcessProviderFiCategory context()
    {
        return context(null);
    }

    @Override
    public TemplateProcessProviderFiCategory context(final Object globalcontext)
    {
        return new TemplateProcessProviderFiCategory()
        {
            @Override
            public TemplateProcessProviderFiType category(
                    final TemplateCategory category)
            {
                return new TemplateProcessProviderFiType()
                {
                    @Override
                    public TemplateProcessProviderFiPath type(
                            final String contentType)
                    {
                        return new TemplateProcessProviderFiPath()
                        {
                            @Override
                            public WorkflowProcess<Object> path(
                                    final String path)
                                    throws TemplateSyntaxEexception,
                                    ParserMethodNotFoundException,
                                    ResourceNotFoundException
                            {
                                return template.get(globalcontext,
                                        path,
                                        contentType,
                                        category);
                            }
                        };
                    }
                };
            }
        };
    }

    /**
     * @param template
     */
    @Inject
    public TemplateProcessProviderFiImpl(final TemplateProcessProvider template)
    {
        this.template = template;
    }

    final TemplateProcessProvider template;
}
