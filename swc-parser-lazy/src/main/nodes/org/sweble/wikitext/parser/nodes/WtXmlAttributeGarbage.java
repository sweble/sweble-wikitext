package org.sweble.wikitext.parser.nodes;

/**
 * <h1>XML Attribute Garbage</h1>
 */
public class WtXmlAttributeGarbage
		extends
			WtStringContentNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtXmlAttributeGarbage()
	{
	}
	
	public WtXmlAttributeGarbage(String content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_XML_ATTRIBUTE_GARBAGE;
	}
}
