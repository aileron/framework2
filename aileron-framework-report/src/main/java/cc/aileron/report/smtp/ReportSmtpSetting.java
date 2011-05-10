/**
 * 
 */
package cc.aileron.report.smtp;

import java.util.Properties;

import javax.mail.Authenticator;

/**
 * @author aileron
 */
public class ReportSmtpSetting
{
    /**
     * @param properties
     */
    public ReportSmtpSetting(final Properties properties)
    {
        this.authenticator = null;
        this.properties = properties;
    }

    /**
     * @param authenticator
     * @param properties
     */
    public ReportSmtpSetting(final Properties properties,
            final Authenticator authenticator)
    {
        this.authenticator = authenticator;
        this.properties = properties;
    }

    /**
     * authenticator
     */
    public final Authenticator authenticator;

    /**
     * properties
     */
    public final Properties properties;
}
