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
 * A template substitution parameter.
 */
public interface DomTemplateParameter
        extends
            DomProcessingInstruction
{
	/**
	 * Get the name of the parameter.
	 * 
	 * @return The name of the parameter.
	 */
	public DomName getName();
	
	/**
	 * Set the name of the parameter.
	 * 
	 * @param name
	 *            The new name of the parameter.
	 * @return The old name of the parameter.
	 */
	public DomName setName(DomName name);
	
	/**
	 * Get the default value of the parameter.
	 * 
	 * @return The default value of the parameter or <code>null</code> if no
	 *         default value is specified.
	 */
	public DomValue getDefault();
	
	/**
	 * Set the default value of the parameter.
	 * 
	 * @param value
	 *            The new default value of the parameter.
	 * @return The old default value of the parameter.
	 */
	public DomValue setDefault(DomValue value);
}
