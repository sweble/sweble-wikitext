package org.sweble.wikitext.parser.nodes;

public interface WtLinkTitle
		extends
			WtContentNode
{
	public static final WtNullLinkTitle NULL_TITLE = new WtNullLinkTitle();
	
	public static final class WtNullLinkTitle
			extends
				WtNullContentNode
			implements
				WtLinkTitle
	{
		private static final long serialVersionUID = -1064749733891892633L;
	}
}
