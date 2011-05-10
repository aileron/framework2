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

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;

/**
 * 
 * InstanceFactory 実装
 * 
 * @author Aileron
 * 
 */
@Singleton
public class InstanceManagerImpl implements InstanceManager
{
    @Override
    public <T> T get(final Class<T> target)
    {
        return injector.getInstance(target);
    }

    @Override
    public <T> T get(final Class<T> type,
            final Class<? extends Annotation> annotation)
    {
        return injector.getInstance(Key.get(type, annotation));
    }

    @Override
    public <T> T injectMembers(final T object)
    {
        injector.injectMembers(object);
        return object;
    }

    /**
     * constractor injection
     * 
     * @param injector
     */
    @Inject
    public InstanceManagerImpl(final Injector injector)
    {
        this.injector = injector;
    }

    /**
     * injector
     */
    final Injector injector;
}
