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
package org.sweble.wikitext.engine.dom;

/**
 * An argument to a Wikitext transclusion statement.
 */
public interface DomTemplateArgument
        extends
            DomNode
{
	/**
	 * Get the name of the argument.
	 * 
	 * @return The name of the argument.
	 */
	public DomName getName();
	
	/**
	 * Set the name of the argument.
	 * 
	 * @param name
	 *            The new name of the argument.
	 * @return The old name of the argument.
	 */
	public DomName setName(DomName name);
	
	/**
	 * Get the value of the argument.
	 * 
	 * @return The name of the value.
	 */
	public DomValue getArgValue();
	
	/**
	 * Set the value of the argument.
	 * 
	 * @param value
	 *            The new value of the argument.
	 * @return The old value of the argument.
	 */
	public DomValue setArgValue(DomValue value);
}
