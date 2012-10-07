package org.sweble.wikitext.parser.nodes;

public interface WtValue
		extends
			WtContentNode
{
	public static final WtNullValue NULL_VALUE = new WtNullValue();
	
	public static final class WtNullValue
			extends
				WtNullContentNode
			implements
				WtValue
	{
		private static final long serialVersionUID = -1064749733891892633L;
	}
}
