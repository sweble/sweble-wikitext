package org.sweble.wikitext.parser.nodes;

public interface WtBody
		extends
			WtContentNode
{
	public static final WtNullBody NULL_BODY = new WtNullBody();
	
	public static final class WtNullBody
			extends
				WtNullContentNode
			implements
				WtBody
	{
		private static final long serialVersionUID = -1064749733891892633L;
	}
}
