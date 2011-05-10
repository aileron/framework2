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
package cc.aileron.template.reader;

import cc.aileron.template.flow.FlowComponent;
import cc.aileron.template.parser.ParserMethodNotFoundException;

import com.google.inject.ImplementedBy;

/**
 * リーダー
 * 
 * @author Aileron
 * 
 */
@ImplementedBy(TemplateReaderImpl.class)
public interface TemplateReader
{
    /**
     * テンプレートを読みこむ
     * @param charSequence
     * @return パース結果
     * @throws TemplateSyntaxEexception
     * @throws ParserMethodNotFoundException
     */
    FlowComponent read(CharSequence charSequence)
            throws TemplateSyntaxEexception, ParserMethodNotFoundException;
}
