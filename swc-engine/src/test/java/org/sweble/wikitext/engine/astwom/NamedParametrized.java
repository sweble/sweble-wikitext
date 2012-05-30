/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sweble.wikitext.engine.astwom;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

public class NamedParametrized
		extends
			Suite
{
	private final ArrayList<Runner> runners = new ArrayList<Runner>();
	
	// =========================================================================
	
	public NamedParametrized(Class<?> clazz) throws Throwable
	{
		super(clazz, Collections.<Runner> emptyList());
		
		int i = 0;
		for (Object[] parameters : getParametersList(getTestClass()))
			runners.add(new TestClassRunner(
					getTestClass().getJavaClass(),
					parameters,
					i++));
	}
	
	@Override
	protected List<Runner> getChildren()
	{
		return runners;
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getParametersList(TestClass clazz) throws Throwable
	{
		return (List<Object[]>) getParametersMethod(clazz).invokeExplosively(null);
	}
	
	private FrameworkMethod getParametersMethod(TestClass testClass) throws Exception
	{
		List<FrameworkMethod> methods = testClass.getAnnotatedMethods(Parameters.class);
		
		FrameworkMethod found = null;
		for (FrameworkMethod method : methods)
		{
			int modifiers = method.getMethod().getModifiers();
			if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
			{
				if (found != null)
					throw new Exception("Multiple public static methods are annotated with @Parameters in " + testClass.getName());
				
				found = method;
			}
		}
		
		if (found == null)
			throw new Exception("Cannot find public static method annotated with @Parameters in " + testClass.getName());
		
		return found;
	}
	
	// =========================================================================
	
	private static class TestClassRunner
			extends
				BlockJUnit4ClassRunner
	{
		private final int parameterSet;
		
		private final Object[] parameters;
		
		private final String parameterSetName;
		
		TestClassRunner(Class<?> type, Object[] parameters, int parameterSet) throws InitializationError
		{
			super(type);
			this.parameters = parameters;
			this.parameterSet = parameterSet;
			parameterSetName = getParameterSetName(parameters);
		}
		
		private static String getParameterSetName(Object[] parameters)
		{
			Object object = parameters[0];
			if (object instanceof String)
				return String.format("\"%s\"", (String) object);
			else
				return "<UKNOWN>";
		}
		
		@Override
		public Object createTest() throws Exception
		{
			return getTestClass().getOnlyConstructor().newInstance(parameters);
		}
		
		@Override
		protected String getName()
		{
			return String.format(
					"[%d]: %s",
					parameterSet,
					parameterSetName);
		}
		
		@Override
		protected String testName(final FrameworkMethod method)
		{
			return String.format(
					"%s with { %s, ... }",
					method.getName(),
					parameterSetName);
		}
		
		@Override
		protected void validateConstructor(List<Throwable> errors)
		{
			validateOnlyOneConstructor(errors);
		}
		
		@Override
		protected Statement classBlock(RunNotifier notifier)
		{
			return childrenInvoker(notifier);
		}
	}
}
