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
package cc.aileron.commons.util;

import static cc.aileron.generic.util.Cast.*;
import cc.aileron.commons.instance.InstanceManager;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@ImplementedBy(LogicInitializerImpl.class)
public interface LogicInitializer
{
    /**
     * @author Aileron
     * @param <T>
     */
    public static interface Initialize<T>
    {
        /**
         * 初期化
         * 
         * @param target
         */
        void initialize(T target);
    }

    /**
     * @param <Target>
     * @param <Logic>
     * @param logicClass
     * @param target
     * @return logic
     */
    <Target, Logic> Logic create(final Target target,
            final Class<Logic> logicClass);
}

/**
 * @author Aileron
 */
@Singleton
class LogicInitializerImpl implements LogicInitializer
{
    /**
     * @param <Target>
     * @param <Logic>
     * @param logicClass
     * @param target
     * @return logic
     */
    public <Target, Logic> Logic create(final Target target,
            final Class<Logic> logicClass)
    {
        final Logic logic = factory.get(logicClass);
        if (!(logic instanceof LogicInitializer.Initialize<?>))
        {
            throw new Error();
        }
        final LogicInitializer.Initialize<Target> initialize = cast(logic);
        initialize.initialize(target);
        return logic;
    }

    /**
     * @param iFactory
     */
    @Inject
    public LogicInitializerImpl(final InstanceManager iFactory)
    {
        factory = iFactory;
    }

    private final InstanceManager factory;
}