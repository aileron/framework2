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
package cc.aileron.dao;

import cc.aileron.dao.db.G2DaoFactoryRdbms;
import cc.aileron.dao.db.sql.SqlTemplateConfigure;
import cc.aileron.template.TemplateModule;

import com.google.inject.Guice;
import com.google.inject.Module;

/**
 * @author Aileron
 */
public enum G2DaoCategory
{
    /**
     * KVS
     */
    KVS
    {
        @Override
        public G2DaoFactory getFactory()
        {
            return null;
        }
    },

    /**
     * RDBMS
     */
    RDBMS
    {
        @Override
        public G2DaoFactory getFactory()
        {
            final Module templateModule = new TemplateModule(
                    SqlTemplateConfigure.configure);
            return Guice.createInjector(templateModule)
                .getInstance(G2DaoFactoryRdbms.class);
        }
    }

    ;

    /**
     * @return factory
     */
    public abstract G2DaoFactory getFactory();
}
