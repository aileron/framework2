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
package cc.aileron.dao.db.sql;

import static cc.aileron.generic.util.StringUtils.*;

import java.util.List;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.generic.util.SkipList;
import cc.aileron.template.context.WriterNameSpace;

/**
 * @author Aileron
 */
public interface G2DaoDtoAccessor
{
    /**
     * @author Aileron
     */
    enum Category
    {
        IN
        {
            @Override
            public void call(final WriterNameSpace namespace,
                    final PojoAccessor<Object> accessor,
                    final List<Object> parameters, final String args,
                    final String content)
                    throws PojoAccessorValueNotFoundException,
                    PojoPropertiesNotFoundException
            {
                final List<String> keys = new SkipList<String>();

                final String[] tokens = args.split("@");
                final String key = tokens[0];
                final String id = tokens.length > 1 ? tokens[1] : "self";
                for (final PojoAccessor<Object> object : accessor.to(key)
                        .accessorIterable(Object.class))
                {
                    parameters.add(object.to(id).value(Object.class));
                    keys.add("?");
                }

                namespace.append(join(",", keys));
            }
        },
        REP
        {
            @Override
            public void call(final WriterNameSpace namespace,
                    final PojoAccessor<Object> accessor,
                    final List<Object> parameters, final String args,
                    final String content)
                    throws PojoAccessorValueNotFoundException,
                    PojoPropertiesNotFoundException
            {
                namespace.append(accessor.to(args).value(String.class));
            }
        },
        VAR
        {
            @Override
            public void call(final WriterNameSpace namespace,
                    final PojoAccessor<Object> accessor,
                    final List<Object> parameters, final String args,
                    final String content)
                    throws PojoAccessorValueNotFoundException,
                    PojoPropertiesNotFoundException
            {
                parameters.add(accessor.to(args).value(Object.class));
                namespace.append("?");
            }
        };

        /**
         * @param namespace
         * @param accessor
         * @param parameters
         * @param args
         * @param content
         * @throws PojoAccessorValueNotFoundException
         * @throws PojoPropertiesNotFoundException
         */
        public abstract void call(final WriterNameSpace namespace,
                final PojoAccessor<Object> accessor,
                final List<Object> parameters, final String args,
                final String content)
                throws PojoAccessorValueNotFoundException,
                PojoPropertiesNotFoundException;
    }

    /**
     * @param args
     * @param content
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    void call(String args, String content)
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException;
}
