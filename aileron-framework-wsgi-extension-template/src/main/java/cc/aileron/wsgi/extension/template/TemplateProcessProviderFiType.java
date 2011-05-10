/**
 *
 */
package cc.aileron.wsgi.extension.template;

/**
 * @author aileron
 */
public interface TemplateProcessProviderFiType
{
    /**
     * @param contentType
     * @return {@link TemplateProcessProviderFiPath}
     */
    TemplateProcessProviderFiPath type(String contentType);
}
