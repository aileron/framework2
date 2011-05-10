/**
 * 
 */
package cc.aileron.wsgi.phase;

import java.io.OutputStream;

/**
 * ファイルダウンロード処理
 * 
 * @author aileron
 */
public interface DownloadPhase
{
    /**
     * @return contentType
     */
    String contentType();

    /**
     * @return filename ファイル名
     */
    String filename();

    /**
     * ダウンロードストリームに出力する
     * 
     * @param stream
     * @throws Exception
     */
    void output(OutputStream stream) throws Exception;
}
