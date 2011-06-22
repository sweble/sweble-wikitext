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
package org.sweble.wikitext.engine.wom;

/**
 * A template substitution parameter.
 * 
 * Corresponds to the XWML 1.0 element "param".
 * 
 * <b>Child elements:</b> name default?
 */
public interface WomParam
        extends
            WomProcessingInstruction
{
	/**
	 * Get the name of the parameter.
	 * 
	 * Operates on the first &lt;name> element found among this node's children.
	 * 
	 * @return The name of the parameter.
	 */
	public WomName getName();
	
	/**
	 * Set the name of the parameter.
	 * 
	 * Operates on the first &lt;name> element found among this node's children.
	 * 
	 * @param name
	 *            The new name of the parameter.
	 * @return The old name of the parameter.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is given as name.
	 */
	public WomName setName(WomName name) throws NullPointerException;
	
	/**
	 * Get the default value of the parameter.
	 * 
	 * Operates on the first &lt;default> element found among this node's
	 * children.
	 * 
	 * @return The default value of the parameter or <code>null</code> if no
	 *         default value is specified.
	 */
	public WomValue getDefault();
	
	/**
	 * Set the default value of the parameter.
	 * 
	 * Operates on the first &lt;default> element found among this node's
	 * children.
	 * 
	 * @param value
	 *            The new default value of the parameter or <code>null</code> to
	 *            remove the default value.
	 * @return The old default value of the parameter.
	 */
	public WomValue setDefault(WomValue value);
}
