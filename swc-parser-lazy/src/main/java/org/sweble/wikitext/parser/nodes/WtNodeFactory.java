package org.sweble.wikitext.parser.nodes;

public interface WtNodeFactory
{
	public HorizontalRule wtnHr(int dashCount);
	
	public ExternalLink wtnExtLink();
}
