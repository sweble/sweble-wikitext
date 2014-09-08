/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.junit.Test;

public class FontTest
{
	private FontImpl n = (FontImpl) TestHelperDoc.genElem("font");
	
	// =========================================================================
	
	@Test
	public void testColorAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(n, "color", "getColor", "setColor", ColorImpl.valueOf("#abcdef"), "#ABCDEF");
	}
	
	@Test
	public void testColorCanCopeWithSpaces() throws Exception
	{
		n.setAttribute("color", " blue ");
	}
	
	@Test
	public void testFaceAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(n, "face", "getFace", "setFace", "Arial");
	}
	
	@Test
	public void testSizeAttribute() throws Exception
	{
		TestHelperAttribute.testAttribute(n, "size", "getSize", "setSize", 3, "3");
	}
	
	@Test
	public void testSizeCanCopeWithSpaces() throws Exception
	{
		n.setAttribute("size", " 3 ");
	}
	
	@Test
	public void testUnknownAttributeDoesNotThrow() throws Exception
	{
		n.setAttribute("test", "value");
	}
	
	// ==[ Core Attributes ]====================================================
	
	@Test
	public void testClassAttribute() throws Exception
	{
		TestHelperAttribute.testClassAttribute(this.n);
	}
	
	@Test
	public void testIdAttribute() throws Exception
	{
		TestHelperAttribute.testIdAttribute(this.n);
	}
	
	@Test
	public void testStyleAttribute() throws Exception
	{
		TestHelperAttribute.testStyleAttribute(this.n);
	}
	
	@Test
	public void testTitleAttribute() throws Exception
	{
		TestHelperAttribute.testTitleAttribute(this.n);
	}
	
	// ==[ I18n Attributes ]====================================================
	
	@Test
	public void testDirAttribute() throws Exception
	{
		TestHelperAttribute.testDirAttribute(this.n);
	}
	
	@Test
	public void testLangAttribute() throws Exception
	{
		TestHelperAttribute.testLangAttribute(this.n);
	}
	
	@Test
	public void testXmlLangAttribute() throws Exception
	{
		TestHelperAttribute.testXmlLangAttribute(this.n);
	}
}
