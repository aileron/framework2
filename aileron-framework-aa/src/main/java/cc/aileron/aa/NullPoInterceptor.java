/**
 *
 */
package cc.aileron.aa;

import java.util.LinkedList;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.Singleton;

/**
 * ガってする
 * 
 * @author aileron
 */
@Singleton
public class NullPoInterceptor implements MethodInterceptor
{
    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable
    {
        try
        {
            return invocation.proceed();
        }
        catch (final NullPointerException e)
        {
            throw new Exception(new StringBuilder().append("\n")
                    .append("　　Λ＿Λ　　＼＼")
                    .append("\n")
                    .append("　 （　・∀・）　　　|　|　ｶﾞｯ")
                    .append("\n")
                    .append("　と　　　　）　 　 |　|" + "　　 Ｙ　/ノ　　　 人")
                    .append("\n")
                    .append("　　　 /　）　 　 < 　>_Λ∩")
                    .append("\n")
                    .append("　 ＿/し'　／／. Ｖ｀Д´）/")
                    .append("\n")
                    .append("　（＿フ彡　　　　　 　　/")
                    .toString(), e);
        }
    }

    /**
     * default constractor
     */
    public NullPoInterceptor()
    {
        this.aas.add(new StringBuilder().append("\n")
                .append("　　Λ＿Λ　　＼＼")
                .append("\n")
                .append("　 （　・∀・）　　　|　|　ｶﾞｯ")
                .append("\n")
                .append("　と　　　　）　 　 |　|" + "　　 Ｙ　/ノ　　　 人")
                .append("\n")
                .append("　　　 /　）　 　 < 　>_Λ∩")
                .append("\n")
                .append("　 ＿/し'　／／. Ｖ｀Д´）/")
                .append("\n")
                .append("　（＿フ彡　　　　　 　　/")
                .toString());
    }

    private final List<String> aas = new LinkedList<String>();
}
