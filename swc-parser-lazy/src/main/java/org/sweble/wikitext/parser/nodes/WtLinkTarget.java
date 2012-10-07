package org.sweble.wikitext.parser.nodes;

public interface WtLinkTarget
		extends
			WtNode
{
	public static final WtLinkTarget DONT_LINK = new WtDontLink();
	
	// =========================================================================
	
	public LinkTargetType getTargetType();
	
	// =========================================================================
	
	public static enum LinkTargetType
	{
		PAGE,
		URL,
		DONT_LINK
	}
	
	// =========================================================================
	
	public static final class WtDontLink
			extends
				WtNullNode
			implements
				WtLinkTarget
	{
		private static final long serialVersionUID = 4433767404703646519L;
		
		@Override
		public LinkTargetType getTargetType()
		{
			return LinkTargetType.DONT_LINK;
		}
	}
}
