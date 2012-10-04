package org.sweble.wikitext.parser.nodes;

import java.util.List;

import org.sweble.wikitext.parser.WtEntityMap;

import de.fau.cs.osr.ptk.common.Warning;

/**
 * <h1>WtPreproWikitextPage</h1> <h2>Grammar</h2>
 */
public class WtPreproWikitextPage
		extends
			WtPage
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtPreproWikitextPage()
	{
	}
	
	public WtPreproWikitextPage(
			WtNodeList content,
			List<Warning> warnings,
			WtEntityMap entityMap)
	{
		super(content, warnings, entityMap);
	}
	
	public WtPreproWikitextPage(WtNodeList content, List<Warning> warnings)
	{
		super(content, warnings);
	}
}
