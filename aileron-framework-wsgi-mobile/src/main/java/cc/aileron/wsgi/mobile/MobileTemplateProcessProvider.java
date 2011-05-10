/**
 *
 */
package cc.aileron.wsgi.mobile;

import cc.aileron.workflow.WorkflowProcess;

import com.google.inject.ImplementedBy;

/**
 * @author aileron
 */
@ImplementedBy(MobileTemplateProcessProviderImpl.class)
public interface MobileTemplateProcessProvider
{
    /**
     * @param <Resource>
     * @param global
     * @param path
     * @param csspath
     * @param contentType
     * @return wsgiProcess
     * @throws Exception
     */
    <Resource> WorkflowProcess<Resource> get(Object global, String path,
            String csspath) throws Exception;

    /**
     * @param <Resource>
     * @param path
     * @param csspath
     * @param contentType
     * @param templateCategory
     * @return wsgiProcess
     * @throws Exception
     */
    <Resource> WorkflowProcess<Resource> get(String path, String csspath)
            throws Exception;
}
