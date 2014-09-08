/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.swcadapter.utils;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.WtEngineToolbox;
import org.sweble.wom3.Wom3Document;
import org.sweble.wom3.swcadapter.AstToWomConverter;
import org.sweble.wom3.util.Wom3Toolbox;
import org.w3c.dom.Element;

import de.fau.cs.osr.utils.FileContent;
import de.fau.cs.osr.utils.TestResourcesFixture;

public class WtWom3Toolbox
		extends
			WtEngineToolbox
{
	public static final String WOM_NS_DEFAULT_PREFIX = Wom3Toolbox.WOM_NS_DEFAULT_PREFIX;
	
	// =========================================================================
	//  WM <-> AST <-> WOM helpers
	// =========================================================================
	
	public Artifacts wmToWom(String wmPath) throws Exception
	{
		File wmFile = TestResourcesFixture.resourceNameToFile(getClass(), wmPath);
		String title = FilenameUtils.getBaseName(wmFile.getName());
		PageId pageId = makePageId(title);
		return wmToWom(wmFile, pageId);
	}
	
	public Artifacts wmToWom(File wmFile) throws Exception
	{
		String title = FilenameUtils.getBaseName(wmFile.getName());
		PageId pageId = makePageId(title);
		return wmToWom(wmFile, pageId);
	}
	
	public Artifacts wmToWom(File inputFile, PageId pageId) throws Exception
	{
		FileContent inputFileContent = new FileContent(inputFile);
		return wmToWom(inputFile, inputFileContent.getContent(), pageId);
	}
	
	public Artifacts wmToWom(File inputFile, String wikitext, PageId pageId) throws Exception
	{
		TestExpansionCallback expCallback = new TestExpansionCallback();
		return wmToWom(inputFile, wikitext, pageId, expCallback);
	}
	
	public Artifacts wmToWom(
			File inputFile,
			PageId pageId,
			ExpansionCallback callback) throws Exception
	{
		FileContent inputFileContent = new FileContent(inputFile);
		return wmToWom(inputFile, inputFileContent.getContent(), pageId, callback);
	}
	
	public Artifacts wmToWom(
			File inputFile,
			String wikitext,
			PageId pageId,
			ExpansionCallback callback) throws Exception
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
			ExpansionCallback callback) throws Exception
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
				getWikiConfig(),
				pageId.getTitle(),
				"Mr. Tester",
				DateTime.parse("2012-12-07T12:15:30.000+01:00"),
				ast.getPage());
		
		return womDoc;
	}
	
	public String wmToWomXml(PageId pageId, String wm) throws Exception
	{
		return Wom3Toolbox.printWom(astToWom(
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
