package org.sweble.wikitext.lazy.utils;

import static org.junit.Assert.*;
import static org.sweble.wikitext.lazy.utils.AstBuilder.*;

import org.junit.Test;
import org.sweble.wikitext.lazy.parser.HorizontalRule;
import org.sweble.wikitext.lazy.preprocessor.XmlComment;

public class TestWikitextPrinter
{
	@Test
	public void wikitextPrinterGeneratesCorrectOutputForHorizontalRule()
	{
		HorizontalRule node = astHr().build();
		
		assertEquals(
				"\n\n----",
				WikitextPrinter.print(node));
	}
	
	@Test
	public void wikitextPrinterProducesNoOutputForComments()
	{
		XmlComment node = astComment().build();
		
		assertEquals(
				"",
				WikitextPrinter.print(node));
	}
}
