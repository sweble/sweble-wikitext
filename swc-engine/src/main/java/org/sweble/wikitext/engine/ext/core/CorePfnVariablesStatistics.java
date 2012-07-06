package org.sweble.wikitext.engine.ext.core;

import org.sweble.wikitext.engine.config.ParserFunctionGroup;

public class CorePfnVariablesStatistics
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	protected CorePfnVariablesStatistics()
	{
		super("Core - Variables - Statistics");
	}
	
	public static CorePfnVariablesStatistics group()
	{
		return new CorePfnVariablesStatistics();
	}
	
	// =========================================================================
	// ==
	// == Entire wiki
	// == --------------------
	// == TODO: {{NUMBEROFPAGES}}
	// == TODO: {{NUMBEROFARTICLES}}
	// == TODO: {{NUMBEROFFILES}}
	// == TODO: {{NUMBEROFEDITS}}
	// == TODO: {{NUMBEROFVIEWS}}
	// == TODO: {{NUMBEROFUSERS}}
	// == TODO: {{NUMBEROFADMINS}}
	// == TODO: {{NUMBEROFACTIVEUSERS}}
	// == TODO: {{PAGESINCATEGORY:categoryname}}, {{PAGESINCAT:Help}}
	// == TODO: {{NUMBERINGROUP:groupname}}, {{NUMINGROUP:groupname}}
	// == TODO: {{PAGESINNS:index}}, {{PAGESINNAMESPACE:index}}
	// ==
	// =========================================================================
}
