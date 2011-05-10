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

import java.util.Properties;

/**
 * @author Aileron
 */
public enum MailetSettingType
{
    /**
     * normal
     */
    NORMAL
    {
        @Override
        public Properties getProperties(final String port)
        {
            return System.getProperties();
        }
    },

    /**
     * ssl-pop3
     */
    SSL_POP3
    {
        @Override
        public Properties getProperties(final String port)
        {
            final Properties properties = System.getProperties();
            properties.setProperty("mail.pop3.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail.pop3.socketFactory.fallback", "false");
            properties.setProperty("mail.pop3.port", port);
            properties.setProperty("mail.pop3.socketFactory.port", port);
            return properties;
        }
    };

    /**
     * @param port
     * @return {@link Properties}
     */
    public abstract Properties getProperties(String port);
}
