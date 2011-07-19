package org.sweble.wikitext.engine.dom;

/**
 * Alignment attribute on DIV, HR, P, TABLE, TD and TH elements.
 */
public enum DomAlign
{
	LEFT,
	RIGHT,
	CENTER,
	
	/**
	 * Not applicable to HR and TABLE.
	 */
	JUSTIFY,
	
	/**
	 * Only applicable to TD and TH.
	 */
	CHAR
}
