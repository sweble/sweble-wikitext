package org.sweble.wikitext.articlecruncher.pnodes;

import java.util.concurrent.Callable;

import org.sweble.wikitext.articlecruncher.CompletedJob;
import org.sweble.wikitext.articlecruncher.JobWithHistory;
import org.sweble.wikitext.articlecruncher.Processor;

final class LpnWorker
		implements
			Callable<CompletedJob>
{
	private final LpnJobProcessorFactory jobProcessorFactory;
	
	private final JobWithHistory job;
	
	// =========================================================================
	
	LpnWorker(LpnJobProcessorFactory jobProcessorFactory, JobWithHistory job)
	{
		this.jobProcessorFactory = jobProcessorFactory;
		this.job = job;
	}
	
	// =========================================================================
	
	@Override
	public CompletedJob call() throws Exception
	{
		// Wait for LpnDistributor to retrieve the future handle!
		synchronized (job)
		{
			job.getJob().getTrace().signOff(getClass(), null);
			
			Processor processor = jobProcessorFactory.createProcessor();
			return processor.process(job);
		}
	}
}
