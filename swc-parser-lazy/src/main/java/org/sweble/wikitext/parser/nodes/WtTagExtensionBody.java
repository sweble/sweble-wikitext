package org.sweble.wikitext.parser.nodes;

public interface WtTagExtensionBody
		extends
			WtStringNode,
			WtPreproNode
{
	public static final WtTagExtensionNullBody NULL_BODY = new WtTagExtensionNullBody();
	
	// =========================================================================
	
	public static final class WtTagExtensionNullBody
			extends
				WtNullStringNode
			implements
				WtTagExtensionBody
	{
		private static final long serialVersionUID = -1064749733891892633L;
	}
	
	// =========================================================================
	
	public class WtTagExtensionBodyImpl
			extends
				WtStringNodeImpl
			implements
				WtTagExtensionBody
	{
		private static final long serialVersionUID = -6588373105033239206L;
		
		// =====================================================================
		
		/**
		 * Only for use by de-serialization code.
		 */
		protected WtTagExtensionBodyImpl()
		{
			super(null);
		}
		
		public WtTagExtensionBodyImpl(String content)
		{
			super(content);
		}
	}
}
