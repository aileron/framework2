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

import cc.aileron.commons.util.ClassPattrnFinderUtils;
import cc.aileron.dao.impl.G2DaoManagerImpl;
import cc.aileron.dao.impl.G2DaoRegistrarImpl;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * @author Aileron
 * 
 * @deprecated as multi connection supported replaced by {@link G2MDaoModule}
 */
@Deprecated
public class G2DaoModule implements Module
{
    /*
     * (非 Javadoc)
     * 
     * @see com.google.inject.Module#configure(com.google.inject.Binder)
     */
    @Override
    public void configure(final Binder binder)
    {
        binder.bind(G2DaoManager.class)
                .to(G2DaoManagerImpl.class)
                .asEagerSingleton();
        binder.bind(G2DaoRegistrar.class).toInstance(registrar);
    }

    /**
     * @param dao
     */
    protected <T> void registry(final Class<T> type, final G2DaoLocal<T> dao)
    {
        registrar.registry(type, dao);
    }

    /**
     * @param <T>
     * @param factory
     * @param type
     * @throws Exception
     */
    private <T> void registry(final G2DaoFactory factory, final Class<T> type)
            throws Exception
    {
        final G2DaoLocal<T> dao = factory.create(type, false);
        registry(type, dao);
    }

    /**
     * @param targets
     * @throws Exception
     */
    public G2DaoModule(final Class<?>... targets) throws Exception
    {
        for (final Class<?> target : targets)
        {
            final G2DaoTargetSymbol symbol = target.getAnnotation(G2DaoTargetSymbol.class);
            final G2DaoCategory category = symbol == null ? G2DaoCategory.RDBMS
                    : symbol.category();
            final G2DaoFactory factory = category.getFactory();
            for (final Class<?> model : ClassPattrnFinderUtils.getClassNameList(target.getPackage()))
            {
                registry(factory, model);
            }
        }
    }

    /**
     * constractor
     */
    protected G2DaoModule()
    {
    }

    /**
     * registrar
     */
    final G2DaoRegistrarImpl registrar = new G2DaoRegistrarImpl();
}