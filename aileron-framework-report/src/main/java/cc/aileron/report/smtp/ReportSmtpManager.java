/**
 * 
 */
package cc.aileron.report.smtp;

import cc.aileron.accessor.PojoAccessorManager;
import cc.aileron.report.Report;
import cc.aileron.report.ReportManager;
import cc.aileron.template.Template;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author aileron
 */
@Singleton
public class ReportSmtpManager implements ReportManager
{
    @Override
    public <T> Report<T> from(final Template template)
    {
        try
        {
            return new ReportSmtp<T>(pojoAccessorManager, setting, template);
        }
        catch (final Exception e)
        {
            throw new Error(e);
        }
    }

    /**
     * @param pojoAccessorManager 
     * @param setting 
     */
    @Inject
    public ReportSmtpManager(final PojoAccessorManager pojoAccessorManager,
            final ReportSmtpSetting setting)
    {
        this.pojoAccessorManager = pojoAccessorManager;
        this.setting = setting;
    }

    private final PojoAccessorManager pojoAccessorManager;
    private final ReportSmtpSetting setting;
}
