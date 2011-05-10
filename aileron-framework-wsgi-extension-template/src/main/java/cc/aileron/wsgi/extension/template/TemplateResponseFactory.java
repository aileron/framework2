package cc.aileron.wsgi.extension.template;

import java.nio.charset.Charset;

import com.google.inject.ImplementedBy;

/**
 * @author Aileron
 */
@ImplementedBy(TemplateResponseFactoryImpl.class)
public interface TemplateResponseFactory
{
    /**
     * @param <Resource>
     * @param global
     * @param contentType
     * @param charset
     * @return response
     */
    <Resource> TemplateResponse<Resource> get(Object global,
            final String contentType, final Charset charset);

}