package org.sweble.wikitext.parser.utils;

import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;

public interface AstTextUtils
{
	
	/**
	 * When converting an AST to text, the conversion will abort with a
	 * StringConversionException if it encounters an unresolved XML entity
	 * reference. If a partial conversion is performed, the conversion will stop
	 * at a unresolved XML entity reference.
	 */
	public static final int FAIL_ON_UNRESOLVED_ENTITY_REF = 1;
	
	public static final int AST_TO_TEXT_LAST_OPTION = 1;
	
	public String astToText(WtNode node) throws StringConversionException;
	
	public String astToText(WtNode node, int... options) throws StringConversionException;
	
	public PartialConversion astToTextPartial(WtNode node);
	
	public PartialConversion astToTextPartial(WtNode node, int... options);
	
	public interface PartialConversion
	{
		/**
		 * Concatenated text of the first nodes which could be converted.
		 */
		public String getText();
		
		/**
		 * The remaining nodes, the first of which stopped the conversion.
		 */
		public WtNodeList getTail();
	}
	
}
