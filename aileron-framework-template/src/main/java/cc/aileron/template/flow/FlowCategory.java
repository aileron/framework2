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
package cc.aileron.template.flow;

import java.util.HashMap;

/**
 * フロー種別
 * 
 * @author Aileron
 */
public enum FlowCategory
{
    /**
     * コメント
     */
    COMMENT,

    /**
     * 分岐
     */
    DEF,

    /**
     * 反復
     */
    EACH,

    /**
     * 実行
     */
    EXECUTE,

    /**
     * インクルード
     */
    INCLUDE,

    /**
     * 順次
     */
    SEQUENTIAL,

    /**
     * 名前空間の束縛
     */
    WITH;

    private static HashMap<String, Void> map = new HashMap<String, Void>();
    static
    {
        map.put("with", null);
        map.put("def", null);
        map.put("each", null);
        map.put("comment", null);
        map.put("include", null);
    }

    /**
     * @param name
     * @return ステートメントかどうか
     */
    public static boolean isFlowStatement(final String name)
    {
        return map.containsKey(name);
    }
}
