package org.sweble.wikitext.engine.output;

import org.sweble.wikitext.engine.PageTitle;

public interface HtmlRendererCallback
{
	public MediaInfo getMediaInfo(String title, int width, int height) throws Exception;

	public boolean resourceExists(PageTitle target);
}
