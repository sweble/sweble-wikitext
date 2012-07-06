package org.sweble.wikitext.engine.ext.core;

import java.util.List;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.PfnArgumentMode;
import org.sweble.wikitext.lazy.parser.PageSwitch;

import de.fau.cs.osr.ptk.common.ast.AstNode;

public abstract class CorePfnBehaviorSwitch
		extends
			ParserFunctionBase
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public CorePfnBehaviorSwitch(String name)
	{
		// Page Switches don't take arguments so don't waste time with funny 
		// conversions.
		super(PfnArgumentMode.TEMPLATE_ARGUMENTS, name);
	}
	
	// =========================================================================
	
	@Override
	public final AstNode invoke(
			AstNode pageSwitch,
			ExpansionFrame frame,
			List<? extends AstNode> argsValues)
	{
		return invoke((PageSwitch) pageSwitch, frame);
	}
	
	protected abstract AstNode invoke(PageSwitch var, ExpansionFrame frame);
}
