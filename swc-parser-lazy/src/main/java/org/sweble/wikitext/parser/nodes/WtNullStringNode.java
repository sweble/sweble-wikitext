package org.sweble.wikitext.parser.nodes;

public class WtNullStringNode
		extends
			WtNullNode
		implements
			WtStringNode
{
	private static final long serialVersionUID = 2465445739660029292L;
	
	@Override
	public String getContent()
	{
		return "";
	}
	
	@Override
	public String setContent(String content)
	{
		throw new UnsupportedOperationException(genMsg());
	}
}
