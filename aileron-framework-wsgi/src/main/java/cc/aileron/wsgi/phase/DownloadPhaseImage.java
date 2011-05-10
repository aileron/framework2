/**
 * 
 */
package cc.aileron.wsgi.phase;


/**
 * ファイルダウンロード処理
 * 
 * @author aileron
 */
public abstract class DownloadPhaseImage implements DownloadPhase
{
    @Override
    public String filename()
    {
        return null;
    }
}
