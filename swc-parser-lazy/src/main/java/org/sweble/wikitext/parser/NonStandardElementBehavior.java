package org.sweble.wikitext.parser;

public enum NonStandardElementBehavior
{
	/** No behavior for this non-HTML/non-Wikitext element has been specified. */
	UNSPECIFIED,
	
	/** Treat like a self-closing tag (e.g. a &lt;br /> or &lt;img /> tag). */
	LIKE_BR,
	
	/** Treat like a block element (e.g. &lt;div>). */
	LIKE_DIV,
	
	/** Treat like an inline element (e.g. &lt;span>). */
	LIKE_ANY_OTHER,
}
