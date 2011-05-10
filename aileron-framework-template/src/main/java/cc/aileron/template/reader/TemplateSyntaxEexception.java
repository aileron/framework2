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

/**
 * 
 * テンプレートを読み込み中にシンタックスが不正だった場合に発生する例外
 * 
 * @author Aileron
 * 
 */
public class TemplateSyntaxEexception extends Exception
{
    /**
     * 種別
     * 
     * @author Aileron
     * 
     */
    public static enum Category
    {
        /**
         * メソッドの記述形式が不正
         */
        METHOD_SYNTAX_ERROR,

        /**
         * タグがオープンされていないのにクローズした
         */
        NOT_OPEN
    }

    private static final long serialVersionUID = 586464921107944557L;

    /**
     * @param category
     * @param number
     * @param charSequence
     */
    public TemplateSyntaxEexception(final Category category, final int number,
            final CharSequence charSequence)
    {
        super(category + ":" + number + ":" + charSequence);
        this.category = category;
        this.number = number;
    }

    /**
     * 種別
     */
    public final Category category;

    /**
     * 行数
     */
    public final int number;
}
