package cc.aileron.report.smtp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.accessor.PojoAccessorManager;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.report.Report;
import cc.aileron.template.Template;
import cc.aileron.template.flow.FlowMethodNotFoundError;
import cc.aileron.template.parser.ParserMethodNotFoundException;
import cc.aileron.template.reader.TemplateSyntaxEexception;

/**
 * メール送信
 * 
 * @param <T>
 */
public class ReportSmtp<T> implements Report<T>
{
    class MailHeaders extends HashMap<String, String>
    {
        private static final long serialVersionUID = 1L;

        public InternetAddress getAddr(final String key)
                throws AddressException
        {
            return new InternetAddress(get(key));
        }

        @Override
        public String toString()
        {
            final StringBuffer buf = new StringBuffer();
            for (final java.util.Map.Entry<String, String> e : entrySet())
            {
                buf.append(" ");
                buf.append(e.getKey() + ":" + e.getValue());
            }
            return buf.toString();
        }
    }

    /*
     * (非 Javadoc)
     * 
     * @see
     * jp.speee.ranking.report.Report#send(jp.speee.ranking.report.ReportCategory
     * , java.lang.Object)
     */
    @Override
    public void send(final T object)
    {
        try
        {
            final MimeMessage msg = createMessage(object, template);
            Transport.send(msg);
        }
        catch (final Exception e)
        {
            throw new Error(e);
        }
    }

    /**
     * 
     * @param object
     * @return
     * @throws TemplateSyntaxEexception
     * @throws ParserMethodNotFoundException
     * @throws ResourceNotFoundException
     * @throws MessagingException
     * @throws FlowMethodNotFoundError
     * @throws PojoAccessorValueNotFoundException
     * @throws PojoPropertiesNotFoundException
     */
    private MimeMessage createMessage(final T object, final Template template)
            throws TemplateSyntaxEexception, ParserMethodNotFoundException,
            ResourceNotFoundException, MessagingException,
            FlowMethodNotFoundError, PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final StringWriter writer = new StringWriter();
        template.print(new PrintWriter(writer),
                pojoAccessorManager.from(object));
        final String text = writer.toString();

        logger.debug(text);

        final String[] splitMessage = text.split("(\\\n\\\n)|(\\\r\\\n)");
        final String header = splitMessage[0];
        final StringBuffer bodyBuffer = new StringBuffer();
        for (int idx = 1; idx < splitMessage.length; idx++)
        {
            bodyBuffer.append(splitMessage[idx]);
            bodyBuffer.append("\n\n");
        }
        final String content = bodyBuffer.toString();
        final MimeBodyPart body = new MimeBodyPart();
        body.setContent(content, "text/plain; charset=\"iso-2022-jp\"");
        final MimeMultipart mime = new MimeMultipart();
        mime.addBodyPart(body);

        if (object instanceof ReportSmtpFile)
        {
            for (final DataSource dataSource : ((ReportSmtpFile) object).files())
            {
                final MimeBodyPart filebody = new MimeBodyPart();
                filebody.setDataHandler(new DataHandler(dataSource));
                mime.addBodyPart(filebody);
            }
        }

        final MailHeaders headers = new MailHeaders();
        final Matcher matcher = headerPattern.matcher(header);
        while (matcher.find())
        {
            if (matcher.groupCount() == 2)
            {
                final String key = matcher.group(1);
                final String val = matcher.group(2);

                if (val == null || val.isEmpty())
                {
                    throw new Error("[" + key + "] is not found !!!");
                }

                headers.put(key, val);
            }
        }
        final MimeMessage msg = new MimeMessage(createSession());
        msg.addHeader("From", headers.get("From"));
        msg.addHeader("From", headers.remove("From"));
        msg.addHeader("To", headers.remove("to"));
        try
        {
            msg.setSubject(MimeUtility.encodeText(headers.remove("Subject"),
                    "iso-2022-jp",
                    "B"));
        }
        catch (final UnsupportedEncodingException e)
        {
        }
        for (final Entry<String, String> e : headers.entrySet())
        {
            msg.addHeader(e.getKey(), e.getValue());
        }
        msg.setContent(mime);
        return msg;
    }

    /**
     * 
     * @return
     */
    private Session createSession()
    {
        if (setting.authenticator != null)
        {
            return Session.getInstance(setting.properties,
                    setting.authenticator);
        }
        return Session.getDefaultInstance(setting.properties);
    }

    /**
     * @param pojoAccessorManager
     * @param setting
     * @param template
     * @throws Exception
     */
    public ReportSmtp(final PojoAccessorManager pojoAccessorManager,
            final ReportSmtpSetting setting, final Template template)
            throws Exception
    {
        this.template = template;
        this.setting = setting;
        this.pojoAccessorManager = pojoAccessorManager;
    }

    private final Pattern headerPattern = Pattern.compile("^([^:]+)\\:\\s*(.+)$",
            Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
    private final Logger logger = LoggerFactory.getLogger(Report.class);
    private final PojoAccessorManager pojoAccessorManager;
    private final ReportSmtpSetting setting;
    private final Template template;
}
