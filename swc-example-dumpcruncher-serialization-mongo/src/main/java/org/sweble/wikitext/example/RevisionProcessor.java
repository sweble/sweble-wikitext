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

package org.sweble.wikitext.example;

import de.fau.cs.osr.utils.getopt.Options;
import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.Processor;
import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.parser.LinkTargetException;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;

public class RevisionProcessor
		implements
			Processor
{
	private final DumpCruncher dumpCruncher;
	
	public RevisionProcessor(DumpCruncher dumpCruncher)
	{
		this.dumpCruncher = dumpCruncher;
	}
	
	@Override
	public Object process(Job job) throws LinkTargetException, EngineException
	{
		Gui gui = dumpCruncher.getGui();
		gui.processingStarted();
		gui.redrawLater();
		
		RevisionJob revJob = (RevisionJob) job;
		
		WikiConfig config = dumpCruncher.getWikiConfig();
		
		// Instantiate a compiler for wiki pages
		WtEngineImpl engine = new WtEngineImpl(config);
		
		// Retrieve a page
		PageTitle pageTitle = PageTitle.make(config, revJob.getPageTitle());
		
		PageId pageId = new PageId(pageTitle, revJob.getId().longValue());
		
		String wikitext = revJob.getTextText();
		
		// Compile the retrieved page
		EngProcessedPage cp = engine.postprocess(pageId, wikitext, null);
		
		gui.processingFinished();
		gui.redrawLater();

		return serialize(cp.getPage(), SerializationMethod.XML);
	}
	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

	private static String serialize(WtNode source,
			SerializationMethod method)
	{
		Serializer serializer = new Serializer(source);

		// Parsing
//		serializer.setParserAutoCorrectEnabled(opt.has("auto-correct"));
//		serializer.setParserWarningsEnabled(false);
//		serializer.setParserRtdEnabled(!opt.has("strip-rtd"));
//
//		// Postprocessing
//		serializer.setPpStripLocations(opt.has("strip-location"));
//		serializer.setPpStripAllAttributes(opt.has("strip-attrs"));
//		serializer.setPpStripRtdAttributes(opt.has("strip-rtd"));
//		serializer.setPpSimplifyAst(opt.has("simplify"));
//
//		// Timings
//		serializer.setQuiet(!opt.has("timings"));
//
//		if (opt.has("timings"))
//			serializer.roundTrip(method);

		try {
			byte[] result = serializer.serializeTo(method);
			return new String(result, UTF8_CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
