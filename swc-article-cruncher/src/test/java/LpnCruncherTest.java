import static org.junit.Assert.*;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;
import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.JobGeneratorFactory;
import org.sweble.wikitext.articlecruncher.JobTrace;
import org.sweble.wikitext.articlecruncher.Nexus;
import org.sweble.wikitext.articlecruncher.ProcessingNodeFactory;
import org.sweble.wikitext.articlecruncher.Processor;
import org.sweble.wikitext.articlecruncher.StorerFactory;
import org.sweble.wikitext.articlecruncher.pnodes.LocalProcessingNode;
import org.sweble.wikitext.articlecruncher.pnodes.LpnJobProcessorFactory;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;

public class LpnCruncherTest
		extends
			CruncherTestBase
{
	private Nexus nexus;
	
	private static final int NUM_WORKERS = 256;
	
	private AtomicLong processed = new AtomicLong(0);
	
	// =========================================================================
	
	@Before
	public void before() throws Throwable
	{
		nexus = new Nexus();
		
		nexus.setUp(
				16, /* in tray capacity */
				16, /* processed jobs capacity */
				16 /* out tray capacity */);
		
		JobGeneratorFactory jobFactory = createJobFactory();
		nexus.addJobGenerator(jobFactory);
		
		ProcessingNodeFactory pnFactory = createPnFactory();
		nexus.addProcessingNode(pnFactory);
		
		StorerFactory storerFactory = createStorerFactory();
		nexus.addStorer(storerFactory);
	}
	
	// =========================================================================
	
	@Test
	public void test() throws Throwable
	{
		nexus.start();
		
		assertEquals(NUM_JOBS_TO_GENERATE, generated.get());
		
		assertEquals(NUM_JOBS_TO_GENERATE, processed.get());
		
		assertEquals(NUM_JOBS_TO_GENERATE, stored.get());
		
		Set<JobTrace> jobTraces = nexus.getJobTraces();
		assertTrue(jobTraces.isEmpty());
	}
	
	// =========================================================================
	
	private ProcessingNodeFactory createPnFactory()
	{
		return new ProcessingNodeFactory()
		{
			@Override
			public WorkerBase create(
					AbortHandler abortHandler,
					BlockingQueue<Job> inTray,
					BlockingQueue<Job> processedJobs)
			{
				return new LocalProcessingNode(
						abortHandler,
						inTray,
						processedJobs,
						createLpnFactory(),
						NUM_WORKERS);
			}
		};
	}
	
	private LpnJobProcessorFactory createLpnFactory()
	{
		return new LpnJobProcessorFactory()
		{
			@Override
			public Processor createProcessor()
			{
				return new Processor()
				{
					@Override
					public Object process(Job job)
					{
						job.signOff(getClass(), null);
						
						processed.incrementAndGet();
						
						return null;
					}
				};
			}
		};
	}
}
