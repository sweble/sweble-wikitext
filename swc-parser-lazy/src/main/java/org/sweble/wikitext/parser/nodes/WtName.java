package org.sweble.wikitext.parser.nodes;

public interface WtName
		extends
			WtContentNode
{
	public static final WtNullName NULL = new WtNullName();
	
	// =========================================================================
	
	public static final class WtNullName
			extends
				WtNullContentNode
			implements
				WtName
	{
		private static final long serialVersionUID = -1064749733891892633L;
	}
	
	// =========================================================================
	
	public class WtNameImpl
			extends
				WtContentNodeImpl
			implements
				WtName
	{
		private static final long serialVersionUID = 1L;
		
		// =====================================================================
		
		public WtNameImpl()
		{
		}
		
		public WtNameImpl(WtNodeList content)
		{
			super(content);
		}
		
		@Override
		public int getNodeType()
		{
			return NT_TEMPLATE_NAME;
		}
		
		@Override
		public String getNodeName()
		{
			return WtName.class.getSimpleName();
		}
	}
}
