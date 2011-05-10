/**
 *
 */
package cc.aileron.wsgi.extension.template;

import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.template.parser.ParserMethodNotFoundException;
import cc.aileron.template.reader.TemplateSyntaxEexception;
import cc.aileron.workflow.WorkflowProcess;

/**
 * @author aileron
 */
public interface TemplateProcessProviderFiPath
{
    /**
     * @param path
     * @return {@link WorkflowProcess}
     * @throws ResourceNotFoundException
     * @throws ParserMethodNotFoundException
     * @throws TemplateSyntaxEexception
     */
    WorkflowProcess<Object> path(String path)
            throws TemplateSyntaxEexception, ParserMethodNotFoundException,
            ResourceNotFoundException;
}
