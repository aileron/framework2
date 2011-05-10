/**
 * Copyright (C) 2008 aileron.cc
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
 * 
 */
package cc.aileron.commons.resource;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;

/**
 * ClasspathResource
 * 
 * @author Aileron
 * 
 */
public interface Resource
{
    /**
     * @return bytes
     * @throws IOException
     */
    byte[] toBytes() throws IOException;

    /**
     * FileContent として取得
     * 
     * @return FileContent
     */
    FileContent toFileContent();

    /**
     * FileObject として取得
     * 
     * @return FileObject
     */
    FileObject toFileObject();

    /**
     * properties として取得
     * 
     * @return Properties
     * @throws IOException
     */
    Properties toProperties() throws IOException;

    /**
     * @return {@link Properties}
     */
    Properties toPropertiesNoneException();

    /*
     * (非 Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    String toString();

    /**
     * URL として取得
     * 
     * @return URL
     */
    URL toURL();

    /**
     * NULL
     */
    Resource NULL = new Resource()
    {
        @Override
        public byte[] toBytes() throws IOException
        {
            return null;
        }

        @Override
        public FileContent toFileContent()
        {
            return null;
        }

        @Override
        public FileObject toFileObject()
        {
            return null;
        }

        @Override
        public Properties toProperties() throws IOException
        {
            return new Properties();
        }

        @Override
        public Properties toPropertiesNoneException()
        {
            return new Properties();
        }

        @Override
        public URL toURL()
        {
            return null;
        }
    };
}
