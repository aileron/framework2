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
 */
package cc.aileron.commons.resource;

import java.io.File;
import java.net.URL;

import org.apache.commons.vfs.FileSystemException;

import cc.aileron.commons.util.ResourceUtils;

/**
 * Resourceへ変換する
 * 
 * @author Aileron
 * 
 */
public class ResourceConvertUtils
{
    /**
     * java.io.File から Resource インスタンスを生成
     * 
     * @param file
     * @return Resource
     * @throws FileSystemException
     * @throws ResourceNotFoundException
     */
    public static Resource convertFile(final File file) throws FileSystemException,
            ResourceNotFoundException
    {
        return ResourceUtils.resource(ResourceLoaders.manager.toFileObject(file)
            .getContent());
    }

    /**
     * java.net.URL　から Resource インスタンスを生成
     * 
     * @param url
     * @return Resource
     * @throws FileSystemException
     * @throws ResourceNotFoundException
     */
    public static Resource convertUrl(final URL url) throws FileSystemException,
            ResourceNotFoundException
    {
        return ResourceUtils.resource(ResourceLoaders.manager.resolveFile(url.toString())
            .getContent());
    }
}