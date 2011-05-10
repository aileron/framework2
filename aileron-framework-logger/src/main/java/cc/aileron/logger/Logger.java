/**
 *
 */
package cc.aileron.logger;

/**
 * @author aileron
 */
public interface Logger
{
    /**
     * @param message
     * @param objects
     */
    void debug(String message, Object... objects);

    /**
     * @param message
     * @param th
     * @param objects
     */
    void debug(String message, Throwable th, Object... objects);

    /**
     * @param message
     * @param objects
     */
    void error(String message, Object... objects);

    /**
     * @param message
     * @param th
     * @param objects
     */
    void error(String message, Throwable th, Object... objects);

    /**
     * @param message
     * @param objects
     */
    void info(String message, Object... objects);

    /**
     * @param message
     * @param th
     * @param objects
     */
    void info(String message, Throwable th, Object... objects);

    /**
     * @param message
     * @param objects
     */
    void trace(String message, Object... objects);

    /**
     * @param message
     * @param th
     * @param objects
     */
    void trace(String message, Throwable th, Object... objects);

    /**
     * @param message
     * @param objects
     */
    void warn(String message, Object... objects);

    /**
     * @param message
     * @param th
     * @param objects
     */
    void warn(String message, Throwable th, Object... objects);
}