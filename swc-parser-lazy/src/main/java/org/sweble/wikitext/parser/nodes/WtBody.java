package org.sweble.wikitext.parser.nodes;

public interface WtBody
		extends
			WtContentNode
{
	public static final WtNullBody NULL = new WtNullBody();
	
	public static final WtNullBody EMPTY = new WtNullBody();
	
	// =========================================================================
	
	public static final class WtNullBody
			extends
				WtNullContentNode
			implements
				WtBody
	{
		private static final long serialVersionUID = -1064749733891892633L;
	}
	
	// =========================================================================
	
	public class WtBodyImpl
			extends
				WtContentNodeImpl
			implements
				WtBody
	{
		private static final long serialVersionUID = 1L;
		
		// =====================================================================
		
		public WtBodyImpl()
		{
		}
		
		public WtBodyImpl(WtNodeList content)
		{
			super(content);
		}
		
		@Override
		public int getNodeType()
		{
			return NT_BODY;
		}
		
		@Override
		public String getNodeName()
		{
			return WtBody.class.getSimpleName();
		}
	}
}
