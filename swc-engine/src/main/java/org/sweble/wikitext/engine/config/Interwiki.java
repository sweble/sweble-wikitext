package org.sweble.wikitext.engine.config;

import java.net.URL;

import org.sweble.wikitext.engine.PageTitle;

public interface Interwiki
{
	
	public abstract String getPrefix();
	
	public abstract URL getUrl(PageTitle title);
	
	public abstract boolean isLocal();
	
	public abstract boolean isTrans();
	
}
