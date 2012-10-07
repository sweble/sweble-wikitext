package org.sweble.wikitext.parser.nodes;

public interface WtLinkTitle
		extends
			WtContentNode
{
	public static final WtNullLinkTitle NULL = new WtNullLinkTitle();
	
	// =========================================================================
	
	public static final class WtNullLinkTitle
			extends
				WtNullContentNode
			implements
				WtLinkTitle
	{
		private static final long serialVersionUID = -1064749733891892633L;
	}
	
	// =========================================================================
	
	public class WtLinkTitleImpl
			extends
				WtContentNodeImpl
			implements
				WtLinkTitle
	{
		private static final long serialVersionUID = 1L;
		
		// =====================================================================
		
		public WtLinkTitleImpl()
		{
		}
		
		public WtLinkTitleImpl(WtNodeList content)
		{
			super(content);
		}
		
		@Override
		public int getNodeType()
		{
			return NT_LINK_TITLE;
		}
		
		@Override
		public String getNodeName()
		{
			return WtLinkTitle.class.getSimpleName();
		}
	}
}
