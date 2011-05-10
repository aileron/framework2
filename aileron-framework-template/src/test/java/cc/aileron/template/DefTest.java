package cc.aileron.template;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorManager;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.junit.runner.guice.GuiceInjectRunner;
import cc.aileron.junit.runner.guice.GuiceInjectRunnerModule;
import cc.aileron.template.flow.FlowMethodNotFoundError;
import cc.aileron.template.parser.ParserMethodNotFoundException;
import cc.aileron.template.reader.TemplateSyntaxEexception;

import com.google.inject.Inject;

/**
 * @author aileron
 */
@RunWith(GuiceInjectRunner.class)
@GuiceInjectRunnerModule(XmlSampleModule.class)
public class DefTest
{
    public static enum Category
    {
        A, B, C;
    }

    /**
     * @param name
     * @return object
     */
    public Object get(final String name)
    {
        return Category.A;
    }

    /**
     * spec
     * @throws PojoPropertiesNotFoundException 
     * @throws PojoAccessorValueNotFoundException 
     * @throws FlowMethodNotFoundError 
     */
    @Test
    public void spec()
            throws FlowMethodNotFoundError, PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final StringWriter sw = new StringWriter();
        final PrintWriter writer = new PrintWriter(sw);
        template.print(writer, accessor);

        assertThat(sw.toString(), is("hello"));
    }

    /**
     * @param manager
     * @param compiler
     * @throws ParserMethodNotFoundException 
     * @throws TemplateSyntaxEexception 
     */
    @Inject
    public DefTest(final PojoAccessorManager manager,
            final TemplateCompiler compiler) throws TemplateSyntaxEexception,
            ParserMethodNotFoundException
    {
        template = compiler.compile("<!--#{def AAA @ A}#-->hello<!--#-->");
        accessor = manager.from(this);
    }

    private final PojoAccessor<DefTest> accessor;
    private final Template template;
}
