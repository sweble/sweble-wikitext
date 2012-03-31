package org.sweble.wikitext.articlecruncher;

public interface Processor
{
	public CompletedJob process(JobWithHistory job);
	
}
