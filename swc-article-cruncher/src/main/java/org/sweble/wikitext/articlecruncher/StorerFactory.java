package org.sweble.wikitext.articlecruncher;

import java.util.concurrent.BlockingQueue;

import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;

public interface StorerFactory
{
	WorkerBase create(
			AbortHandler abortHandler,
			JobTraceSet jobTraces,
			BlockingQueue<CompletedJob> outTray);
}
