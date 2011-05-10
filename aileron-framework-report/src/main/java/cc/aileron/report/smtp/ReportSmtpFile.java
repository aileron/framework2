/**
 * 
 */
package cc.aileron.report.smtp;

import java.util.List;

import javax.activation.FileDataSource;

/**
 * @author aileron
 */
public interface ReportSmtpFile
{
    /**
     * @return files
     */
    List<FileDataSource> files();
}
