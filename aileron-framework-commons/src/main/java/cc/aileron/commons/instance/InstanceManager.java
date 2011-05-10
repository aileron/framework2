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
package cc.aileron.commons.instance;

import java.lang.annotation.Annotation;

import com.google.inject.Guice;
import com.google.inject.ImplementedBy;
import com.google.inject.Module;

/**
 * 
 * インスタンス取得用 Provider
 * 
 * @author Aileron
 * 
 */
@ImplementedBy(InstanceManagerImpl.class)
public interface InstanceManager
{
    /**
     * @author aileron
     * @param <T>
     * @param <P>
     */
    interface Factory<T, P>
    {
        T get(P object);
    }

    /**
     * @author aileron
     * @param <T>
     * @param <P1>
     * @param <P2>
     */
    interface Factory2<T, P1, P2>
    {
        T get(P1 arg1, P2 arg2);
    }

    /**
     * @author aileron
     * @param <T>
     * @param <P1>
     * @param <P2>
     * @param <P3>
     */
    interface Factory3<T, P1, P2, P3>
    {
        T get(P1 arg1, P2 arg2, P3 arg3);
    }

    /**
     * @author aileron
     * 
     * @param <T>
     * @param <P>
     */
    interface FactoryX<T, P>
    {
        T get(P... args);
    }

    /**
     * @param <T>
     * @param type
     * @return instance
     */
    <T> T get(Class<T> type);

    /**
     * @param <T>
     * @param type
     * @param annotation
     * @return T
     */
    <T> T get(Class<T> type, Class<? extends Annotation> annotation);

    /**
     * @param <T>
     * @param object
     * @return object
     */
    <T> T injectMembers(T object);

    /**
     * factory
     */
    FactoryX<InstanceManager, Module> factory = new FactoryX<InstanceManager, Module>()
    {
        @Override
        public InstanceManager get(final Module... modules)
        {
            return Guice.createInjector(modules)
                    .getInstance(InstanceManager.class);
        }
    };
}
