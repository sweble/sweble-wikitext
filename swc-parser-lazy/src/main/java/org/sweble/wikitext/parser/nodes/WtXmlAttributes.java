package org.sweble.wikitext.parser.nodes;

public interface WtXmlAttributes
		extends
			WtContentNode
{
	public static final WtXmlAttributes EMPTY = new WtEmptyXmlAttributes();
	
	public static final class WtEmptyXmlAttributes
			extends
				WtNullContentNode
			implements
				WtXmlAttributes
	{
		private static final long serialVersionUID = -1064749733891892633L;
	}
}
