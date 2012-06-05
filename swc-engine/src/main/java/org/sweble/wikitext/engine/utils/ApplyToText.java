package org.sweble.wikitext.engine.utils;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.Text;

public class ApplyToText
{
	private Functor fn;
	
	// =========================================================================
	
	public ApplyToText(Functor fn)
	{
		this.fn = fn;
	}
	
	// =========================================================================
	
	public void go(AstNode arg0)
	{
		if (arg0.getNodeType() == AstNode.NT_TEXT)
		{
			apply((Text) arg0);
		}
		else
		{
			for (AstNode n : arg0)
			{
				if (n == null)
					continue;
				
				if (n.isNodeType(AstNode.NT_TEXT))
				{
					apply((Text) n);
				}
				else
				{
					go(n);
				}
			}
		}
	}
	
	private void apply(Text arg0)
	{
		arg0.setContent(fn.apply(arg0.getContent()));
	}
	
	// =========================================================================
	
	public interface Functor
	{
		public String apply(String text);
	}
}
