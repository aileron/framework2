/**
 *
 */
package cc.aileron.wsgi.mobile;

import java.util.EnumMap;

import cc.aileron.template.Template;

import com.google.inject.ImplementedBy;

/**
 * @author aileron
 */
@ImplementedBy(MobileHtmlTemplateManagerImpl.class)
public interface MobileHtmlTemplateManager
{
    /**
     * @param templateCompiler
     * @param carrier
     * @param plaintemplate
     * @param css
     * @return Template
     * @throws Exception
     */
    EnumMap<MobileCarrier, Template> get(String plaintemplate,
            CssSelectorProperties css) throws Exception;
}
