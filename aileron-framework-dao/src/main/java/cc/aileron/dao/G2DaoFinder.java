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

import java.util.List;

import cc.aileron.generic.Procedure;

/**
 * @author Aileron
 * @param <T>
 */
public interface G2DaoFinder<T>
{
    /**
     * @param object
     */
    void bind(T object);

    /**
     * @return count
     */
    int count();

    /**
     * @param procedure
     */
    void each(Procedure<T> procedure);

    /**
     * @param procedure
     * @param paging
     */
    void each(Procedure<T> procedure, G2DaoPaging paging);

    /**
     * @return exist
     */
    boolean exist();

    /**
     * @return list
     */
    List<T> list();

    /**
     * @param condition
     * @param paging
     * @return list
     */
    List<T> list(G2DaoPaging paging);

    /**
     * @return bean
     */
    T one();
}
