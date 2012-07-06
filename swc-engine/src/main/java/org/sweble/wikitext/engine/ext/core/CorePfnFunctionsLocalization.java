package org.sweble.wikitext.engine.ext.core;

import org.sweble.wikitext.engine.config.ParserFunctionGroup;

public class CorePfnFunctionsLocalization
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	protected CorePfnFunctionsLocalization()
	{
		super("Core - Parser Functions - Localization");
	}
	
	public static CorePfnFunctionsLocalization group()
	{
		return new CorePfnFunctionsLocalization();
	}
	
	// =========================================================================
	// ==
	// == TODO: {{plural:2|is|are}}
	// == TODO: {{grammar:N|noun}}
	// == TODO: {{gender:username
	// ==           |return text if user is male
	// ==           |return text if user is female
	// ==           |return text if user hasn't defined their gender
	// ==       }}
	// == TODO: {{int:message name}}
	// == TODO: {{int:editsectionhint|MediaWiki}}
	// ==
	// =========================================================================
}
