/**
 *
 */
package cc.aileron.wsgi.extension.template;

import cc.aileron.template.TemplateCategory;

/**
 * @author aileron
 */
public interface TemplateProcessProviderFiCategory
{

    /**
     * @param category
     * @return TemplateProcessProviderFiType
     */
    TemplateProcessProviderFiType category(TemplateCategory category);
}
