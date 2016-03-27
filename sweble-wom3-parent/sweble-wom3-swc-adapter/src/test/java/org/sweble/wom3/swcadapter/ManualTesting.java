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
package org.sweble.wom3.swcadapter;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wom3.util.SaxonWomTransformations;
import org.sweble.wom3.util.SaxonWomXPath;
import org.sweble.wom3.util.Wom3Toolbox;

import de.fau.cs.osr.utils.FileContent;
import de.fau.cs.osr.utils.NamedParametrized;
import de.fau.cs.osr.utils.TestResourcesFixture;

@RunWith(value = NamedParametrized.class)
public class ManualTesting
		extends
			WtWom3IntegrationTestBase
{
	private static final String FILTER_RX = ".*?\\.wikitext";

	private static final String INPUT_SUB_DIR = "nopkg-manual";

	// =========================================================================

	public static void main(String[] args) throws Exception
	{
		for (Object[] input : enumerateInputs())
		{
			String title = (String) input[0];
			TestResourcesFixture resources = (TestResourcesFixture) input[1];
			File inputFile = (File) input[2];
			ManualTesting test = new ManualTesting(title, resources, inputFile);
			test.runTests();
		}
	}

	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		TestResourcesFixture resources = getTestResourcesFixture();

		File dir = new File(resources.getBaseDirectory(), INPUT_SUB_DIR);
		if (!dir.exists() || !dir.isDirectory())
			return Collections.emptyList();

		return resources.gatherAsParameters(INPUT_SUB_DIR, FILTER_RX, false);
	}

	// =========================================================================

	private final File inputFile;

	// =========================================================================

	public ManualTesting(
			String title,
			TestResourcesFixture resources,
			File inputFile)
	{
		super(resources);
		this.inputFile = inputFile;

	}

	// =========================================================================

	@Test
	public void runTests() throws Exception
	{
		FileContent inputFileContent = new FileContent(inputFile);

		Artifacts afs = parseArticle(
				inputFileContent.getContent(),
				inputFile.getAbsolutePath());

		{
			String restoredWmFast = Wom3Toolbox.womToWmFast(afs.womDoc);
			File output = new File(inputFile.getName() + "-wm-from-wom-fast.wikitext");
			FileUtils.writeStringToFile(output, restoredWmFast, "UTF8");
			Assert.assertEquals(inputFileContent.getContent(), restoredWmFast);
		}

		{
			String restoredWmXPath = SaxonWomXPath.womToWmXPath(afs.womDoc);
			File output = new File(inputFile.getName() + "-wm-from-wom-xpath.wikitext");
			FileUtils.writeStringToFile(output, restoredWmXPath, "UTF8");
			Assert.assertEquals(inputFileContent.getContent(), restoredWmXPath);
		}
	}

	// =========================================================================

	public Artifacts parseArticle(String source, String title) throws Exception
	{

		String fileTitle = FilenameUtils.getBaseName(inputFile.getName());

		PageId pageId = makePageId(fileTitle);

		Artifacts afs = wmToWom(inputFile, pageId, null);

		{
			String ast = printAst(afs.ast);
			File output = new File(inputFile.getName() + "-ast.wikitext");
			FileUtils.writeStringToFile(output, ast, "UTF8");
		}

		String wom = SaxonWomTransformations.printWom(afs.womDoc);
		{
			File output = new File(inputFile.getName() + "-wom.wikitext");
			FileUtils.writeStringToFile(output, wom, "UTF8");
		}

		return afs;
	}
}
