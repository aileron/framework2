/**
 * Copyright 2008 aileron.cc, Inc.
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
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map.Entry;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;

/**
 * ResourceLoader
 * 
 * @author Aileron
 * 
 */
public class ResourceLoaders
{
    /**
     * type
     * 
     * @author Aileron
     * 
     */
    public static enum Type
    {
        /**
         * アプリケーション固有のパス
         */
        APPLICATIONPATH,

        /**
         * クラスパス
         */
        CLASSPATH,

        /**
         * ファイルパス
         */
        FILEPATH;
    }

    /**
     * リソースローダ
     * 
     * @author Aileron
     * 
     */
    private static abstract class Loader
    {
        /**
         * FileObject の取得
         * 
         * @param path
         * @return FileObject
         * @throws ResourceNotFoundException
         * @throws FileSystemException
         */
        public abstract FileObject get(final String path) throws ResourceNotFoundException,
                FileSystemException;

        /**
         * construct
         * 
         * @param type
         */
        public Loader(final Type type)
        {
            this.type = type;
        }

        /**
         * loader-type
         */
        @SuppressWarnings("unused")
        public final Type type;
    }

    /**
     * ローダー
     */
    private static final EnumMap<Type, Loader> map = new EnumMap<Type, Loader>(
            Type.class);

    /**
     * FileSystemManager
     */
    static final FileSystemManager manager;

    static
    {
        try
        {
            manager = VFS.getManager();
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    static
    {
        map.put(Type.CLASSPATH, new Loader(Type.CLASSPATH)
        {

            @Override
            public FileObject get(final String path) throws ResourceNotFoundException,
                    FileSystemException
            {
                final URL url = Thread.currentThread()
                    .getContextClassLoader()
                    .getResource(path);

                if (url != null)
                {
                    return manager.resolveFile(url.toString());
                }
                throw new ResourceNotFoundException(path);
            }
        });
        map.put(Type.FILEPATH, new Loader(Type.FILEPATH)
        {
            @Override
            public FileObject get(final String path) throws ResourceNotFoundException,
                    FileSystemException
            {
                return manager.toFileObject(new File(path));
            }
        });
    }

    /**
     * URL の基を追加
     * 
     * @param type
     * @param resource
     * 
     */
    public static void appendResourceLoader(final Type type,
            final Resource resource)
    {
        final FileObject root = resource.toFileContent()
            .getFile();
        if (root == null)
        {
            throw new RuntimeException("url is null");
        }
        map.put(type, new Loader(type)
        {

            @Override
            public FileObject get(final String path) throws ResourceNotFoundException,
                    FileSystemException
            {
                return root.resolveFile(path);
            }
        });
    }

    /**
     * リソースの取得
     * 
     * @param path
     * @return Resource
     * @throws ResourceNotFoundException
     */
    public static Resource load(final String path) throws ResourceNotFoundException
    {
        try
        {
            return tryLoad(path);
        }
        catch (final ResourceNotFoundException e)
        {
            throw e;
        }
        catch (final Exception e)
        {
            throw new ResourceNotFoundException(path, e);
        }
    }

    /**
     * リソースの取得（Loaderの指定）
     * 
     * @param type
     * @param path
     * @return Resource
     * @throws ResourceNotFoundException
     */
    public static Resource load(final Type type,
            final String path) throws ResourceNotFoundException
    {
        final FileContent content;
        try
        {
            content = map.get(type)
                .get(path)
                .getContent();
        }
        catch (final Exception e)
        {
            throw new ResourceNotFoundException(path, e);
        }
        return new ResourceImpl(content);
    }

    /**
     * vsf パス経由でのリソースの取得
     * 
     * @param path
     * @return Resource
     * @throws ResourceNotFoundException
     */
    public static Resource loadVsfResource(final String path) throws ResourceNotFoundException
    {
        try
        {
            return new ResourceImpl(manager.resolveFile(path)
                .getContent());
        }
        catch (final FileSystemException e)
        {
            throw new ResourceNotFoundException(e);
        }
    }

    /**
     * try-load
     * 
     * @param path
     * @return
     * @throws URISyntaxException
     * @throws MalformedURLException
     * @throws ResourceNotFoundException
     */
    private static Resource tryLoad(final String path) throws URISyntaxException,
            MalformedURLException,
            ResourceNotFoundException
    {
        for (final Entry<Type, Loader> entry : map.entrySet())
        {
            final ResourceLoaders.Type type = entry.getKey();
            final ResourceLoaders.Loader loader = entry.getValue();
            try
            {
                final FileObject file = loader.get(path);
                if (file.exists())
                {
                    return new ResourceImpl(file.getContent(), type);
                }
            }
            catch (final Exception e)// 例外は握りつぶす
            {
            }
        }
        throw new ResourceNotFoundException(path);
    }
}