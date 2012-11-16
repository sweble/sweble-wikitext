package org.example;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.sweble.wikitext.articlecruncher.JobWithHistory;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.ExecutorType;
import org.sweble.wikitext.articlecruncher.utils.MyExecutorService;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;
import org.sweble.wikitext.articlecruncher.utils.WorkerSynchronizer;

/**
 * 
 */
public class RevisionProcessor
		extends
			WorkerBase
{
	private BlockingQueue<JobWithHistory> inTray;
	
	private BlockingQueue<JobWithHistory> completedJobs;
	
	private final int numWorkers;
	
	// =========================================================================
	
	public RevisionProcessor(
			AbortHandler abortHandler,
			BlockingQueue<JobWithHistory> inTray,
			BlockingQueue<JobWithHistory> completedJobs,
			int numWorkers)
	{
		super(RevisionProcessor.class.getSimpleName(), abortHandler);
		
		this.numWorkers = numWorkers;
		this.inTray = inTray;
		this.completedJobs = completedJobs;
	}
	
	@Override
	protected void work() throws Throwable
	{
		AbortHandler abortHandler = new AbortHandler()
		{
			@Override
			public void notify(Throwable t)
			{
				stop();
			}
		};
		
		MyExecutorService executor = null;
		
		List<WorkerBase> workers = new LinkedList<WorkerBase>();
		
		executor = new MyExecutorService(
				ExecutorType.CACHED_THREAD_POOL,
				getLogger());
		
		try
		{
			WorkerSynchronizer synchronizer = new WorkerSynchronizer();
			
			for (int i = 0; i < numWorkers; ++i)
			{
				SqlStorerWorker worker = new SqlStorerWorker(
						abortHandler,
						inTray,
						completedJobs);
				
				workers.add(worker);
				
				worker.start(executor, synchronizer);
			}
			
			synchronizer.waitForAll(numWorkers);
		}
		finally
		{
			if (executor != null)
			{
				for (WorkerBase worker : workers)
					worker.stop();
				
				executor.shutdownAndAwaitTermination();
			}
		}
	}
}
