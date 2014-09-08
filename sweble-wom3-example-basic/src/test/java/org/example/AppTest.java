/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.example;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import de.fau.cs.osr.utils.FileUtils;
import de.fau.cs.osr.utils.StringUtils;

public class AppTest
{
	@Test
	public void test() throws Exception
	{
		String title = "Simple_Page";
		
		URL url = AppTest.class.getResource("/" + title + ".wikitext");
		
		String path = StringUtils.decodeUsingDefaultCharset(url.getFile());
		String actual = App.run(new File(path), title, false);
		actual = FileUtils.lineEndToUnix(actual);
		
		InputStream expectedIs = AppTest.class.getResourceAsStream("/" + title + ".txt");
		String expected = IOUtils.toString(expectedIs);
		expected = FileUtils.lineEndToUnix(expected);
		
		Assert.assertEquals(expected, actual);
	}
}
