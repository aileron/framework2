/**
 * Copyright (C) 2009 aileron.cc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.aileron.workflow.environment;

import static cc.aileron.commons.util.ResourceUtils.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

import cc.aileron.commons.resource.Resource;
import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.workflow.WorkflowConfigure;

import com.google.inject.Module;
import com.google.inject.Stage;

/**
 * 環境変数プロパティファイルから読み込む設定値
 * 
 * <dl>
 * <dt>wsgi.module</dt>
 * <dd>アプリケーションのコンテキストを管理するGuiceモジュールのクラス名</dd>
 * <dt>wsgi.container.configure</dt>
 * <dd>wsgiアプリケーションとしてコンテナに登録する為の設定クラス名
 * <dd>
 * <dt>wsgi.encode</dt>
 * <dd>アプリケーションの文字コード</dd>
 * <dt>wsgi.stage</dt>
 * <dd>アプリケーションの動作モード
 * <dd>
 * </dl>
 * 
 * @author Aileron
 */
class WorkflowEnvironmentImpl implements WorkflowEnvironment
{
    @Override
    public Class<? extends Module> getApplicationModule()
            throws ClassNotFoundException
    {
        return Class.forName(properties.getProperty("wsgi.module"))
                .asSubclass(Module.class);
    }

    @Override
    public Class<? extends WorkflowConfigure> getContainerConfigure()
            throws ClassNotFoundException
    {
        return Class.forName(properties.getProperty("wsgi.configure"))
                .asSubclass(WorkflowConfigure.class);
    }

    @Override
    public Charset getEncode()
    {
        return Charset.forName(properties.getProperty("wsgi.encode"));
    }

    @Override
    public String getFileName()
    {
        return fileName;
    }

    @Override
    public Properties getProperties()
    {
        return properties;
    }

    @Override
    public Stage getStage()
    {
        return stage;
    }

    @Override
    public boolean routingDebug()
    {
        return Boolean.parseBoolean(properties.getProperty("wsgi.routing.debug",
                "false"));
    }

    /**
     * @param fileName
     * @throws ResourceNotFoundException
     * @throws IOException
     */
    public WorkflowEnvironmentImpl(final String fileName) throws IOException,
            ResourceNotFoundException
    {
        this.fileName = fileName;
        this.stage = resourceNoneException("PRODUCTION") != Resource.NULL ? Stage.PRODUCTION
                : Stage.DEVELOPMENT;
        this.properties = resource(fileName).toProperties();
    }

    /**
     * 環境変数として読み込む為の設定ファイル名
     */
    private final String fileName;

    /**
     * 設定ファイル
     */
    private final Properties properties;

    private final Stage stage;
}
