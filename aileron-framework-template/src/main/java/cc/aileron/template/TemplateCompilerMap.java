/**
 * Copyright (C) 2009 Speee.inc
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
package cc.aileron.template;

import java.util.EnumMap;

/**
 * @author Misato Takahashi<misato@speee.jp>
 * 
 */
public class TemplateCompilerMap extends
		EnumMap<TemplateCategory, TemplateCompiler>
{
	/**
	 *
	 */
	private static final long serialVersionUID = -3762008449519782010L;

	/**
	 * @param map
	 */
	public TemplateCompilerMap(
			final EnumMap<TemplateCategory, TemplateCompiler> map)
	{
		super(map);
	}

}
