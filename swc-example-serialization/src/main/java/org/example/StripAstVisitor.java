package org.example;

import org.sweble.wikitext.parser.nodes.WikitextNode;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ast.Location;

public final class StripAstVisitor
		extends
			AstVisitor
{
	private boolean stripAllAttributes;
	
	private boolean stripRtdAttributes;
	
	private boolean stripLocations;
	
	// =========================================================================
	
	public StripAstVisitor(
			boolean stripAllAttributes,
			boolean stripRtdAttributes,
			boolean stripLocations)
	{
		this.stripAllAttributes = stripAllAttributes;
		this.stripRtdAttributes = stripRtdAttributes;
		this.stripLocations = stripLocations;
	}
	
	// =========================================================================
	
	public void visit(WikitextNode n)
	{
		if (stripAllAttributes)
		{
			n.clearAttributes();
		}
		else if (stripRtdAttributes)
		{
			n.removeAttribute("RTD");
		}
		
		if (stripLocations)
			n.setNativeLocation((Location) null);
		
		iterate(n);
	}
}
