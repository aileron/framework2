/**
 * 
 */
package cc.aileron.wsgi.mobile;

import static cc.aileron.wsgi.context.WsgiContextProvider.*;

/**
 * モバイルキャリアを変数に保持する、一般的なリソース
 */
public class MobileGenericResource
{
    /**
     * carrier
     */
    public final MobileCarrier carrier = MobileCarrier.parseUserAgent(context().request()
            .getHeader("User-Agent"));
}
