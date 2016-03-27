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
package org.sweble.wikitext.engine;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.config.WikiRuntimeInfo;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;
import org.sweble.wikitext.engine.utils.NoTransparentRtDataPrinter;
import org.sweble.wikitext.parser.parser.LinkTargetException;

import de.fau.cs.osr.utils.NamedParametrizedSuites;
import de.fau.cs.osr.utils.NamedParametrizedSuites.NamedParametrizedSuite;
import de.fau.cs.osr.utils.NamedParametrizedSuites.Suites;
import de.fau.cs.osr.utils.StringTools;
import de.fau.cs.osr.utils.TestResourcesFixture;

@RunWith(value = NamedParametrizedSuites.class)
public class MassExpansionTest
{
	private static final String TESTS_DIR = "engine/mass-expansion";

	// =========================================================================

	private static final Pattern FILE_URL_RX = Pattern.compile("fileUrl-(.*?)-(-?\\d+)-(-?\\d+).txt");

	private static final Pattern ARTICLE_RX = Pattern.compile("retrieveWikitext-(.*?)-(\\d+).wikitext");

	private static final Pattern TEST_RX = Pattern.compile("(.*?).txt");

	// =========================================================================

	@Suites
	public static List<NamedParametrizedSuite> enumerateSuites() throws Exception
	{
		File dir = TestResourcesFixture.resourceNameToFile(
				MassExpansionTest.class, "/" + TESTS_DIR);

		String[] testSetDirs = dir.list(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return new File(dir, name).isDirectory();
			}
		});

		String[] testSetZips = dir.list(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.toLowerCase().endsWith(".zip");
			}
		});

		List<NamedParametrizedSuite> suites = new ArrayList<NamedParametrizedSuite>();

		for (String testDir : testSetDirs)
			suites.add(enumerateSuiteTestCases(new File(dir, testDir)));

		for (String testZip : testSetZips)
			suites.add(enumerateSuiteTestCasesFromZip(new File(dir, testZip)));

		Collections.sort(suites);

		return suites;
	}

	private static NamedParametrizedSuite enumerateSuiteTestCasesFromZip(
			File zipFile) throws IOException
	{
		ZipFile zf = new ZipFile(zipFile);
		try
		{
			Enumeration<? extends ZipEntry> entries = zf.entries();

			String suiteName;
			{
				String zfname = zipFile.getName();
				if (!zfname.toLowerCase().endsWith(".zip"))
					throw new InternalError();
				suiteName = zfname.substring(0, zfname.length() - 4);
			}

			String testPrefix = suiteName + "/tests/";
			String resourcesPrefix = suiteName + "/resources/";

			Map<FileUrlKey, String> fileUrls =
					new HashMap<FileUrlKey, String>();

			Map<String, ArticleDesc> articles =
					new HashMap<String, ArticleDesc>();

			List<Object[]> testCases = new ArrayList<Object[]>();

			while (entries.hasMoreElements())
			{
				ZipEntry ze = (ZipEntry) entries.nextElement();

				// Skip directories
				if (ze.getName().endsWith("/"))
					continue;

				long longSize = ze.getSize();
				if (longSize > Integer.MAX_VALUE)
					throw new IllegalArgumentException("Archives contains files too big to process!");

				InputStream is = zf.getInputStream(ze);
				byte[] content = IOUtils.toByteArray(is);
				is.close();

				String filename = ze.getName();
				if (filename.startsWith(resourcesPrefix + "fileUrl-") && filename.endsWith(".txt"))
				{
					filename = filename.substring(resourcesPrefix.length());
					Matcher m = FILE_URL_RX.matcher(filename);
					if (!m.matches())
						throw new IllegalArgumentException("Wrong 'fileUrl' pattern: " + ze.getName());

					String encName = m.group(1);
					int width = Integer.parseInt(m.group(2));
					int height = Integer.parseInt(m.group(3));

					FileUrlKey key = new FileUrlKey(encName, width, height);
					fileUrls.put(key, new String(content, "UTF8"));
				}
				else if (filename.startsWith(resourcesPrefix + "retrieveWikitext-") && filename.endsWith(".wikitext"))
				{
					filename = filename.substring(resourcesPrefix.length());
					Matcher m = ARTICLE_RX.matcher(filename);
					if (!m.matches())
						throw new IllegalArgumentException("Wrong 'retrieveWikitext' pattern: " + ze.getName());

					String encName = m.group(1);
					long revision = Long.parseLong(m.group(2));

					ArticleDesc article = new ArticleDesc(
							revision,
							new String(content, "UTF8"));

					articles.put(encName, article);
				}
				else if (filename.startsWith(testPrefix) && filename.endsWith(".txt"))
				{
					filename = filename.substring(testPrefix.length());
					Matcher m = TEST_RX.matcher(filename);
					if (!m.matches())
						throw new IllegalArgumentException("Invalid test case filename: " + ze.getName());

					String title = m.group(1);
					String test = new String(content, "UTF8");

					testCases.add(new Object[] {
							title,
							fileUrls,
							articles,
							test });
				}
				else
				{
					System.err.println("Ignored file in " + zipFile + ": " + ze.getName());
					continue;
				}
			}

			Collections.sort(testCases, new Comparator<Object[]>()
			{
				@Override
				public int compare(Object[] o1, Object[] o2)
				{
					return ((String) o1[0]).compareTo((String) o2[0]);
				}
			});

			return new NamedParametrizedSuite(
					zipFile.getName(),
					MassExpansionTest.class.getSimpleName(),
					testCases);
		}
		finally
		{
			zf.close();
		}
	}

	private static NamedParametrizedSuite enumerateSuiteTestCases(File dir) throws IOException
	{
		Map<FileUrlKey, String> fileUrls =
				new HashMap<FileUrlKey, String>();

		Map<String, ArticleDesc> articles =
				new HashMap<String, ArticleDesc>();

		File resourcesDir = new File(dir, "resources");
		File testCasesDir = new File(dir, "tests");

		String[] fileUrlFiles = resourcesDir.list(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.startsWith("fileUrl-") && name.endsWith(".txt");
			}
		});

		String[] articleFiles = resourcesDir.list(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.startsWith("retrieveWikitext-") && name.endsWith(".wikitext");
			}
		});

		for (String fileUrlFilename : fileUrlFiles)
		{
			Matcher m = FILE_URL_RX.matcher(fileUrlFilename);
			if (!m.matches())
				throw new IllegalArgumentException("Wrong 'fileUrl' pattern: " + fileUrlFilename);

			String encName = m.group(1);
			int width = Integer.parseInt(m.group(2));
			int height = Integer.parseInt(m.group(3));

			File fileUrlFile = new File(resourcesDir, fileUrlFilename);
			String url = FileUtils.readFileToString(fileUrlFile, "UTF8");

			FileUrlKey key = new FileUrlKey(encName, width, height);
			fileUrls.put(key, url);
		}

		for (String articleFilename : articleFiles)
		{
			Matcher m = ARTICLE_RX.matcher(articleFilename);
			if (!m.matches())
				throw new IllegalArgumentException("Wrong 'retrieveWikitext' pattern: " + articleFilename);

			String encName = m.group(1);
			long revision = Long.parseLong(m.group(2));

			File fileUrlFile = new File(resourcesDir, articleFilename);
			String content = FileUtils.readFileToString(fileUrlFile, "UTF8");

			articles.put(encName, new ArticleDesc(revision, content));
		}

		List<Object[]> testCases = new ArrayList<Object[]>();

		String[] testCaseFiles = testCasesDir.list(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.endsWith(".txt");
			}
		});

		Arrays.sort(testCaseFiles);

		for (String filename : testCaseFiles)
		{
			File file = new File(testCasesDir, filename);
			String content = FileUtils.readFileToString(file, "UTF-8");

			testCases.add(new Object[] {
					filename,
					fileUrls,
					articles,
					content });
		}

		return new NamedParametrizedSuite(
				dir.getName(),
				MassExpansionTest.class.getSimpleName(),
				testCases);
	}

	// =========================================================================

	private final WikiConfigImpl config;

	private final WtEngineImpl engine;

	private final String title;

	private final Map<FileUrlKey, String> fileUrls;

	private final Map<String, ArticleDesc> articles;

	private final String inputFileContent;

	// =========================================================================

	public MassExpansionTest(
			String title,
			Map<FileUrlKey, String> fileUrls,
			Map<String, ArticleDesc> articles,
			String inputFileContent)
	{
		this.config = fixConfig(DefaultConfigEnWp.generate());
		this.engine = new WtEngineImpl(config);

		this.title = title;
		this.fileUrls = fileUrls;
		this.articles = articles;
		this.inputFileContent = inputFileContent;
	}

	// =========================================================================

	private WikiConfigImpl fixConfig(WikiConfigImpl config)
	{
		config.setSiteName("English Wikipedia");

		config.setRuntimeInfo(new WikiRuntimeInfo()
		{
			@Override
			public Calendar getDateAndTime(Locale locale)
			{
				Calendar timestamp = new GregorianCalendar(locale);
				timestamp.setLenient(true);
				timestamp.set(2012, 9, 18, 14, 25, 13);
				return timestamp;
			}

			@Override
			public Calendar getDateAndTime()
			{
				return getDateAndTime(Locale.getDefault());
			}
		});

		return config;
	}

	// =========================================================================

	@Test
	public void testOurExpansionMatchesReference() throws Exception
	{
		ExpansionCallback callback = new MassExpansionCallback();

		boolean forInclusion = false;

		TestDesc desc = buildTest();

		PageTitle title = desc.getTitle();
		PageId pageId = new PageId(title, -1);
		EngProcessedPage ast = engine.expand(
				pageId,
				desc.getStmt(),
				forInclusion,
				callback);

		String raw = NoTransparentRtDataPrinter.print(ast);
		String actual = polishRawResult(raw);

		assertEquals(desc.getExpected(), actual);
	}

	/**
	 * Convert XML char refs to chars and convert "<nowiki></nowiki>" to an
	 * empty tag.
	 */
	private String polishRawResult(String raw)
	{
		StringBuilder b = new StringBuilder();

		int copyFrom = 0;
		int searchFrom = 0;
		int i;
		while ((i = raw.indexOf("&#x", searchFrom)) != -1)
		{
			searchFrom = i + 3;
			int j = raw.indexOf(';', searchFrom);
			if (j == -1 || j > i + 12)
				continue;

			int ch = Integer.valueOf(raw.substring(i + 3, j), 0x10);
			switch (ch)
			{
				case 0x7B: // {
				case 0x7D: // }
				case 0x7C: // |
				case 0x3D: // =
					break;
				default:
					continue;
			}
			b.append(raw.substring(copyFrom, i));
			b.append(Character.toChars(ch));

			searchFrom = copyFrom = j + 1;
		}
		b.append(raw.substring(copyFrom, raw.length()));

		return b.toString().replace("<nowiki></nowiki>", "<nowiki />");
	}

	// =========================================================================

	private TestDesc buildTest() throws IOException, LinkTargetException
	{
		String content = inputFileContent;

		String i0Delim = "==[ TITLE ]=====================================================================\n";
		int i0 = content.indexOf(i0Delim);
		if (i0 != 0)
			wrongArticleFormat();
		int from0 = i0 + i0Delim.length();

		String i1Delim = "\n==[ STATEMENT ]=================================================================\n";
		int i1 = content.indexOf(i1Delim, from0);
		if (i1 < 0)
			wrongArticleFormat();
		int from1 = i1 + i1Delim.length();

		String i2Delim = "\n==[ EXPECTED EXPANSION ]========================================================\n";
		int i2 = content.indexOf(i2Delim, from1);
		if (i2 < 0)
			wrongArticleFormat();
		int from2 = i2 + i2Delim.length();

		String i3Delim = "\n==[ END ]=======================================================================\n";
		int i3 = content.indexOf(i3Delim, from2);
		if (i3 < 0)
			wrongArticleFormat();

		PageTitle pageTitle = PageTitle.make(config, content.substring(from0, i1));
		String stmt = content.substring(from1, i2);
		String expected = content.substring(from2, i3);
		return new TestDesc(pageTitle, stmt, expected);
	}

	private void wrongArticleFormat()
	{
		throw new IllegalArgumentException("Wrong test case file format: " + title);
	}

	// =========================================================================

	public final class MassExpansionCallback
			implements
				ExpansionCallback
	{

		@Override
		public FullPage retrieveWikitext(
				ExpansionFrame expansionFrame,
				PageTitle pageTitle)
		{
			String title = StringTools.safeFilename(
					pageTitle.getNormalizedFullTitle());

			ArticleDesc article = articles.get(title);
			if (article == null)
				return null;

			PageId pageId = new PageId(pageTitle, article.getRevision());
			return new FullPage(pageId, article.getContent());
		}

		@Override
		public String fileUrl(PageTitle pageTitle, int width, int height)
		{
			String title = StringTools.safeFilename(
					pageTitle.getNormalizedFullTitle());

			FileUrlKey key = new FileUrlKey(title, width, height);

			return fileUrls.get(key);
		}

	}

	// =========================================================================

	public static final class TestDesc
	{
		private final PageTitle title;

		private final String stmt;

		private final String expected;

		public TestDesc(PageTitle title, String stmt, String expected)
		{
			super();
			this.title = title;
			this.stmt = stmt;
			this.expected = expected;
		}

		public PageTitle getTitle()
		{
			return title;
		}

		public String getStmt()
		{
			return stmt;
		}

		public String getExpected()
		{
			return expected;
		}
	}

	// =========================================================================

	public static final class ArticleDesc
	{
		private final long revision;

		private final String content;

		public ArticleDesc(long revision, String content)
		{
			super();
			this.revision = revision;
			this.content = content;
		}

		public long getRevision()
		{
			return revision;
		}

		public String getContent()
		{
			return content;
		}

		@Override
		public String toString()
		{
			return "ArticleDesc [revision=" + revision + ", content=" + content + "]";
		}
	}

	// =========================================================================

	public static final class FileUrlKey
	{

		private String encName;

		private int width;

		private int height;

		public FileUrlKey(String encName, int width, int height)
		{
			this.encName = encName;
			this.width = width;
			this.height = height;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((encName == null) ? 0 : encName.hashCode());
			result = prime * result + (int) (height ^ (height >>> 32));
			result = prime * result + (int) (width ^ (width >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			FileUrlKey other = (FileUrlKey) obj;
			if (encName == null)
			{
				if (other.encName != null)
					return false;
			}
			else if (!encName.equals(other.encName))
				return false;
			if (height != other.height)
				return false;
			if (width != other.width)
				return false;
			return true;
		}

		@Override
		public String toString()
		{
			return "FileUrlKey [encName=" + encName + ", width=" + width + ", height=" + height + "]";
		}
	}
}
