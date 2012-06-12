package org.sweble.wikitext.engine;

public enum PfnArgumentMode
{
	/**
	 * This is the normal mode. The original template arguments (name, value)
	 * will be collapsed into a single value of the form "$name=$value". No
	 * expansion is done.
	 */
	UNEXPANDED_VALUES,
	
	/**
	 * The original template arguments will be collapsed as is done by
	 * EXPANDED_AND_STRIPPED_VALUES. Then the collapsed value is expanded,
	 * converted to a string and trimmed.
	 */
	EXPANDED_AND_TRIMMED_VALUES,
	
	/**
	 * The original template arguments are left as is.
	 */
	TEMPLATE_ARGUMENTS,
}
