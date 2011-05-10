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
package cc.aileron.dao.db.exec;

import java.util.EnumSet;

import cc.aileron.generic.PrimitiveWrappers.NumberGetAccessor;

import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class G2DaoParameterConvertManagerImpl implements
        G2DaoParameterConvertManager
{
    @Override
    public Object convert(final Object object)
    {
        if (object == null)
        {
            return null;
        }

        /*
         * Numberのオブジェクトとして評価可能な場合には、Numberの値を取得
         */
        if (object instanceof NumberGetAccessor)
        {
            return NumberGetAccessor.class.cast(object).toNumber();
        }

        /*
         * EnumSet の場合は、各Enumの数値表現の重ね合わせをDBに格納する為
         * 計算する。またEnumの数値表現は、Numberとして評価した際の値を使用する
         */
        if (object instanceof EnumSet)
        {
            int result = 0;
            for (final Object o : EnumSet.class.cast(object))
            {
                result += ((NumberGetAccessor) o).toNumber().intValue();
            }
            return result;
        }

        /*
         * 特に変換無し
         */
        return object;
    }
}
