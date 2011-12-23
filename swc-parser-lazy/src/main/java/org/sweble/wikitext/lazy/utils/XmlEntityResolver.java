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
package org.sweble.wikitext.lazy.utils;

public interface XmlEntityResolver
{
	/**
	 * Resolve an XML entity reference like <code>&amp;amp;</code>. The
	 * implementation must recursivly fully resolve the given entity.
	 * 
	 * @param name
	 *            The name of the XML entity reference to resolve. For
	 *            <code>&amp;amp;</code> this would be <code>amp</code>.
	 * @return A string containing the resolved text of the entity or
	 *         <code>null</code> if the entity name is unknown.
	 */
	String resolveXmlEntity(String name);
}
