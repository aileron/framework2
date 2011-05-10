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
package cc.aileron.template.context;

import java.util.HashMap;
import java.util.List;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.generic.util.SkipList;

import com.google.inject.Singleton;

/**
 * 
 * TemplateContext の factory
 * 
 * @author Aileron
 * 
 */
@Singleton
public class TemplateContextFactoryImpl implements TemplateContextFactory
{
	/**
	 * @param defaultAccessor
	 * @return context
	 */
	public TemplateContext get(final PojoAccessor<?>[] defaultAccessor)
	{
		final NameSpaceImpl namespace = new NameSpaceImpl();
		return new TemplateContext()
		{
			@Override
			public PojoAccessor<?> getAccessor()
			{
				return _accessors.get(0);
			}

			@Override
			public PojoAccessor<?> getAccessor(final int idx)
			{
				return _accessors.get(idx);
			}

			@Override
			public WriterNameSpace getNameSpace()
			{
				return namespace;
			}

			@Override
			public WriterNameSpace getNameSpace(final String name)
			{
				namespace.currentNameSpace = name;
				return namespace;
			}

			@Override
			public void setAccessor(final PojoAccessor<?> accessor)
			{
				_accessors.set(0, accessor);
			}

			@Override
			public void setAccessor(final PojoAccessor<?> accessor,
					final int idx)
			{
				_accessors.set(idx, accessor);
			}

			private final List<PojoAccessor<?>> _accessors = new SkipList<PojoAccessor<?>>();
			{
				for (final PojoAccessor<?> accessor : defaultAccessor)
				{
					_accessors.add(accessor);
				}
			}
		};
	}
}

class NameSpaceImpl implements WriterNameSpace
{
	public void append(final String content)
	{
		namespace.get(currentNameSpace).append(content);
	}

	public void clear()
	{
		namespace.put(currentNameSpace, new StringBuilder());
	}

	public String toOutputStrings()
	{
		return namespace.get(currentNameSpace).toString();
	}

	/**
	 * 現在の名前空間
	 */
	public String currentNameSpace = "default";

	/**
	 * メモリ空間
	 */
	private final HashMap<String, StringBuilder> namespace = new HashMap<String, StringBuilder>()
	{
		private static final long serialVersionUID = -3480016307309852451L;

		@Override
		public StringBuilder get(final Object key)
		{
			final StringBuilder builder = super.get(key);
			if (builder != null)
			{
				return builder;
			}
			final StringBuilder newBuilder = new StringBuilder();
			super.put(key.toString(), newBuilder);
			return newBuilder;
		}
	};
}