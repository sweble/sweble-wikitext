package org.example;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import joptsimple.OptionException;

import org.apache.log4j.Logger;
import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.JobGeneratorFactory;
import org.sweble.wikitext.articlecruncher.JobTrace;
import org.sweble.wikitext.articlecruncher.JobTraceSet;
import org.sweble.wikitext.articlecruncher.Nexus;
import org.sweble.wikitext.articlecruncher.ProcessingNodeFactory;
import org.sweble.wikitext.articlecruncher.Processor;
import org.sweble.wikitext.articlecruncher.StorerFactory;
import org.sweble.wikitext.articlecruncher.pnodes.LocalProcessingNode;
import org.sweble.wikitext.articlecruncher.pnodes.LpnJobProcessorFactory;
import org.sweble.wikitext.articlecruncher.storers.DummyStorer;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;

import de.fau.cs.osr.utils.WrappedException;
import de.fau.cs.osr.utils.getopt.Options;

public class DumpCruncher
{
	private static final Logger logger = Logger.getLogger(DumpCruncher.class);
	
	private final Options options = new Options();
	
	private Nexus nexus;
	
	// =========================================================================
	
	public static void main(String[] args) throws Throwable
	{
		new DumpCruncher().run(args);
	}
	
	// =========================================================================
	
	private void run(String[] args) throws Throwable
	{
		if (!options(args))
			return;
		
		logger.info("Starting dump reader");
		try
		{
			setUp();
		}
		finally
		{
			Set<JobTrace> jobTraces = nexus.getJobTraces();
			for (JobTrace trace : jobTraces)
				logger.warn("Unfinished job: " + trace.toString());
			
			logger.info("Number of unfinished jobs: " + jobTraces.size());
			logger.info("Dump reader exiting");
		}
	}
	
	public void setUp() throws Throwable
	{
		nexus = new Nexus();
		
		final File dumpFile = new File(options.value("dump"));
		
		nexus.setUp(
				options.value("Nexus.InTrayCapacity", int.class),
				options.value("Nexus.ProcessedJobsCapacity", int.class),
				options.value("Nexus.OutTrayCapacity", int.class));
		
		nexus.addJobGenerator(new JobGeneratorFactory()
		{
			@Override
			public WorkerBase create(
					AbortHandler abortHandler,
					BlockingQueue<Job> inTray,
					JobTraceSet jobTraces)
			{
				try
				{
					return new DumpReaderJobGenerator(dumpFile, abortHandler, inTray, jobTraces);
				}
				catch (Exception e)
				{
					throw new WrappedException(e);
				}
			}
		});
		
		/* This is a dummy processor. It can be used in addProcessingNode()
		 * to just forward the jobs unaltered to the storer. This is useful if
		 * no processing should take place. You can, for example, simply read
		 * a dump and directly write it to a database, without further 
		 * processing.
		 * 
		final LpnJobProcessorFactory lpnJPFactory = new LpnJobProcessorFactory()
		{
			@Override
			public Processor createProcessor()
			{
				return new Processor()
				{
					@Override
					public CompletedJob process(Job job)
					{
						return null;
					}
				};
			}
		};
		 */
		
		final LpnJobProcessorFactory lpnJPFactory = new LpnJobProcessorFactory()
		{
			@Override
			public Processor createProcessor()
			{
				return new RevisionProcessor();
			}
		};
		
		nexus.addProcessingNode(new ProcessingNodeFactory()
		{
			@Override
			public WorkerBase create(
					AbortHandler abortHandler,
					BlockingQueue<Job> inTray,
					BlockingQueue<Job> completedJobs)
			{
				final int numWorkers = options.value("Nexus.NumProcessorWorkers", int.class);
				
				return new LocalProcessingNode(
						abortHandler,
						inTray,
						completedJobs,
						lpnJPFactory,
						numWorkers);
			}
		});
		
		/* If it makes sense, one could also use multiple storers
		 * to distribute the load. 
		 * 
		 * Example: If you want to write the results to a database, multiple 
		 * storeres can make sense since a single connection might be dominated 
		 * by communication overhead between this application and the database 
		 * server.
		 */
		nexus.addStorer(new StorerFactory()
		{
			@Override
			public WorkerBase create(
					AbortHandler abortHandler,
					JobTraceSet jobTraces,
					BlockingQueue<Job> outTray)
			{
				try
				{
					return new DummyStorer(abortHandler, jobTraces, outTray);
				}
				catch (Exception e)
				{
					throw new WrappedException(e);
				}
			}
		});
		
		nexus.start();
	}
	
	private boolean options(String[] args) throws IOException
	{
		options.createOption('h', "help")
				.withDescription("Print help message.")
				.create();
		
		// ---
		
		options.createOption('d', "dump")
				.withDescription("The dump file to read.")
				.withPropertyKey("DumpToDb.File")
				.withArgName("FILE")
				.withRequiredArg()
				.create();
		
		// ---
		
		options.createPropertyOnlyOption("Nexus.InTrayCapacity")
				.withDescription("The number of elements the `in tray' queue can hold.")
				.withDefault("8")
				.withArgName("N")
				.withRequiredArg()
				.create();
		
		options.createPropertyOnlyOption("Nexus.ProcessedJobsCapacity")
				.withDescription("The number of elements the `in tray' queue can hold.")
				.withDefault("8")
				.withArgName("N")
				.create();
		
		options.createPropertyOnlyOption("Nexus.OutTrayCapacity")
				.withDescription("The number of elements the `in tray' queue can hold.")
				.withDefault("8")
				.withArgName("N")
				.create();
		
		options.createOption("processor-workers")
				.withDescription("The number of processor workers.")
				.withPropertyKey("Nexus.NumProcessorWorkers")
				.withDefault("4")
				.withArgName("N")
				.create();
		
		options.createOption('P', "properties")
				.withDescription("A properties file to load additional configuration options from.")
				.withDefault("dump2db.properties")
				.withArgName("FILE")
				.withRequiredArg()
				.create();
		
		// ---
		
		try
		{
			options.parse(args);
			
			if (options.has("help"))
			{
				printHelpMessage(options);
				return false;
			}
			
			if (options.has("P"))
				options.load(new File(options.value("P")));
			
			options.expected("dump");
			
			options.optional("P");
			options.optional("Nexus.InTrayCapacity");
			options.optional("Nexus.ProcessedJobsCapacity");
			options.optional("Nexus.OutTrayCapacity");
			options.optional("Nexus.NumProcessorWorkers");
			
			options.checkForInvalidOptions();
			
			return true;
		}
		catch (OptionException e)
		{
			printArgsErrorMessage(e);
			return false;
		}
	}
	
	private void printArgsErrorMessage(OptionException e)
	{
		System.err.println(e.getMessage());
		System.err.println("Try `--help' for more information.");
	}
	
	private void printHelpMessage(Options options)
	{
		options.cmdLineHelp(System.out);
		System.out.println();
		options.propertiesHelp(System.out);
		System.out.println();
		options.fixedOptionsHelp(System.out);
		System.out.println();
	}
}
