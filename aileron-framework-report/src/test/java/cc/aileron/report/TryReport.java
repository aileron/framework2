/**
 * 
 */
package cc.aileron.report;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import cc.aileron.template.Template;
import cc.aileron.template.TemplateCategory;
import cc.aileron.template.TemplateCompiler;
import cc.aileron.template.TemplateModule;

import com.google.inject.Guice;

/**
 * @author aileron
 */
public class TryReport
{
    /**
     * @param args
     * @throws Exception 
     */
    public static void main(final String... args) throws Exception
    {
        final String username = "test@aileron.cc";
        final String password = "437394c806b5598aab4cccfe99ceb4e9";
        final String from = "test@aileron.cc";
        final String to = "0nv4yv00326207y@ezweb.ne.jp";

        final Template template = Guice.createInjector(new TemplateModule(TemplateCategory.XML.configure()))
                .getInstance(TemplateCompiler.class)
                .compile("From: " + from + "\nTo: " + to
                        + "\nSubject:テストだ!!!\n\nほげほげだ!");

        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        final Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");

        Guice.createInjector(new ReportModuleSmtp(props, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(username, password);
            }
        })).getInstance(ReportManager.class).from(template).send(new Object());
    }
}
