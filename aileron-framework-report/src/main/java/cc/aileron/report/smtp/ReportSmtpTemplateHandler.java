/**
 * 
 */
package cc.aileron.report.smtp;

/**
 * @author aileron
 * @param <E> 
 */
public interface ReportSmtpTemplateHandler<E>
{
    /**
     * @param resource
     * @return template
     * @throws Exception 
     */
    String get(E resource) throws Exception;
}
