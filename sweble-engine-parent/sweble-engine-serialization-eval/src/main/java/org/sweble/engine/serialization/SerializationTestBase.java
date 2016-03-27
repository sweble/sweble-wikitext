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
import java.io.FileNotFoundException;
import java.io.IOException;

import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wom3.swcadapter.utils.WtWom3Toolbox;
import org.sweble.wom3.swcadapter.utils.WtWom3Toolbox.Artifacts;

import de.fau.cs.osr.utils.TestResourcesFixture;
import de.fau.cs.osr.utils.WrappedException;

public abstract class SerializationTestBase
		extends
			SerializationLabToolbox
{
	private final TestResourcesFixture resources;

	private WtWom3Toolbox wtWomToolbox = new WtWom3Toolbox();

	// =========================================================================

	protected static TestResourcesFixture getTestResourcesFixture()
	{
		try
		{
			File path = TestResourcesFixture.resourceNameToFile(
					SerializationTestBase.class, "/");

			return new TestResourcesFixture(path);
		}
		catch (FileNotFoundException e)
		{
			throw new WrappedException(e);
		}
	}

	// =========================================================================

	protected SerializationTestBase(TestResourcesFixture resources)
	{
		this.resources = resources;
	}

	// =========================================================================

	protected TestResourcesFixture getResources()
	{
		return resources;
	}

	public Artifacts wmToWom(File wmFile) throws LinkTargetException, IOException, EngineException
	{
		return wtWomToolbox.wmToWom(wmFile, "UTF8");
	}

}
