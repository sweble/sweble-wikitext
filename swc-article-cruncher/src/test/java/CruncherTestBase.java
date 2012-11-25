import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.JobGeneratorFactory;
import org.sweble.wikitext.articlecruncher.JobTrace;
import org.sweble.wikitext.articlecruncher.JobTraceSet;
import org.sweble.wikitext.articlecruncher.StorerFactory;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;

import de.fau.cs.osr.utils.WrappedException;

public class CruncherTestBase
{
	protected static final long NUM_JOBS_TO_GENERATE = (long) Math.pow(2, 20);
	
	protected AtomicLong generated = new AtomicLong(0);
	
	protected AtomicLong stored = new AtomicLong(0);
	
	// =========================================================================
	
	protected static final class TestJob
			extends
				Job
	{
	}
	
	// =========================================================================
	
	protected JobGeneratorFactory createJobFactory()
	{
		return new JobGeneratorFactory()
		{
			@Override
			public WorkerBase create(
					final AbortHandler abortHandler,
					final BlockingQueue<Job> inTray,
					final JobTraceSet jobTraces)
			{
				try
				{
					return new WorkerBase("JobGenerator", abortHandler)
					{
						@Override
						protected void work() throws InterruptedException
						{
							for (long i = 0; i < NUM_JOBS_TO_GENERATE; ++i)
							{
								// generate jobs out of thin air.
								Job job = new TestJob();
								generated.incrementAndGet();
								
								JobTrace trace = job.getTrace();
								trace.signOff(getClass(), null);
								
								jobTraces.add(trace);
								
								inTray.put(job);
							}
						}
					};
				}
				catch (Exception e)
				{
					throw new WrappedException(e);
				}
			}
		};
	}
	
	protected StorerFactory createStorerFactory()
	{
		return new StorerFactory()
		{
			@Override
			public WorkerBase create(
					final AbortHandler abortHandler,
					final JobTraceSet jobTraces,
					final BlockingQueue<Job> outTray)
			{
				return new WorkerBase("Storer", abortHandler)
				{
					@Override
					protected void work() throws InterruptedException
					{
						while (true)
						{
							TestJob job = (TestJob) outTray.take();
							
							JobTrace trace = job.getTrace();
							trace.signOff(getClass(), null);
							
							// do store.
							stored.incrementAndGet();
							
							if (!jobTraces.remove(trace))
								throw new InternalError("Missing job trace");
						}
					}
				};
			}
		};
	}
}
