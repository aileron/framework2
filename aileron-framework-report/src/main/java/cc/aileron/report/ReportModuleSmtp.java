/**
 *
 */
package cc.aileron.report;

import java.util.Properties;

import javax.mail.Authenticator;

import cc.aileron.report.smtp.ReportSmtpManager;
import cc.aileron.report.smtp.ReportSmtpSetting;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * @author aileron
 */
public class ReportModuleSmtp implements Module
{
    @Override
    public void configure(final Binder binder)
    {
        binder.bind(ReportSmtpSetting.class).toInstance(setting);
        binder.bind(ReportManager.class).to(ReportSmtpManager.class);
    }

    /**
     * @param properties
     */
    public ReportModuleSmtp(final Properties properties)
    {
        this.setting = new ReportSmtpSetting(properties);
    }

    /**
     * @param properties
     * @param authenticator
     */
    public ReportModuleSmtp(final Properties properties,
            final Authenticator authenticator)
    {
        this.setting = new ReportSmtpSetting(properties, authenticator);
    }

    private final ReportSmtpSetting setting;
}
