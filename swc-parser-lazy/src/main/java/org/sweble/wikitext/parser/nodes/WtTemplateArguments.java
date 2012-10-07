package org.sweble.wikitext.parser.nodes;

public interface WtTemplateArguments
		extends
			WtContentNode
{
	public static final WtNullTemplateArguments EMPTY = new WtNullTemplateArguments();
	
	// =========================================================================
	
	public static final class WtNullTemplateArguments
			extends
				WtNullContentNode
			implements
				WtTemplateArguments
	{
		private static final long serialVersionUID = -1064749733891892633L;
	}
	
	// =========================================================================
	
	public static final class WtTemplateArgumentsImpl
			extends
				WtContentNodeImpl
			implements
				WtTemplateArguments
	{
		private static final long serialVersionUID = 1L;
		
		// =====================================================================
		
		public WtTemplateArgumentsImpl()
		{
		}
		
		public WtTemplateArgumentsImpl(WtNodeList content)
		{
			super(content);
		}
		
		@Override
		public int getNodeType()
		{
			return NT_TEMPLATE_ARGUMENTS;
		}
		
		@Override
		public String getNodeName()
		{
			return WtTemplateArguments.class.getSimpleName();
		}
	}
}
