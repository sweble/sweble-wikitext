package org.sweble.wikitext.parser.nodes;

public interface WtTagExtensionBody
		extends
			WtStringNode,
			WtPreproNode
{
	public static final WtTagExtensionNullBody NULL_BODY = new WtTagExtensionNullBody();
	
	public static final class WtTagExtensionNullBody
			extends
				WtNullStringNode
			implements
				WtTagExtensionBody
	{
		private static final long serialVersionUID = -1064749733891892633L;
	}
}
