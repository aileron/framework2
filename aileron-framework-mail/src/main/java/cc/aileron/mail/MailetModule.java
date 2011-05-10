/*
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
package cc.aileron.mail;

import java.io.IOException;
import java.util.Properties;

import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.commons.util.ResourceUtils;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * @author Aileron
 */
public class MailetModule implements Module
{
    @Override
    public void configure(final Binder binder)
    {
        binder.bind(MailetApplication.class)
            .to(mailet);
        binder.bind(MailetSetting.class)
            .toInstance(setting);
    }

    /**
     * @param settingFileName
     * @return {@link MailetSetting}
     * @throws ResourceNotFoundException
     * @throws IOException
     */
    private MailetSetting createSetting(final String settingFileName) throws IOException,
            ResourceNotFoundException
    {
        final Properties p = ResourceUtils.resource(settingFileName)
            .toProperties();
        final String account = p.getProperty("account");
        final String folderName = p.getProperty("folder", "INBOX");
        final String host = p.getProperty("host");
        final String password = p.getProperty("password");
        final int port = Integer.parseInt(p.getProperty("port"));
        final MailetType type = MailetType.valueOf(p.getProperty("mail-type")
            .toUpperCase());
        final MailetSettingType settingType = MailetSettingType.valueOf(p.getProperty(
                "setting-type", "normal")
            .toUpperCase());
        final Properties settingProperties = settingType.getProperties(String.valueOf(port));
        return new MailetSetting()
        {

            @Override
            public String account()
            {
                return account;
            }

            @Override
            public String folderName()
            {
                return folderName;
            }

            @Override
            public String host()
            {
                return host;
            }

            @Override
            public String password()
            {
                return password;
            }

            @Override
            public int port()
            {
                return port;
            }

            @Override
            public Properties properties()
            {
                return settingProperties;
            }

            @Override
            public MailetType type()
            {
                return type;
            }
        };
    }

    /**
     * @param mailet
     * @throws Exception
     */
    public MailetModule(final Class<? extends MailetApplication> mailet) throws Exception
    {
        this.mailet = mailet;
        this.setting = createSetting("mailet.properties");
    }

    private final Class<? extends MailetApplication> mailet;
    private final MailetSetting setting;
}
