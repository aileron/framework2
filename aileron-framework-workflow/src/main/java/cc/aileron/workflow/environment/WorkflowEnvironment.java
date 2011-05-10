package cc.aileron.workflow.environment;

import java.nio.charset.Charset;
import java.util.Properties;

import cc.aileron.workflow.WorkflowConfigure;

import com.google.inject.Module;
import com.google.inject.Stage;

/**
 * wsgi 環境変数
 */
public interface WorkflowEnvironment
{
    /**
     * @return application-module
     * @throws ClassNotFoundException
     */
    Class<? extends Module> getApplicationModule()
            throws ClassNotFoundException;

    /**
     * @return contoller-configure
     * @throws ClassNotFoundException
     */
    Class<? extends WorkflowConfigure> getContainerConfigure()
            throws ClassNotFoundException;

    /**
     * @return encode
     */
    Charset getEncode();

    /**
     * @return fileName
     */
    String getFileName();

    /**
     * @return properties
     */
    Properties getProperties();

    /**
     * @return Stage
     */
    Stage getStage();

    /**
     * @return routing-debug
     */
    boolean routingDebug();

    /**
     * factory
     */
    final WorkflowEnvironmentFactory factory = new WorkflowEnvironmentFactoryImpl();
}