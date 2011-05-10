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
package cc.aileron.dao.db.sql;

import static cc.aileron.commons.util.ResourceUtils.*;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.commons.resource.Resource;
import cc.aileron.commons.resource.ResourceConvertUtils;
import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.dao.db.G2DaoSqlMap;
import cc.aileron.dao.db.G2DbName;
import cc.aileron.template.Template;
import cc.aileron.template.TemplateCompiler;
import cc.aileron.template.parser.ParserMethodNotFoundException;
import cc.aileron.template.reader.TemplateSyntaxEexception;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 
 * G2Dao-Sql-Map
 * 
 * @author Aileron
 * 
 */
@Singleton
public class G2DaoSqlMapImpl implements G2DaoSqlMap
{
    @Override
    public void compile(final Class<?> targetClass)
            throws IOException, ResourceNotFoundException,
            TemplateSyntaxEexception, ParserMethodNotFoundException
    {
        final String sqlDir = getDir(targetClass);
        if (StringUtils.isEmpty(sqlDir))
        {
            return;
        }

        logger.trace("compile : {} @ {}", targetClass, sqlDir);

        final Enumeration<URL> urls = Thread.currentThread()
                .getContextClassLoader()
                .getResources(sqlDir);

        while (urls.hasMoreElements())
        {
            final URL url = urls.nextElement();
            final Resource resource = ResourceConvertUtils.convertUrl(url);
            for (final FileObject file : resource.toFileObject().getChildren())
            {
                final String name = file.getName().getBaseName().split("\\.")[0];

                logger.trace("load sql-file : {}.sql", name);

                if (!file.getType().equals(FileType.FILE)
                        || !file.getName().getExtension().equals("sql"))
                {
                    continue;
                }

                /*
                 * SQLテンプレートファイルのロードとパース
                 */
                final Template template = compiler.compile(resource(file.getContent()).toString());

                /*
                 * findBy -> find の形式も大丈夫な様に
                 */
                final String key = name.replaceFirst("^findBy", "find")
                        .replaceFirst("^countBy", "count")
                        .replaceFirst("^updateBy", "update")
                        .replaceFirst("^deleteBy", "delete")
                        .replaceFirst("^executeBy", "execute");

                final HashMap<String, Template> m = sqlMap.get(targetClass);
                m.put(key, template);
                m.put(name, template);
            }
        }
    }

    @Override
    public Template get(final Class<?> targetClass, final G2DbName dbName,
            final String name)
    {
        final HashMap<String, Template> map = sqlMap.get(targetClass);
        final Template dbTemplate = map.get(name + "_"
                + dbName.name().toLowerCase());
        if (dbTemplate != null)
        {
            return dbTemplate;
        }

        final Template template = map.get(name);
        if (template == null)
        {
            throw new SqlNotFoundRuntimeException(targetClass + ":" + name);
        }
        return template;
    }

    /**
     * @param targetClass
     * @return dir
     */
    private String getDir(final Class<?> targetClass)
    {
        final String name = targetClass.getCanonicalName();
        if (name == null)
        {
            return null;
        }
        final StringBuffer buffer = new StringBuffer();
        final Matcher matcher = pattern.matcher(name);
        while (matcher.find())
        {
            matcher.appendReplacement(buffer, "/"
                    + matcher.group(1).toLowerCase());
        }
        return matcher.appendTail(buffer).toString();
    }

    /**
     * constractor injection
     * 
     * @param compiler
     */
    @Inject
    public G2DaoSqlMapImpl(final TemplateCompiler compiler)
    {
        this.compiler = compiler;
        this.sqlMap = new HashMap<Class<?>, HashMap<String, Template>>()
        {
            private static final long serialVersionUID = 909010518271062778L;

            @Override
            public HashMap<String, Template> get(final Object key)
            {
                if (!super.containsKey(key))
                {
                    super.put((Class<?>) key, new HashMap<String, Template>());
                }
                return super.get(key);
            }
        };
    }

    /**
     * compiler
     */
    private final TemplateCompiler compiler;

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 
     */
    private final Pattern pattern = Pattern.compile("\\.(.)");

    /**
     * sqlMap
     */
    private final HashMap<Class<?>, HashMap<String, Template>> sqlMap;
}
