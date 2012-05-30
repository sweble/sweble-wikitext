package org.sweble.wikitext.engine.astwom;

import static org.junit.Assert.*;
import static org.sweble.wikitext.engine.wom.tools.AstWomBuilder.*;

import org.junit.Test;
import org.sweble.wikitext.engine.wom.WomBold;
import org.sweble.wikitext.engine.wom.WomPage;

public class SpecialElementSetterAndGetterTest
{
	@Test
	public void testSetStyle() throws Exception
	{
		WomPage page = womPage().withBody(
				womBold().build()
				).build();
		
		WomBold bold = (WomBold) page.getBody().getFirstChild();
		
		bold.setStyle("Test");
		assertEquals("Test", bold.getAttribute("style"));
	}
}
