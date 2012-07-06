package org.sweble.wikitext.engine.ext.core;

import java.util.List;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.PfnArgumentMode;
import org.sweble.wikitext.lazy.preprocessor.Template;

import de.fau.cs.osr.ptk.common.ast.AstNode;

public abstract class CorePfnVariable
		extends
			ParserFunctionBase
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public CorePfnVariable(String name)
	{
		// Most variables don't take arguments so don't waste time with funny 
		// conversions.
		super(PfnArgumentMode.TEMPLATE_ARGUMENTS, name);
	}
	
	public CorePfnVariable(PfnArgumentMode argMode, String name)
	{
		super(argMode, name);
	}
	
	// =========================================================================
	
	@Override
	public final AstNode invoke(
			AstNode var,
			ExpansionFrame frame,
			List<? extends AstNode> argsValues)
	{
		return invoke((Template) var, frame, argsValues);
	}
	
	public AstNode invoke(
			Template var,
			ExpansionFrame frame,
			List<? extends AstNode> argsValues)
	{
		return invoke(var, frame);
	}
	
	protected AstNode invoke(Template var, ExpansionFrame frame)
	{
		return var;
	}
}
