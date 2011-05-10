package cc.aileron.wsgi.extension.template;

import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.template.TemplateCategory;
import cc.aileron.template.parser.ParserMethodNotFoundException;
import cc.aileron.template.reader.TemplateSyntaxEexception;
import cc.aileron.workflow.WorkflowProcess;

import com.google.inject.ImplementedBy;

/**
 * テンプレートプロセス用のプロバイダ
 * 
 * 使用するには、cc.aileron.wsgi.extension.TemplateProcessModule が install されている必要が有る。
 * 
 * @author Aileron
 */
@ImplementedBy(TemplateProcessProviderImpl.class)
public interface TemplateProcessProvider
{
    /**
     * @param <Resource>
     * @param global
     * @param path
     * @param contentType
     * @param templateCategory
     * @return wsgiProcess
     * @throws TemplateSyntaxEexception
     * @throws ParserMethodNotFoundException
     * @throws ResourceNotFoundException
     */
    <Resource> WorkflowProcess<Resource> get(Object global, final String path,
            final String contentType, TemplateCategory templateCategory)
            throws TemplateSyntaxEexception, ParserMethodNotFoundException,
            ResourceNotFoundException;

    /**
     * @param <Resource>
     * @param path
     * @param contentType
     * @param templateCategory
     * @return wsgiProcess
     * @throws TemplateSyntaxEexception
     * @throws ParserMethodNotFoundException
     * @throws ResourceNotFoundException
     */
    <Resource> WorkflowProcess<Resource> get(final String path,
            final String contentType, TemplateCategory templateCategory)
            throws TemplateSyntaxEexception, ParserMethodNotFoundException,
            ResourceNotFoundException;

}