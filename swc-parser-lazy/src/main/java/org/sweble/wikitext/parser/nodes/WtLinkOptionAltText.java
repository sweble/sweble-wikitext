package org.sweble.wikitext.parser.nodes;

public interface WtLinkOptionAltText
		extends
			WtContentNode
{
	public static final WtNullLinkOptionAltText NULL = new WtNullLinkOptionAltText();
	
	// =========================================================================
	
	public static final class WtNullLinkOptionAltText
			extends
				WtNullContentNode
			implements
				WtLinkOptionAltText
	{
		private static final long serialVersionUID = -1064749733891892633L;
	}
	
	// =========================================================================
	
	public static final class WtLinkOptionAltTextImpl
			extends
				WtContentNodeImpl
			implements
				WtLinkOptionAltText
	{
		private static final long serialVersionUID = 1L;
		
		// =====================================================================
		
		public WtLinkOptionAltTextImpl()
		{
		}
		
		public WtLinkOptionAltTextImpl(WtNodeList content)
		{
			super(content);
		}
		
		@Override
		public int getNodeType()
		{
			return NT_LINK_OPTION_ALT_TEXT;
		}
	}
}
