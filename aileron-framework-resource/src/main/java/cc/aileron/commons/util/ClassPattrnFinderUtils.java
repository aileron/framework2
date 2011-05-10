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
package cc.aileron.commons.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileType;

import cc.aileron.commons.resource.Resource;
import cc.aileron.commons.resource.ResourceConvertUtils;
import cc.aileron.commons.resource.ResourceNotFoundException;

/**
 * @author Aileron
 * 
 */
public class ClassPattrnFinderUtils
{
    /**
     * 対象パッケージ以下のクラスを全て取得します
     * 
     * @param targetPackage
     * @return クラスの集合
     * @throws ResourceNotFoundException
     * @throws URISyntaxException
     * @throws IOException
     */
    public static final List<Class<?>> getClassNameList(final Package targetPackage) throws IOException,
            URISyntaxException,
            ResourceNotFoundException
    {
        return tryGetClassNameList(targetPackage.getName(), null);
    }

    /**
     * パターンに一致するクラスを取得します
     * 
     * @param targetPackage
     * @param classNamePattern
     * @return クラスの集合
     * @throws ResourceNotFoundException
     * @throws URISyntaxException
     * @throws IOException
     */
    public static final List<Class<?>> getClassNameList(final Package targetPackage,
            final Pattern classNamePattern) throws IOException,
            URISyntaxException,
            ResourceNotFoundException
    {
        return tryGetClassNameList(targetPackage.getName(), classNamePattern);
    }

    /**
     * クラスを取得します。取得出来ない場合はNULLを返します
     * 
     * @param className
     * @return
     */
    private static final Class<?> getClass(final String className)
    {
        try
        {
            return Class.forName(className);
        }
        catch (final ClassNotFoundException e)
        {
            return null;
        }
    }

    /**
     * クラスのリストの取得を試みます
     * 
     * @param targetPackage
     * @param pattern
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws ResourceNotFoundException
     */
    private static final List<Class<?>> tryGetClassNameList(final String targetPackage,
            final Pattern pattern) throws IOException,
            URISyntaxException,
            ResourceNotFoundException
    {
        final List<Class<?>> result = new ArrayList<Class<?>>();

        final ClassLoader classLoader = Thread.currentThread()
            .getContextClassLoader();

        final String path = targetPackage.replace('.', '/') + "/";
        final Enumeration<URL> urls = classLoader.getResources(path);
        while (urls.hasMoreElements())
        {
            final URL url = urls.nextElement();
            final Resource resource = ResourceConvertUtils.convertUrl(url);
            for (final FileObject file : resource.toFileObject()
                .getChildren())
            {
                final String name = file.getName()
                    .getBaseName()
                    .split("\\.")[0];
                if (!file.getType()
                    .equals(FileType.FILE) || !file.getName()
                    .getExtension()
                    .equals("class"))
                {
                    continue;
                }
                if (pattern != null && !pattern.matcher(name)
                    .matches())
                {
                    continue;
                }

                final Class<?> classObject = getClass(targetPackage + "."
                        + name);
                if (classObject != null)
                {
                    result.add(classObject);
                }
            }
        }
        return result;
    }

    /**
     * インスタンスは作成させない
     */
    private ClassPattrnFinderUtils()
    {
    }
}
