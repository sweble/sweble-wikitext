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
package org.sweble.engine.serialization;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.sweble.wom3.Wom3Document;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.swcadapter.utils.WtWom3Toolbox.Artifacts;

import com.google.gson.Gson;

import de.fau.cs.osr.utils.NamedParametrized;
import de.fau.cs.osr.utils.TestResourcesFixture;

@RunWith(value = NamedParametrized.class)
public class LoadWomFromJsonTest
		extends
			SerializationTestBase
{
	private static final String INPUT_SUB_DIR = "/various";

	private static final String FILTER_RX = ".*?\\.wikitext";

	// =========================================================================

	@Parameters
	public static List<Object[]> enumerateInputs() throws Exception
	{
		TestResourcesFixture resources = getTestResourcesFixture();
		return resources.gatherAsParameters(INPUT_SUB_DIR, FILTER_RX, false);
	}

	// =========================================================================

	private Wom3Document wom;

	private File inputFile;

	// =========================================================================

	public LoadWomFromJsonTest(
			String title,
			TestResourcesFixture resources,
			File inputFile)
	{
		super(resources);
		this.inputFile = inputFile;
	}

	// =========================================================================

	@Before
	public void before() throws Exception
	{
		Artifacts af = wmToWom(inputFile);
		this.wom = af.womDoc;
	}

	@Test
	public void testLoadWomFromCompactJson() throws Exception
	{
		doIt(true, true);
		doIt(true, false);
	}

	@Test
	public void testLoadWomFromVerboseJson() throws Exception
	{
		doIt(false, true);
		doIt(false, false);
	}

	private void doIt(boolean compact, boolean pretty)
	{
		Gson gson = createWom3Gson(compact, pretty);

		String womJson = gson.toJson(wom);
		//System.out.println(womJson);

		Wom3Node loadedWomJsonDoc = gson.fromJson(womJson, Wom3Node.class);
		// Unpack article from document fragment
		loadedWomJsonDoc = loadedWomJsonDoc.getFirstChild();

		String loadedWomXml = gson.toJson(loadedWomJsonDoc);

		Assert.assertEquals(womJson, loadedWomXml);
	}
}
