/**
 * Copyright (C) 2009 aileron.cc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.aileron.template.flow;

import static cc.aileron.template.flow.FlowCategory.*;

import java.util.ArrayList;
import java.util.List;

import cc.aileron.template.parser.ParserMethod;

/**
 * 
 * フローコンポーネント
 * 
 * @author Aileron
 *
 */
public class FlowComponent
{
    /**
     * 追加
     * @param component
     */
    public void add(final FlowComponent component)
    {
        component.parent = this;
        this.children.add(component);
    }

    /**
     * @param interpreter
     */
    public void add(final ParserMethod interpreter)
    {
        this.children.add(new FlowComponent(EXECUTE,interpreter));
    }

    /**
     * @return 親要素の取得
     */
    public FlowComponent getParent()
    {
        return parent;
    }

    /**
     * constractor
     * @param category
     * @param self
     */
    public FlowComponent(
            final FlowCategory category,
            final Object self)
    {
        this.category = category;
        this.self = self;
        this.children = new ArrayList<FlowComponent>();
    }

    /**
     * 親要素
     */
    FlowComponent parent ;

    /**
     * 自身
     */
    final Object self;

    /**
     * 子要素
     */
    final List<FlowComponent> children;

    /**
     * フロー種別
     */
    final FlowCategory category;
}