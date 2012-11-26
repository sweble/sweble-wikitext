package org.example;

import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.Processor;
import org.sweble.wikitext.engine.CompilerException;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngine;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngCompiledPage;
import org.sweble.wikitext.parser.parser.LinkTargetException;

public class RevisionProcessor
		implements
			Processor
{
	private final DumpCruncher dumpCruncher;
	
	public RevisionProcessor(DumpCruncher dumpCruncher)
	{
		this.dumpCruncher = dumpCruncher;
	}
	
	@Override
	public Object process(Job job) throws LinkTargetException, CompilerException
	{
		Gui gui = dumpCruncher.getGui();
		gui.processingStarted();
		gui.redrawLater();
		
		RevisionJob revJob = (RevisionJob) job;
		
		WikiConfig config = dumpCruncher.getWikiConfig();
		
		// Instantiate a compiler for wiki pages
		WtEngine engine = new WtEngine(config);
		
		// Retrieve a page
		PageTitle pageTitle = PageTitle.make(config, revJob.getPageTitle());
		
		PageId pageId = new PageId(pageTitle, revJob.getId().longValue());
		
		String wikitext = revJob.getTextText();
		
		// Compile the retrieved page
		EngCompiledPage cp = engine.postprocess(pageId, wikitext, null);
		
		gui.processingFinished();
		gui.redrawLater();
		
		return cp;
	}
}
