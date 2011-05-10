/**
 * 
 */
package cc.aileron.wsgi.phase;

/**
 * ファイルダウンロード処理
 * 
 * @author aileron
 */
public abstract class DownloadPhaseOctetStream implements DownloadPhase
{
    @Override
    public String contentType()
    {
        return "application/octet-stream";
    }
}
