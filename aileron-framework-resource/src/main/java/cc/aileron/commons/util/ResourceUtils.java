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
package cc.aileron.commons.util;

import org.apache.commons.vfs.FileContent;

import cc.aileron.commons.resource.Resource;
import cc.aileron.commons.resource.ResourceImpl;
import cc.aileron.commons.resource.ResourceLoaders;
import cc.aileron.commons.resource.ResourceLoaders.Type;
import cc.aileron.commons.resource.ResourceNotFoundException;

/**
 * classpath 上のリソースにアクセスする為のクラス
 * 
 * @author Aileron
 * 
 */
public class ResourceUtils
{

    // file
    // -----------------------------------------------------------------------

    /**
     * @param targetClass
     * @return スラッシュ区切りのパス
     */
    public static final String classNameToDirectoryName(
            final Class<?> targetClass)
    {
        return targetClass.getName().replace('.', '/') + "/";
    }

    /**
     * packageName to directoryName
     * 
     * @param targetPackage
     * 
     * @return スラッシュ区切りのパス
     */
    public static final String packageNameToDirectoryName(
            final Package targetPackage)
    {
        return targetPackage.getName().replace('.', '/') + "/";
    }

    /**
     * リソースの取得
     * 
     * @param content
     * 
     * @return Resource
     * @throws ResourceNotFoundException
     */
    public static final Resource resource(final FileContent content)
            throws ResourceNotFoundException
    {
        return new ResourceImpl(content);
    }

    // path
    // -----------------------------------------------------------------------

    /**
     * リソースの取得
     * 
     * @param path
     * @return Resource
     * @throws ResourceNotFoundException
     */
    public static final Resource resource(final String path)
            throws ResourceNotFoundException
    {
        return ResourceLoaders.load(path);
    }

    /**
     * リソースの取得
     * 
     * @param path
     * @param type
     * @return Resource
     * @throws ResourceNotFoundException
     */
    public static final Resource resource(final String path, final Type type)
            throws ResourceNotFoundException
    {
        return ResourceLoaders.load(type, path);
    }

    // ResourceLoader types

    /**
     * リソースの取得
     * 
     * @param content
     * 
     * @return Resource
     */
    public static final Resource resourceNoneException(final FileContent content)
    {
        try
        {
            return resource(content);
        }
        catch (final Exception e)
        {
            return Resource.NULL;
        }
    }

    /**
     * リソースの取得
     * 
     * @param path
     * @return Resource
     */
    public static final Resource resourceNoneException(final String path)
    {
        try
        {
            return resource(path);
        }
        catch (final ResourceNotFoundException e)
        {
            return Resource.NULL;
        }
    }

    // utils
    // ---------------------------------------------------------------------

    /**
     * vsf パス指定でのリソースの取得
     * 
     * @param path
     * @return Resource
     * @throws ResourceNotFoundException
     */
    public static Resource vfsResource(final String path)
            throws ResourceNotFoundException
    {
        return ResourceLoaders.loadVsfResource(path);
    }
}