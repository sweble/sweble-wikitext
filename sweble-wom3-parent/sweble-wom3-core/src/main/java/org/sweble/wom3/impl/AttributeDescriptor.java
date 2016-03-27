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

public abstract class AttributeDescriptor
		implements
			AttributeVerificationAndConverion,
			AttributeCustomAction
{
	public static final int REMOVABLE = 0x01;

	public static final int READ_ONLY = 0x02;

	public static final int NORMALIZATION_MASK = 0x04 | 0x08;

	public static final int NORMALIZATION_NONE = 0x00;

	public static final int NORMALIZATION_NON_CDATA = 0x04;

	public static final int NORMALIZATION_CDATA = 0x08;

	public static final int CUSTOM_ACTION = 0x10;

	// =========================================================================

	/**
	 * The default implementation only works for attributes whose native value
	 * is of type string.
	 * 
	 * @param parent
	 *            The node that owns or will own the attribute.
	 * @param verified
	 *            Stores the values to convert. The converted and verified
	 *            values will be written into this object as well.
	 * @return {@code true} when the attribute should be kept, {@code false} if
	 *         the attributes should be removed.
	 */
	@Override
	public boolean verifyAndConvert(
			Backbone parent,
			NativeAndStringValuePair verified)
	{
		if (verified.strValue != null)
			verified.value = verified.strValue;
		else
			verified.strValue = String.valueOf(verified.value);

		return true;

	}

	/**
	 * Called after the attribute was (re-)set to perform custom alterations on
	 * WOM. Also called when an attribute is renamed. In this case it is called
	 * on the old descriptor with the {@code newAttr} parameter set to
	 * {@code null}. Then it is called on the new descriptor with the
	 * {@code oldAttr} set to {@code null}. If the old and new descriptor are
	 * the same (== equality) then it is only called once on the new descriptor
	 * with both {@code oldAttr} and {@code newAttr} set.
	 * 
	 * @param parent
	 *            The parent node for which to verify the attribute.
	 * @param oldAttr
	 *            The old attribute node.
	 * @param newAttr
	 *            The new attribute node or <code>null</code> if the old
	 *            attribute was removed.
	 */
	@Override
	public void customAction(
			Wom3Node parent,
			AttributeBase oldAttr,
			AttributeBase newAttr)
	{
	}

	/**
	 * @return A set of flags that describe this attributes behavior.
	 */
	public abstract int getFlags();

	// =========================================================================

	/**
	 * Ask whether this attribute can be removed from its parent node.
	 */
	public boolean isRemovable()
	{
		return (getFlags() & REMOVABLE) != 0;
	}

	/**
	 * Ask whether this attribute cannot be changed.
	 */
	public boolean isReadOnly()
	{
		return (getFlags() & READ_ONLY) != 0;
	}

	/**
	 * Return the normalization mode for the attribute.
	 */
	public Normalization getNormalizationMode()
	{
		return translateNormalization(getFlags() & NORMALIZATION_MASK);
	}

	/**
	 * Whether the descriptor implements the {@link customAction} method.
	 */
	public boolean hasCustomAction()
	{
		return (getFlags() & CUSTOM_ACTION) != 0;
	}

	// =========================================================================

	public static int makeFlags(
			boolean removable,
			boolean readOnly,
			boolean customAction,
			Normalization normalization)
	{
		return (removable ? REMOVABLE : 0)
				| (readOnly ? READ_ONLY : 0)
				| translateNormalization(normalization)
				| (customAction ? CUSTOM_ACTION : 0);
	}

	// =========================================================================

	public static int translateNormalization(Normalization normalization)
	{
		switch (normalization)
		{
			case CDATA:
				return NORMALIZATION_CDATA;

			case NON_CDATA:
				return NORMALIZATION_NON_CDATA;

			case NONE:
				return NORMALIZATION_NONE;

			default:
				throw new AssertionError();
		}
	}

	public static Normalization translateNormalization(int flag) throws AssertionError
	{
		switch (flag)
		{
			case NORMALIZATION_CDATA:
				return Normalization.NON_CDATA;

			case NORMALIZATION_NON_CDATA:
				return Normalization.NON_CDATA;

			case NORMALIZATION_NONE:
				return Normalization.NONE;

			default:
				throw new AssertionError();
		}
	}

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
