/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.denplusplus.wikipedia.dumpcruncher;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;

import de.fau.cs.osr.utils.WrappedException;
import de.fau.cs.osr.utils.getopt.Options;
import joptsimple.OptionException;

public class DumpCruncher
{
	private static final Logger logger = Logger.getLogger(DumpCruncher.class);
	
	private final Options options = new Options();
	
	private Nexus nexus;
	
	private Gui gui;
	
	private WikiConfig wikiConfig;
	
	private InputStream is;
	
	private OutputStream osCars;
	private OutputStreamWriter swCars;
	private OutputStream osPersons;
	private OutputStreamWriter swPersons;
	
	private AtomicInteger matchedCount;

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
		
		logger.info("Starting dump cruncher");
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
			logger.info("Dump cruncher exiting");
		}
	}
	
	public void setUp() throws Throwable
	{
		gui = new Gui(this);
		
		nexus = new Nexus();
		
		final String dumpFilePath = options.value("dump");
		final File dumpFile = new File(dumpFilePath);
		
		nexus.setUp(
				options.value("Nexus.InTrayCapacity", int.class),
				options.value("Nexus.ProcessedJobsCapacity", int.class),
				options.value("Nexus.OutTrayCapacity", int.class));
		
		is = new BufferedInputStream(new FileInputStream(dumpFile));
		osCars = new BufferedOutputStream(new FileOutputStream("cars.wiki"));
		swCars = new OutputStreamWriter(osCars);
		osPersons = new BufferedOutputStream(new FileOutputStream("persons.wiki"));
		swPersons  = new OutputStreamWriter(osPersons);
		matchedCount = new AtomicInteger();
		
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
					return new DumpReaderJobGenerator(
							DumpCruncher.this,
							is,
							dumpFilePath,
							abortHandler,
							inTray,
							jobTraces);
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
		
		wikiConfig = DefaultConfigEnWp.generate();
		
		final LpnJobProcessorFactory lpnJPFactory = new LpnJobProcessorFactory()
		{
			@Override
			public Processor createProcessor()
			{
				return new RevisionProcessor(DumpCruncher.this);
			}
			
			@Override
			public String getProcessorNameTemplate()
			{
				return "Processor-%02d";
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
				final int numWorkers = options.value("Nexus.NumProcessingWorkers", int.class);
				
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
					return new DumpStorer(abortHandler, jobTraces, outTray, swCars, swPersons, matchedCount);
				}
				catch (Exception e)
				{
					throw new WrappedException(e);
				}
			}
		});
		
		nexus.start();
		
		gui.close();
		
		close();
	}
	
	public void close()
	{
		if (is != null)
		{
			try
			{
				logger.info("Close the input stream");
				is.close();
				is = null;
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
			finally
			{
			}
		}

		if (osCars != null)
		{
			try
			{
				logger.info("Close the output stream");
				osCars.close();
				osCars = null;
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
			finally
			{
			}
		}

		if (osPersons != null)
		{
			try
			{
				logger.info("Close the output stream");
				osPersons.close();
				osPersons = null;
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
			finally
			{
			}
		}
	}
	
	// =========================================================================
	
	public Options getOptions()
	{
		return options;
	}
	
	public Nexus getNexus()
	{
		return nexus;
	}
	
	public Gui getGui()
	{
		return gui;
	}
	
	public WikiConfig getWikiConfig()
	{
		return wikiConfig;
	}

	public int getMatchedCount()
	{
		return matchedCount.get();
	}

	// =========================================================================
	
	private boolean options(String[] args) throws IOException
	{
		options.createOption('h', "help")
				.withDescription("Print help message.")
				.create();
		
		// ---
		
		options.createOption('d', "dump")
				.withDescription("The dump file to read.")
				.withPropertyKey("DumpCruncher.File")
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
		
		options.createOption("processing-workers")
				.withDescription("The number of processing workers.")
				.withPropertyKey("Nexus.NumProcessingWorkers")
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
			options.optional("Nexus.NumProcessingWorkers");
			
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
