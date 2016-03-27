/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3.util;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

/**
 * Requires dependency:
 * 
 * <pre>
 *   &lt;dependency>
 *     &lt;groupId>xalan&lt;/groupId>
 *     &lt;artifactId>xalan&lt;/artifactId>
 *   &lt;/dependency>
 * </pre>
 */
public class XalanWomTransformations
{
	public static TransformerFactory getXalanTransformerFactory() throws TransformerFactoryConfigurationError
	{
		return TransformerFactory.newInstance(
				org.apache.xalan.processor.TransformerFactoryImpl.class.getName(),
				null);
	}
}
