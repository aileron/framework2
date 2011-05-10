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
package cc.aileron.generic.util;

import java.nio.charset.Charset;

/**
 * 文字コード変換 ユーティリティ
 * 
 * @author Aileron
 */
public class EncodeConvertorUtils
{
	static final Charset defaultEncode = Charset.forName("iso-8859-1");

	/**
	 * @param encode
	 * @return {@link EncodeConvertor}
	 */
	public static EncodeConvertor getConvertor(final Charset encode)
	{
		return new EncodeConvertor()
		{

			@Override
			public String convert(final String value)
			{
				return new String(value.getBytes(defaultEncode), encode);
			}
		};
	}

	/**
	 * @param encodeName
	 * @return {@link EncodeConvertor}
	 */
	public static EncodeConvertor getConvertor(final String encodeName)
	{
		final Charset encode = Charset.forName(encodeName);
		return new EncodeConvertor()
		{
			@Override
			public String convert(final String value)
			{
				return new String(value.getBytes(defaultEncode), encode);
			}
		};
	}
}
