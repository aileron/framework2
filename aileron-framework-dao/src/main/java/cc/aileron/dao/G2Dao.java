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
package cc.aileron.dao;

/**
 * @author Aileron
 * @param <T>
 */
public interface G2Dao<T>
{
    /**
     * @param <P>
     * @param type
     * @return {@link G2DaoExecute}
     */
    <P> G2DaoExecute<P, T> execute(Class<P> type);

    /**
     * 初期化
     */
    void initialize();

    /**
     * @param target
     * @return インサートした際に生成されたID
     */
    Number insert(T target);

    /**
     * @param target
     * @return updateした際の件数
     */
    Number update(T target);

    /**
     * @return {@link G2DaoWhere}
     */
    G2DaoWhere<T> where();

    /**
     * @param <C>
     * @param condition
     * @return {@link G2DaoWhere}
     */
    <C> G2DaoWhere<T> where(C condition);

    /**
     * @param <C>
     * @param condition
     * @param type
     * @return {@link G2DaoWhere}
     */
    <C> G2DaoWhere<T> where(C condition, Class<C> type);
}
