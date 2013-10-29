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
package org.sweble.wikitext.engine.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.collections.iterators.ArrayIterator;
import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.parser.nodes.WtNode;

import de.fau.cs.osr.ptk.common.AstPrinter;
import de.fau.cs.osr.utils.StringUtils;
import de.fau.cs.osr.utils.WrappedException;

/**
 * Starter-Kit for those who want to play with the Engine.
 */
public class WtEngineToolbox
{
	private WikiConfigImpl wikiConfig;
	
	private WtEngineImpl engine;
	
	private boolean quiet;
	
	private boolean verbose;
	
	// =========================================================================
	
	public WtEngineToolbox()
	{
		restartEngine(DefaultConfigEnWp.generate());
		this.quiet = false;
	}
	
	// =========================================================================
	
	public void setQuiet(boolean quiet)
	{
		this.quiet = quiet;
	}
	
	public boolean getQuiet()
	{
		return this.quiet;
	}
	
	public void setVerbose(boolean verbose)
	{
		this.verbose = verbose;
	}
	
	public boolean getVerbose()
	{
		return this.verbose;
	}
	
	public void restartEngine(WikiConfigImpl wikiConfig)
	{
		this.wikiConfig = wikiConfig;
		this.engine = new WtEngineImpl(wikiConfig);
	}
	
	public WikiConfigImpl getWikiConfig()
	{
		return wikiConfig;
	}
	
	public WtEngineImpl getEngine()
	{
		return engine;
	}
	
	// =========================================================================
	//  AST helpers
	// =========================================================================
	
	public PageId makePageId(String title) throws Exception
	{
		PageTitle pageTitle = PageTitle.make(wikiConfig, title);
		return new PageId(pageTitle, -1);
	}
	
	public PageId makePageId(String title, long id) throws Exception
	{
		PageTitle pageTitle = PageTitle.make(wikiConfig, title);
		return new PageId(pageTitle, id);
	}
	
	public EngProcessedPage wmToAst(PageId pageId, String wikitext) throws Exception
	{
		return wmToAst(pageId, wikitext, new TestExpansionCallback());
	}
	
	public EngProcessedPage wmToAst(
			PageId pageId,
			String wikitext,
			ExpansionCallback callback) throws Exception
	{
		return engine.postprocess(pageId, wikitext, callback);
	}
	
	public String printAst(EngProcessedPage ast)
	{
		return AstPrinter.print((WtNode) ast.getPage());
	}
	
	// =========================================================================
	//  Print information to console
	// =========================================================================
	
	public void printQuoted(String x)
	{
		if (!quiet)
		{
			System.out.print("\"\"\"");
			System.out.print(x);
			System.out.println("\"\"\"");
		}
	}
	
	public void printSep(String title)
	{
		if (!quiet)
		{
			String line = formatSepLine(0, title);
			System.out.println(line);
		}
	}
	
	public void printSep(int indent, String title)
	{
		if (!quiet)
		{
			String line = formatSepLine(indent, title);
			System.out.println(line);
		}
	}
	
	public void printBigSep(String title)
	{
		if (!quiet)
		{
			System.out.println();
			String eq80 = StringUtils.strrep("=", 80);
			String sp76 = StringUtils.strrep(" ", 76);
			String spX = StringUtils.strrep(" ", Math.max(75 - title.length(), 1));
			System.out.println(eq80);
			System.out.println("==" + sp76 + "==");
			System.out.println("== " + title + spX + "==");
			System.out.println("==" + sp76 + "==");
			System.out.println(eq80);
			System.out.println();
		}
	}
	
	public String formatSepLine(int indent, String title)
	{
		String sep = StringUtils.strrep(' ', indent) + "--[ " + title + " ]";
		String line = sep + StringUtils.strrep("-", Math.max(80 - sep.length(), 2));
		return line;
	}
	
	public void printArtifacts(Map<String, String> artifacts)
	{
		if (quiet)
			return;
		
		for (Entry<String, String> a : artifacts.entrySet())
		{
			System.out.print(a.getKey());
			System.out.print(" = ");
			printQuoted(a.getValue());
		}
	}
	
	// =========================================================================
	//  FullPage IO
	// =========================================================================
	
	public final class PagesFromDiskIterator
			implements
				Iterator<FullPage>
	{
		private final ArrayIterator iter;
		
		public PagesFromDiskIterator(File baseDir)
		{
			File[] files = baseDir.listFiles(new FileFilter()
			{
				@Override
				public boolean accept(File pathname)
				{
					return pathname.getName().endsWith(".wikitext");
				}
			});
			
			iter = new ArrayIterator(files);
		}
		
		@Override
		public boolean hasNext()
		{
			return iter.hasNext();
		}
		
		@Override
		public FullPage next()
		{
			try
			{
				return loadFullPageFromDisk((File) iter.next());
			}
			catch (Exception e)
			{
				throw new WrappedException(e);
			}
		}
		
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	public final class PagesFromZipIterator
			implements
				Iterator<FullPage>
	{
		private final ZipFile zipFile;
		
		private final Enumeration<? extends ZipEntry> entries;
		
		public PagesFromZipIterator(File zipFile) throws ZipException, IOException
		{
			this.zipFile = new ZipFile(zipFile);
			this.entries = this.zipFile.entries();
		}
		
		@Override
		public boolean hasNext()
		{
			return entries.hasMoreElements();
		}
		
		@Override
		public FullPage next()
		{
			try
			{
				ZipEntry ze = (ZipEntry) entries.nextElement();
				
				long longSize = ze.getSize();
				if (longSize > Integer.MAX_VALUE)
					throw new IllegalArgumentException("Archives contains files too big to process!");
				
				Reader reader = new InputStreamReader(zipFile.getInputStream(ze), "UTF-8");
				return loadFullPageFromDisk(reader);
			}
			catch (Exception e)
			{
				throw new WrappedException(e);
			}
		}
		
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	public static void storePage(FullPage page, File dir) throws FileNotFoundException, UnsupportedEncodingException
	{
		PageTitle title = page.getId().getTitle();
		String fileTitle = URLEncoder.encode(title.getNormalizedFullTitle(), "UTF-8");
		PrintStream ps = new PrintStream(new File(dir, fileTitle + ".wikitext"));
		ps.println(title);
		ps.println(page.getId().getRevision());
		ps.print(page.getText());
		ps.close();
	}
	
	public FullPage loadFullPageFromDisk(Reader fileReader) throws Exception
	{
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(fileReader);
			
			String title = br.readLine();
			long revision = Long.parseLong(br.readLine());
			PageId pageId = makePageId(title, revision);
			
			StringWriter sw = new StringWriter();
			int ch;
			while ((ch = br.read()) != -1)
				sw.append((char) ch);
			String wm = sw.toString();
			
			return new FullPage(pageId, wm);
		}
		finally
		{
			if (br != null)
				br.close();
		}
	}
	
	public FullPage loadFullPageFromDisk(File file) throws Exception
	{
		return loadFullPageFromDisk(new FileReader(file));
	}
	
	// =========================================================================
	
	public static final class TestExpansionCallback
			implements
				ExpansionCallback
	{
		@Override
		public FullPage retrieveWikitext(
				ExpansionFrame expansionFrame,
				PageTitle pageTitle) throws Exception
		{
			return null;
		}
		
		@Override
		public String fileUrl(PageTitle pageTitle, int width, int height) throws Exception
		{
			return null;
		}
	}
}
