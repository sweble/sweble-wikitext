package org.sweble.wikitext.engine.ext.core;

import java.util.List;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.PfnArgumentMode;
import org.sweble.wikitext.lazy.preprocessor.Template;

import de.fau.cs.osr.ptk.common.ast.AstNode;

public abstract class CorePfnFunction
		extends
			ParserFunctionBase
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public CorePfnFunction(PfnArgumentMode argMode, String name)
	{
		super(argMode, name);
	}
	
	public CorePfnFunction(String name)
	{
		super(name);
	}
	
	// =========================================================================
	
	@Override
	public final AstNode invoke(
			AstNode pfn,
			ExpansionFrame frame,
			List<? extends AstNode> argsValues)
	{
		return invoke((Template) pfn, frame, argsValues);
	}
	
	public abstract AstNode invoke(
			Template pfn,
			ExpansionFrame frame,
			List<? extends AstNode> argsValues);
}
