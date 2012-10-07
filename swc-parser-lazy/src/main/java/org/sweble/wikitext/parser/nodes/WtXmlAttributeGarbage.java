package org.sweble.wikitext.parser.nodes;

public class WtXmlAttributeGarbage
		extends
			WtStringNodeImpl
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtXmlAttributeGarbage()
	{
		super(null);
	}
	
	public WtXmlAttributeGarbage(String content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_XML_ATTRIBUTE_GARBAGE;
	}
}
