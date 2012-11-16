package org.example;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import joptsimple.OptionException;

import org.apache.log4j.Logger;
import org.sweble.wikitext.articlecruncher.CompletedJob;
import org.sweble.wikitext.articlecruncher.JobGeneratorFactory;
import org.sweble.wikitext.articlecruncher.JobTrace;
import org.sweble.wikitext.articlecruncher.JobTraceSet;
import org.sweble.wikitext.articlecruncher.JobWithHistory;
import org.sweble.wikitext.articlecruncher.Nexus;
import org.sweble.wikitext.articlecruncher.ProcessingNodeFactory;
import org.sweble.wikitext.articlecruncher.StorerFactory;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;

import de.fau.cs.osr.utils.WrappedException;
import de.fau.cs.osr.utils.getopt.Options;

public class DumpToDb
{
	private static final Logger logger = Logger.getLogger(DumpToDb.class);
	
	private final Options options = new Options();
	
	// =========================================================================
	
	public static void main(String[] args) throws Throwable
	{
		new DumpToDb().run(args);
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
			Set<JobTrace> jobTraces = Nexus.get().getJobTraces();
			for (JobTrace trace : jobTraces)
				logger.warn("Unfinished job: " + trace.toString());
			
			logger.info("Number of unfinished jobs: " + jobTraces.size());
			logger.info("Dump reader exiting");
		}
	}
	
	public void setUp() throws Throwable
	{
		Nexus nexus = Nexus.get();
		
		final File dumpFile = new File(options.value("dump"));
		
		nexus.setUp(
				options.value("Nexus.InTrayCapacity", int.class),
				options.value("Nexus.CompletedJobsCapacity", int.class),
				options.value("Nexus.OutTrayCapacity", int.class));
		
		nexus.addJobGenerator(new JobGeneratorFactory()
		{
			@Override
			public WorkerBase create(
					AbortHandler abortHandler,
					BlockingQueue<JobWithHistory> inTray,
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
					public CompletedJob process(JobWithHistory jobWithHistory)
					{
						Job job = jobWithHistory.getJob();
						Result result = new Result(job, (Object) null);
						return new CompletedJob(job, result);
					}
				};
			}
		};
		 */
		
		nexus.addProcessingNode(new ProcessingNodeFactory()
		{
			@Override
			public WorkerBase create(
					AbortHandler abortHandler,
					BlockingQueue<JobWithHistory> inTray,
					BlockingQueue<JobWithHistory> completedJobs)
			{
				/* If you have more or fewer CPUs you can alter the number of 
				 * workers.
				 */
				final int numWorkers = 4;
				return new RevisionProcessor(
						abortHandler,
						inTray,
						completedJobs,
						numWorkers);
				
				/* This code would use the dummy processor (lpnJPFactory) 
				 * instead.
				 * 
				final int numWorkers = 1;
				return new LocalProcessingNode(
						abortHandler,
						inTray,
						completedJobs,
						lpnJPFactory,
						numWorkers);
				 */
			}
		});
		
		nexus.addStorer(new StorerFactory()
		{
			@Override
			public WorkerBase create(
					AbortHandler abortHandler,
					JobTraceSet jobTraces,
					BlockingQueue<CompletedJob> outTray)
			{
				try
				{
					return new RevisionProcessor(
							abortHandler,
							jobTraces,
							outTray,
							options.propertySubset("ConnectionProperties"),
							options.value("store-workers", int.class));
					
					//return new DummyStorer(abortHandler, jobTraces, outTray);
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
		
		/*
		options.createOption("quiet")
				.withDescription("Suppress progress messages.")
				.create();
		*/
		
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
		
		options.createPropertyOnlyOption("Nexus.CompletedJobsCapacity")
				.withDescription("The number of elements the `in tray' queue can hold.")
				.withDefault("8")
				.withArgName("N")
				.create();
		
		options.createPropertyOnlyOption("Nexus.OutTrayCapacity")
				.withDescription("The number of elements the `in tray' queue can hold.")
				.withDefault("8")
				.withArgName("N")
				.create();
		
		options.createOption("store-workers")
				.withDescription("The number of store workers.")
				.withPropertyKey("Nexus.NumStorerWorkers")
				.withDefault("16")
				.withArgName("N")
				.create();
		
		options.createOption('P', "properties")
				.withDescription("A properties file to load additional configuration options from.")
				.withDefault("dump2db.properties")
				.withArgName("FILE")
				.withRequiredArg()
				.create();
		
		// ---
		
		options.createFixedValueOption("ConnectionProperties.MaxActive", "100");
		
		options.createFixedValueOption("ConnectionProperties.MaxWait", "10000");
		
		options.createFixedValueOption("ConnectionProperties.TestOnBorrow", "true");
		
		options.createOption("username")
				.withDescription("Database username.")
				.withPropertyKey("ConnectionProperties.Username")
				.withArgName("USERNAME")
				.withRequiredArg()
				.create();
		
		options.createOption("password")
				.withDescription("Database password.")
				.withPropertyKey("ConnectionProperties.Password")
				.withArgName("PASSWORD")
				.withRequiredArg()
				.create();
		
		options.createOption("url")
				.withDescription("Database url (eg: jdbc:postgresql://localhost:5432/<DB>).")
				.withPropertyKey("ConnectionProperties.Url")
				.withArgName("URL")
				.withRequiredArg()
				.create();
		
		options.createOption("driver-class-name")
				.withDescription("The JDBC driver class.")
				.withPropertyKey("ConnectionProperties.DriverClassName")
				.withDefault("org.postgresql.Driver")
				.withArgName("CLASS")
				.withRequiredArg()
				.create();
		
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
			options.expected("username");
			options.expected("password");
			options.expected("url");
			
			//options.optional("quiet");
			options.optional("P");
			options.optional("Nexus.InTrayCapacity");
			options.optional("Nexus.CompletedJobsCapacity");
			options.optional("Nexus.OutTrayCapacity");
			options.optional("store-workers");
			
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
