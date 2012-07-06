package org.sweble.wikitext.engine.config;

import java.util.TreeSet;

public interface I18nAlias
{
	
	public abstract String getId();
	
	public abstract Boolean isCaseSensitive();
	
	public abstract TreeSet<String> getAliases();
	
}
