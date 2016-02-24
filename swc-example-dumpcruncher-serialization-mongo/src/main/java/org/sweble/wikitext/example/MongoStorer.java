package org.sweble.wikitext.example;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.JobTrace;
import org.sweble.wikitext.articlecruncher.JobTraceSet;
import org.sweble.wikitext.articlecruncher.utils.AbortHandler;
import org.sweble.wikitext.articlecruncher.utils.WorkerBase;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

/**
 * Created by darvin on 2/24/16.
 */
public class MongoStorer
        extends
        WorkerBase
{
    private final JobTraceSet jobTraces;

    private final BlockingQueue<Job> outTray;

    private MongoClient mongoClient;
    private DB db;
    private DBCollection collection;
    // =========================================================================

    public MongoStorer(
            AbortHandler abortHandler,
            JobTraceSet jobTraces,
            BlockingQueue<Job> outTray)
    {
        super(MongoStorer.class.getSimpleName(), abortHandler);

        this.outTray = outTray;
        this.jobTraces = jobTraces;

        mongoClient = new MongoClient();
        db = mongoClient.getDB( "wikiDump" );
        collection = db.getCollection("wikiDump");

    }

    // =========================================================================

    int count = 0;

    @Override
    protected void work() throws Throwable
    {
        while (true)
        {
            RevisionJob job = (RevisionJob) outTray.take();
            ++count;


            String pageTitle = job.getPageTitle();

            if (pageTitle.startsWith("Wiktionary:") || pageTitle.startsWith("Index:")){
                continue;
            }


            String pageId = job.getPageId().toString();
            String serializedWikiText = (String) job.getResult();
//            System.out.print("PAGE storer: "+pageTitle+"\n");


            String stringPageTitle = pageTitle.toString();
            String wikitext = job.getTextText();
            BasicDBObject update = new BasicDBObject("$set", new BasicDBObject("_id", pageId)
                    .append("pageTitle", stringPageTitle)
                    .append("parsedWikiText", serializedWikiText)
                    .append("wikiText", wikitext));
            BasicDBObject filter = new BasicDBObject("_id", pageId);


            try {
                collection.update(filter, update, true, false);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.print("ERROR ON STORING: "+pageTitle+"\n");

            }


            JobTrace trace = job.getTrace();
            trace.signOff(getClass(), null);

            if (!jobTraces.remove(trace))
                throw new InternalError("Missing job trace");
        }
    }

    @Override
    protected void after()
    {
        info(getClass().getSimpleName() + " counts " + count + " items");
        mongoClient.close();
    }
}