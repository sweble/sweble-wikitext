package org.sweble.wikitext.parser.nodes;

public interface WtName
		extends
			WtContentNode
{
	public static final WtNullName NULL_NAME = new WtNullName();
	
	public static final class WtNullName
			extends
				WtNullContentNode
			implements
				WtName
	{
		private static final long serialVersionUID = -1064749733891892633L;
	}
}
