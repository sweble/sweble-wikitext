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

import org.sweble.wikitext.engine.astwom.adapters.NativeOrXmlAttributeAdapter;
import org.sweble.wikitext.engine.wom.WomNode;

public interface AttributeDescriptor
{
	/**
	 * Check an attribute's value for well-formed-ness.
	 * 
	 * @param parent
	 *            The parent node for which to verify the attribute.
	 * @param value
	 *            The value to check.
	 * @return The value to set the attribute to. Most of the time this will be
	 *         the input parameter "value". But if the value needs further
	 *         normalization (apart from XML normalization of attributes), this
	 *         method can return a normalized string to use as value.
	 * @throws IllegalArgumentException
	 *             Thrown if the given value is not well-formed.
	 */
	public abstract String verify(WomNode parent, String value) throws IllegalArgumentException;
	
	/**
	 * Ask whether this attribute can be removed from its parent node.
	 */
	public abstract boolean isRemovable();
	
	/**
	 * Whether to sync the this attribute with the AST or whether to shadow the
	 * AST attribute and only manage the WOM portion of this attribute.
	 * 
	 * @return <code>True</code> (the default) if the attribute is also managed
	 *         in the AST, <code>false</code> otherwise.
	 */
	public abstract boolean syncToAst();
	
	/**
	 * Return the normalization mode for the attribute.
	 * 
	 * @return The normalization mode.
	 */
	public abstract Normalization getNormalizationMode();
	
	/**
	 * Called after the attribute was set to perform custom alterations on WOM
	 * or AST.
	 * 
	 * @param parent
	 *            The parent node for which to verify the attribute.
	 * @param oldAttr
	 *            The old attribute node.
	 * @param newAttr
	 *            The new attribute node or <code>null</code> if the old
	 *            attribute was removed.
	 */
	public abstract void customAction(
			WomNode parent,
			NativeOrXmlAttributeAdapter oldAttr,
			NativeOrXmlAttributeAdapter newAttr);
	
	// =========================================================================
	
	public enum Normalization
	{
		/**
		 * Only convert the attribute value's WtNodeList into a string. Don't
		 * post-process the resulting string.
		 */
		NONE,
		
		/**
		 * Normalize the attribute's value after conversion according to these
		 * rules: http://www.w3.org/TR/REC-xml/#AVNormalize.
		 */
		CDATA,
		
		/**
		 * Normalize the attribute's value after conversion according to these
		 * rules: http://www.w3.org/TR/REC-xml/#AVNormalize.
		 * 
		 * Unlike the CDATA normalization, NON_CDATA normalization also
		 * collapses sequences of spaces into a single space and removes leading
		 * and trailing spaces.
		 */
		NON_CDATA
	}
}
