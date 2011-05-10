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
package cc.aileron.accessor;

/**
 * 指定した PojoProperties が存在しない時に発生する例外
 * 
 * @author Aileron
 */
public class PojoPropertiesNotFoundException extends Exception
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -289575379666991302L;

    /**
     * @param target
     * @param type
     * @param key
     */
    public PojoPropertiesNotFoundException(final Class<?> target,
            final String key, final PojoAccessorMethod type)
    {
        super(target.getName() + "@" + key + "(" + type + ")");
    }
}
