/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

/**
 * Alignment attribute on DIV, H1-H6, HR, P, TABLE, TD, TH and TR elements.
 */
public enum Wom3HorizAlign
{
	LEFT,
	RIGHT,
	CENTER,
	
	/**
	 * Not applicable to HR and TABLE.
	 */
	JUSTIFY,
	
	/**
	 * Only applicable to TD, TH and TR.
	 */
	CHAR
}
