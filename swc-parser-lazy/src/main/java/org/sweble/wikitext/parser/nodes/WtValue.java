package org.sweble.wikitext.parser.nodes;

public interface WtValue
		extends
			WtContentNode
{
	public static final WtNullValue NULL = new WtNullValue();
	
	// =========================================================================
	
	public static final class WtNullValue
			extends
				WtNullContentNode
			implements
				WtValue
	{
		private static final long serialVersionUID = -1064749733891892633L;
	}
	
	// =========================================================================
	
	public class WtValueImpl
			extends
				WtContentNodeImpl
			implements
				WtValue
	{
		private static final long serialVersionUID = 1L;
		
		// =====================================================================
		
		public WtValueImpl()
		{
		}
		
		public WtValueImpl(WtNodeList content)
		{
			super(content);
		}
		
		@Override
		public int getNodeType()
		{
			return NT_TEMPLATE_VALUE;
		}
		
		@Override
		public String getNodeName()
		{
			return WtValue.class.getSimpleName();
		}
	}
}
