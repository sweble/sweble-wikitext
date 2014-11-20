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
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Node;

public interface AttributeDescriptor
{
	/**
	 * @param parent
	 *            The node that owns or will own the attribute.
	 * @param verified
	 *            Write the string value to convert into this object. The
	 *            converted and verified values will be written into this object
	 *            as well.
	 * @return {@code true} when the attribute should be kept, {@code false} if
	 *         the attributes should be removed.
	 */
	public abstract boolean verifyAndConvert(
			Backbone parent,
			NativeAndStringValuePair verified);
	
	/**
	 * Ask whether this attribute can be removed from its parent node.
	 */
	public abstract boolean isRemovable();
	
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
			Wom3Node parent,
			AttributeBase oldAttr,
			AttributeBase newAttr);
	
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
