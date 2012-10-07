package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstStringNode;

public interface WtStringNode
		extends
			WtNode,
			AstStringNode<WtNode>
{
	public abstract class WtNullStringNode
			extends
				WtNullNode
			implements
				WtStringNode
	{
		private static final long serialVersionUID = 2465445739660029292L;
		
		@Override
		public String getContent()
		{
			return "";
		}
		
		@Override
		public String setContent(String content)
		{
			throw new UnsupportedOperationException(genMsg());
		}
	}
}
