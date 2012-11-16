package org.example;

import java.util.concurrent.BlockingQueue;

import javax.persistence.EntityManager;

import org.sweble.wikitext.articlecruncher.CompletedJob;
import org.sweble.wikitext.articlecruncher.JobTrace;
import org.sweble.wikitext.articlecruncher.JobTraceSet;
import org.sweble.wikitext.articlecruncher.Nexus;
import org.sweble.wikitext.articlecruncher.Result;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;

import de.fau.cs.osr.thesisdohrn.wpanalysismodel.MwPage;
import de.fau.cs.osr.thesisdohrn.wpanalysismodel.MwPageDao;
import de.fau.cs.osr.thesisdohrn.wpanalysismodel.MwRevision;
import de.fau.cs.osr.thesisdohrn.wpanalysismodel.MwRevisionDao;
import de.fau.cs.osr.thesisdohrn.wpanalysismodel.MwText;
import de.fau.cs.osr.thesisdohrn.wpanalysismodel.MwTextDao;

public class SqlStorerWorker
		extends
			WorkerBase
{
	private final JobTraceSet jobTraces;
	
	private final BlockingQueue<CompletedJob> outTray;
	
	private final EntityManager entityManager;
	
	private final MwPageDao pageDao;
	
	private final MwTextDao textDao;
	
	private final MwRevisionDao revisionDao;
	
	private int count = 0;
	
	// =========================================================================
	
	public SqlStorerWorker(
			AbortHandler abortHandler,
			JobTraceSet jobTraces,
			BlockingQueue<CompletedJob> outTray,
			EntityManager entityManager)
	{
		super(SqlStorerWorker.class.getSimpleName(), abortHandler);
		
		this.outTray = outTray;
		this.jobTraces = jobTraces;
		
		this.entityManager = entityManager;
		this.pageDao = new MwPageDao(entityManager);
		this.textDao = new MwTextDao(entityManager);
		this.revisionDao = new MwRevisionDao(entityManager);
	}
	
	// =========================================================================
	
	@Override
	protected void work() throws Throwable
	{
		try
		{
			while (true)
			{
				CompletedJob completedJob = outTray.take();
				Nexus.getConsoleWriter().updateOutTray(outTray.size());
				++count;
				
				RevisionJob job = (RevisionJob) completedJob.getJob();
				
				Result result = completedJob.getResult();
				
				Exception excep = result.getException();
				
				store(job, excep);
				
				// sign off job
				JobTrace trace = job.getTrace();
				trace.signOff(getClass(), null);
				
				if (!jobTraces.remove(trace))
					throw new InternalError("Missing job trace");
			}
		}
		finally
		{
			try
			{
				entityManager.close();
			}
			catch (Exception drop)
			{
			}
		}
	}
	
	private void store(RevisionJob job, Exception excep) throws Exception
	{
		try
		{
			entityManager.getTransaction().begin();
			
			long pageId = job.getPageId().longValue();
			MwPage page = pageDao.findById(pageId);
			if (page == null)
			{
				page = new MwPage(
						pageId,
						job.getPageNamespace().intValue(),
						job.getPageTitle(),
						job.getPageRedirect());
				
				pageDao.persist(page);
			}
			
			String textText = job.getTextText();
			byte[] sha1 = MwTextDao.computeSha1(textText);
			MwText text = textDao.findByText(textText, sha1);
			if (text == null)
			{
				text = new MwText(
						sha1,
						job.isTextDeleted(),
						textText);
				
				textDao.persist(text);
			}
			
			long revId = job.getId().longValue();
			MwRevision rev = revisionDao.findById(revId);
			if (rev == null)
			{
				rev = new MwRevision(
						revId,
						job.isMinor(),
						job.getTimestamp(),
						page,
						text);
				
				revisionDao.persist(rev);
			}
			
			entityManager.getTransaction().commit();
		}
		catch (Exception e)
		{
			try
			{
				if (entityManager.getTransaction().isActive())
					entityManager.getTransaction().rollback();
			}
			catch (Exception drop)
			{
			}
			throw e;
		}
	}
	
	@Override
	protected void after()
	{
		info(getClass().getSimpleName() + " counts " + count + " items");
	}
}
