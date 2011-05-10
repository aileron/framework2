/**
 * 
 */
package cc.aileron.mail;

/**
 * プログラム処理対象では無い不正なメッセージを処理した際の例外
 * 
 * @author aileron
 */
public class WrongMessageException extends Exception
{
    private static final long serialVersionUID = 1L;

    /**
     * @param string
     */
    public WrongMessageException(final String string)
    {
        super(string);
    }
}