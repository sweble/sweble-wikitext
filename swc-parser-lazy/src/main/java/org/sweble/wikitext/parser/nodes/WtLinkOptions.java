package org.sweble.wikitext.parser.nodes;

public interface WtLinkOptions
		extends
			WtContentNode
{
	public static final WtLinkOptions EMPTY = new WtEmptyLinkOptions();
	
	public static final class WtEmptyLinkOptions
			extends
				WtNullContentNode
			implements
				WtLinkOptions
	{
		private static final long serialVersionUID = -1064749733891892633L;
	}
}
