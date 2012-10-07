package org.sweble.wikitext.parser.nodes;

public class WtXmlAttributesImpl
		extends
			WtContentNodeImpl
		implements
			WtXmlAttributes
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtXmlAttributesImpl()
	{
	}
	
	public WtXmlAttributesImpl(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_XML_ATTRIBUTES;
	}
	
	@Override
	public String getNodeName()
	{
		return WtXmlAttributes.class.getSimpleName();
	}
}
