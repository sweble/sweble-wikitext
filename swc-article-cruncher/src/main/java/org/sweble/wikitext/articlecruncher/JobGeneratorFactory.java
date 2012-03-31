package org.sweble.wikitext.articlecruncher;

import java.util.concurrent.BlockingQueue;

import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;

public interface JobGeneratorFactory
{
	WorkerBase create(
			AbortHandler abortHandler,
			BlockingQueue<JobWithHistory> inTray,
			JobTraceSet jobTraces);
}
