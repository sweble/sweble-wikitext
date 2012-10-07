package org.sweble.wikitext.parser.nodes;

public interface WtLinkTarget
		extends
			WtNode
{
	public static final WtLinkTarget NULL = new WtNullLink();
	
	// =========================================================================
	
	public LinkTargetType getTargetType();
	
	// =========================================================================
	
	public static enum LinkTargetType
	{
		/** The "link=X" argument was not present */
		DEFAULT,
		PAGE,
		URL,
		/** The "link=" argument was empty */
		NO_LINK
	}
	
	// =========================================================================
	
	public static final class WtNullLink
			extends
				WtNullNode
			implements
				WtLinkTarget
	{
		private static final long serialVersionUID = 4433767404703646519L;
		
		@Override
		public LinkTargetType getTargetType()
		{
			throw new UnsupportedOperationException("NullLink does not have a type!");
		}
	}
}
