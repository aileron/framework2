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

/**
 * 
 * フロー種別に対応するメソッドが定義されていない際に発生するエラー
 * 
 * @author Aileron
 * 
 */
public class FlowMethodNotFoundError extends Error
{
    private static final long serialVersionUID = 3155336000858632132L;

    /**
     * @param flowCategory
     */
    public FlowMethodNotFoundError(final FlowCategory flowCategory)
    {
        super(flowCategory.name());
    }
}
