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
		NONE,
		CDATA,
		NON_CDATA
	}
}
