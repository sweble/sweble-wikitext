/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3.swcadapter.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.WtEngineToolbox;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wom3.Wom3Document;
import org.sweble.wom3.swcadapter.AstToWomConverter;
import org.sweble.wom3.util.SaxonWomTransformations;
import org.sweble.wom3.util.Wom3Toolbox;
import org.w3c.dom.Element;

import de.fau.cs.osr.utils.StringTools;

public class WtWom3Toolbox
		extends
			WtEngineToolbox
{
	private static File resourceNameToFile(Class<?> clazz, String testFile)
	{
		URL wmUrl = clazz.getResource(testFile);
		if (wmUrl != null)
			return new File(StringTools.decodeUsingDefaultCharset(wmUrl.getFile()));
		return null;
	}

	// =========================================================================
	//  WM <-> AST <-> WOM helpers
	// =========================================================================

	public Artifacts wmToWom(String wmPath, String charset) throws LinkTargetException, IOException, EngineException
	{
		File wmFile = resourceNameToFile(getClass(), wmPath);
		String title = FilenameUtils.getBaseName(wmFile.getName());
		PageId pageId = makePageId(title);
		return wmToWom(wmFile, pageId, charset);
	}

	public Artifacts wmToWom(File wmFile, String charset) throws LinkTargetException, IOException, EngineException
	{
		String title = FilenameUtils.getBaseName(wmFile.getName());
		PageId pageId = makePageId(title);
		return wmToWom(wmFile, pageId, charset);
	}

	public Artifacts wmToWom(File inputFile, PageId pageId, String charset) throws IOException, EngineException
	{
		return wmToWom(inputFile, FileUtils.readFileToString(inputFile, charset), pageId);
	}

	public Artifacts wmToWom(File inputFile, String wikitext, PageId pageId) throws EngineException
	{
		TestExpansionCallback expCallback = new TestExpansionCallback();
		return wmToWom(inputFile, wikitext, pageId, expCallback);
	}

	public Artifacts wmToWom(
			File inputFile,
			PageId pageId,
			ExpansionCallback callback,
			String charset) throws IOException, EngineException
	{
		return wmToWom(inputFile, FileUtils.readFileToString(inputFile, charset), pageId, callback);
	}

	public Artifacts wmToWom(
			File inputFile,
			String wikitext,
			PageId pageId,
			ExpansionCallback callback) throws EngineException
	{
		Artifacts a = new Artifacts();
		a.wmFile = inputFile;
		a.pageId = pageId;
		a.wm = wikitext;
		a.ast = wmToAst(pageId, a.wm, callback);
		a.womDoc = astToWom(pageId, a.ast);
		return a;
	}

	public Artifacts astToWom(
			EngProcessedPage ast,
			PageId pageId,
			ExpansionCallback callback)
	{
		Artifacts a = new Artifacts();
		a.wmFile = null;
		a.pageId = pageId;
		a.wm = null;
		a.ast = ast;
		a.womDoc = astToWom(pageId, a.ast);
		return a;
	}

	public Wom3Document astToWom(PageId pageId, EngProcessedPage ast)
	{
		Wom3Document womDoc = AstToWomConverter.convert(
				getWikiConfig().getParserConfig(),
				null,
				null,
				pageId.getTitle().getTitle(),
				"Mr. Tester",
				DateTime.parse("2012-12-07T12:15:30.000+01:00"),
				ast.getPage());

		return womDoc;
	}

	/**
	 * Requires dependency:
	 * 
	 * <pre>
	 *   &lt;dependency>
	 *     &lt;groupId>net.sf.saxon&lt;/groupId>
	 *     &lt;artifactId>Saxon-HE&lt;/artifactId>
	 *   &lt;/dependency>
	 * </pre>
	 */
	public String wmToWomXml(PageId pageId, String wm) throws EngineException
	{
		return SaxonWomTransformations.printWom(astToWom(
				pageId,
				wmToAst(pageId, wm)));
	}

	// =========================================================================
	//  WOM query & manipulation
	// =========================================================================

	public static Wom3Document wrapMultipleArticles(Artifacts afs)
	{
		Wom3Document womDoc = Wom3Toolbox.createDocument("articles");

		Element articles = womDoc.getDocumentElement();
		Element article = afs.womDoc.getDocumentElement();
		womDoc.adoptNode(article);
		articles.appendChild(article);

		return womDoc;
	}

	public static Wom3Document wrapMultipleArticles(Map<String, Artifacts> afs)
	{
		Wom3Document womDoc = Wom3Toolbox.createDocument("articles");

		Element articles = womDoc.getDocumentElement();
		for (Artifacts a : afs.values())
		{
			Element article = a.womDoc.getDocumentElement();
			womDoc.adoptNode(article);
			articles.appendChild(article);
		}

		return womDoc;
	}

	// =========================================================================

	public static final class Artifacts
	{
		public File wmFile;

		public PageId pageId;

		public String wm;

		public EngProcessedPage ast;

		public Wom3Document womDoc;
	}
}
