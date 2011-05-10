/**
 * 
 */
package cc.aileron.report.smtp;

import static cc.aileron.commons.util.ResourceUtils.*;
import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.commons.util.ResourceUtils;

/**
 * @author aileron
 */
public class ReportSmtpTemplateHandlerClassBase implements
        ReportSmtpTemplateHandler<Class<?>>
{
    @Override
    public String get(final Class<?> resource) throws ResourceNotFoundException
    {
        final String className = resource.getSimpleName();
        final String templateFileName = packageNameToDirectoryName(resource.getPackage())
                + "/"
                + className.substring(0, 1).toLowerCase()
                + className.substring(1, className.length()) + ".txt";
        return ResourceUtils.resource(templateFileName).toString();
    }
}
