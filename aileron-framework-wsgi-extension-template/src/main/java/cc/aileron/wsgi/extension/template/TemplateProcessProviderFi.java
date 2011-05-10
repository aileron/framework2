/**
 *
 */
package cc.aileron.wsgi.extension.template;

import com.google.inject.ImplementedBy;

/**
 * 流れる様なインタフェースで、テンプレートプロセスを取得する為のインタフェース
 * 
 * @author aileron
 */
@ImplementedBy(TemplateProcessProviderFiImpl.class)
public interface TemplateProcessProviderFi
{
    /**
     * @return {@link TemplateProcessProviderFiCategory}
     */
    TemplateProcessProviderFiCategory context();

    /**
     * @param globalcontext
     * @return {@link TemplateProcessProviderFiCategory}
     */
    TemplateProcessProviderFiCategory context(Object globalcontext);
}
