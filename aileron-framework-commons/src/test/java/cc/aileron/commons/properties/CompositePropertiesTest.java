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
package cc.aileron.commons.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Aileron
 * 
 */
@Ignore
public class CompositePropertiesTest
{
	/**
	 * @throws Exception
	 */
	@Test
	public void spec() throws Exception
	{
		final Properties prop = new Properties();
		prop.put("user.a.id", "user_a");
		prop.put("user.a.pass", "user_a_pass");
		prop.put("user.a.cnt", 100);
		prop.put("user.b.id", "user_b");
		prop.put("user.b.pass", "user_b_pass");

		final CompositeProperties cprop = CompositeProperties.load(prop);
		final CompositeProperties userA = cprop.get("user").get("a");

		assertThat(new Integer(100), is(userA.get("cnt").toNumber()));

		for (final Map.Entry<String, CompositeProperties> entry : cprop.get(
				"user").entrySet())
		{
			final CompositeProperties eprop = entry.getValue();
			if (entry.getKey().equals("a"))
			{
				assertThat("user_a", is(eprop.get("id").toString()));
				assertThat("user_a_pass", is(eprop.get("pass").toString()));
				assertThat(new Integer(100), is(eprop.get("cnt").toNumber()));
				assertThat(true, is(eprop.get("cnt").toBoolean()));
			}
		}
	}
}
