/**
 * 
 */
package cc.aileron.report;

import cc.aileron.report.smtp.ReportSmtpManager;
import cc.aileron.template.Template;

import com.google.inject.ImplementedBy;

/**
 * @author aileron
 */
@ImplementedBy(ReportSmtpManager.class)
public interface ReportManager
{
    /**
     * @param <T>
     * @param template 
     * @param resource 
     * @return {@link Report}
     */
    <T> Report<T> from(Template template);
}
