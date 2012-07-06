package org.sweble.wikitext.engine;

public class RecursiveTransclusionException
		extends
			Exception
{
	
	private static final long serialVersionUID = 1L;
	
	private final PageTitle title;
	
	public RecursiveTransclusionException(PageTitle title, int i)
	{
		super("Aborted recursive transclusion after " + i + " iterations");
		this.title = title;
	}
	
	public PageTitle getTitle()
	{
		return title;
	}
	
}
