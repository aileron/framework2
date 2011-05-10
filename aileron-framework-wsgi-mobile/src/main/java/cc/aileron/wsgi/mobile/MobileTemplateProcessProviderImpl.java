/**
 *
 */
package cc.aileron.wsgi.mobile;

import static cc.aileron.commons.util.ResourceUtils.*;
import static cc.aileron.wsgi.context.WsgiContextProvider.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.EnumMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.generic.util.Katakana;
import cc.aileron.template.Template;
import cc.aileron.template.context.TemplateContext;
import cc.aileron.template.flow.FlowMethodNotFoundError;
import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowProcess;
import cc.aileron.workflow.environment.WorkflowEnvironment;
import cc.aileron.wsgi.context.WsgiContext;
import cc.aileron.wsgi.context.WsgiContextProvider;
import cc.aileron.wsgi.extension.template.TemplateResponse;
import cc.aileron.wsgi.extension.template.TemplateResponseFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.Stage;

/**
 * @author aileron
 */
@Singleton
public class MobileTemplateProcessProviderImpl implements
        MobileTemplateProcessProvider
{
    @Override
    public <Resource> WorkflowProcess<Resource> get(final Object global,
            final String path, final String csspath) throws Exception
    {
        final String debug = "template:" + path;

        final Charset encode = this.environment.getEncode();

        final Provider<EnumMap<MobileCarrier, Template>> p;
        if (this.isDevelopment == false)
        {
            final String htmlstrings = resource(path).toString();
            final CssSelectorProperties css = cssPropertiesFactory.parse(resource(csspath).toString());
            final EnumMap<MobileCarrier, Template> mb = mobileTemplateManager.get(htmlstrings,
                    css);
            p = new Provider<EnumMap<MobileCarrier, Template>>()
            {
                @Override
                public EnumMap<MobileCarrier, Template> get()
                {
                    return mb;
                }
            };
        }
        else
        {
            p = new Provider<EnumMap<MobileCarrier, Template>>()
            {
                @Override
                public EnumMap<MobileCarrier, Template> get()
                {
                    try
                    {
                        final String htmlstrings = resource(path).toString();
                        final CssSelectorProperties css = cssPropertiesFactory.parse(resource(csspath).toString());
                        final EnumMap<MobileCarrier, Template> mb = mobileTemplateManager.get(htmlstrings,
                                css);
                        return mb;
                    }
                    catch (final Exception e)
                    {
                        throw new Error(e);
                    }
                }
            };
        }

        return new WorkflowProcess<Resource>()
        {
            @Override
            public void doProcess(final WorkflowActivity<Resource> activity)
                    throws Exception
            {
                final String userAgent = WsgiContextProvider.context()
                        .request()
                        .getHeader("User-Agent");
                final MobileCarrier carrier = MobileCarrier.parseUserAgent(userAgent);
                final TemplateResponse<Resource> response = responseFactory.get(global,
                        carrier.contentType(),
                        encode);
                context().request()
                        .setAttribute(MobileCarrier.class.getSimpleName(),
                                carrier);

                final Template template = new Template()
                {
                    @Override
                    public TemplateContext print(final PrintWriter writer,
                            final PojoAccessor<?>... accessor)
                            throws FlowMethodNotFoundError,
                            PojoAccessorValueNotFoundException,
                            PojoPropertiesNotFoundException
                    {
                        final StringWriter sw = new StringWriter();
                        final TemplateContext result = org.print(new PrintWriter(sw),
                                accessor);

                        writer.print(Katakana.fullwidth2halfwidth(sw.toString()));
                        writer.flush();

                        return result;
                    }

                    final Template org = p.get().get(carrier);
                };
                response.setTemplate(template);
                response.doResponse(activity);
            }

            @Override
            public String toString()
            {
                return debug;
            }
        };
    }

    @Override
    public <Resource> WorkflowProcess<Resource> get(final String path,
            final String csspath) throws Exception
    {
        return get(null, path, csspath);
    }

    /**
     * @param activity
     * @return accessor
     */
    PojoAccessor<?> getAccessor(final WorkflowActivity<?> activity)
    {
        return activity.resourceAccessor().mixin(new Object()
        {
            @SuppressWarnings("unused")
            public final WsgiContext context = WsgiContextProvider.context();
        });
    }

    /**
     * @param stage
     * @param responseFactory
     * @param environment
     * @param cssPropertiesFactory
     * @param mobileTemplateManager
     */
    @Inject
    public MobileTemplateProcessProviderImpl(final Stage stage,
            final TemplateResponseFactory responseFactory,
            final WorkflowEnvironment environment,
            final CssSelectorPropertiesFactory cssPropertiesFactory,
            final MobileHtmlTemplateManager mobileTemplateManager)
    {
        this.cssPropertiesFactory = cssPropertiesFactory;
        this.mobileTemplateManager = mobileTemplateManager;
        this.isDevelopment = stage == Stage.DEVELOPMENT;
        this.responseFactory = responseFactory;
        this.environment = environment;
    }

    final CssSelectorPropertiesFactory cssPropertiesFactory;

    /**
     * logger
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * mobileTemplateManager
     */
    final MobileHtmlTemplateManager mobileTemplateManager;

    /**
     * responseFactory
     */
    final TemplateResponseFactory responseFactory;

    /**
     * 環境変数
     */
    private final WorkflowEnvironment environment;

    /**
     * stage
     */
    private final boolean isDevelopment;
}