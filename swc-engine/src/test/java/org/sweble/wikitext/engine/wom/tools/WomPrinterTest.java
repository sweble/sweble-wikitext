package org.sweble.wikitext.engine.wom.tools;

import static org.junit.Assert.*;
import static org.sweble.wikitext.engine.wom.tools.AstWomBuilder.*;

import org.junit.Test;
import org.sweble.wikitext.engine.wom.WomPage;

public class WomPrinterTest
{
	@Test
	public void womPrinterCorrectlyNestsCommentInsideContainer()
	{
		WomPage womPage =
				womPage().withBody(
						womComment().build()).build();
		
		assertEquals(
				"<body>\n" +
						"  <comment> Default Comment Text </comment>\n" +
						"</body>",
				WomPrinter.print(womPage.getBody()));
	}
	
	@Test
	public void womPrinterCorrectlyNestsBlockElementsInsideContainer()
	{
		WomPage womPage =
				womPage().withBody(
						womHr().build()).build();
		
		assertEquals(
				"<body>\n" +
						"  <hr />\n" +
						"</body>",
				WomPrinter.print(womPage.getBody()));
	}
}
