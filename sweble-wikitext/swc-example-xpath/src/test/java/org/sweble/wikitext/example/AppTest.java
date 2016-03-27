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

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import de.fau.cs.osr.utils.FileTools;
import de.fau.cs.osr.utils.StringTools;

public class AppTest
{
	@Test
	public void test() throws Exception
	{
		String title = "Simple_Page";

		URL url = AppTest.class.getResource("/" + title + ".wikitext");
		String path = StringTools.decodeUsingDefaultCharset(url.getFile());

		StringBuilder b = new StringBuilder();

		b.append(App.run(new File(path), title, "//WtInternalLink"));

		b.append(App.run(new File(path), title, "//WtTableCell"));

		InputStream expectedIs = AppTest.class.getResourceAsStream("/" + title + ".result");
		String expected = FileTools.lineEndToUnix(IOUtils.toString(expectedIs, "UTF8"));

		String actual = FileTools.lineEndToUnix(b.toString());

		Assert.assertEquals(expected, actual);
	}
}
