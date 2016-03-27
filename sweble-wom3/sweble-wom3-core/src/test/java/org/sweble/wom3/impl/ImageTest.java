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
package org.sweble.wom3.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;
import org.sweble.wom3.Wom3ImageFormat;
import org.sweble.wom3.Wom3ImageHAlign;
import org.sweble.wom3.Wom3ImageVAlign;

import de.fau.cs.osr.utils.WrappedException;

public class ImageTest
{
	private ImageImpl l = (ImageImpl) TestHelperDoc.genElem("image");

	@Test
	public void testCreateAndSetSourceWorks() throws Exception
	{
		l.setSource("Some Page");
		assertEquals("Some Page", l.getSource());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateIMageLinkWithEmptyPageThrows() throws Exception
	{
		l.setSource("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateImageLinkWithInvalidPageThrows() throws Exception
	{
		l.setSource("{}");
	}

	@Test
	public void testCanSetSourceAttribute() throws Exception
	{
		l.setSource("Some Other Page");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testCannotRemoveSourceAttribute() throws Exception
	{
		l.setSource(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetUnknownAttributeThrows() throws Exception
	{
		l.setAttribute("I don't exist", "Doesn't matter");
	}

	@Test
	public void testFormatAttribute() throws Exception
	{
		checkFormat(Wom3ImageFormat.FRAME, "frame");
		checkFormat(Wom3ImageFormat.FRAMELESS, "frameless");
		checkFormat(Wom3ImageFormat.THUMBNAIL, "thumbnail");
		checkFormat(Wom3ImageFormat.UNRESTRAINED, "unrestrained");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalFormatStringThrows() throws Exception
	{
		l.setAttribute("format", "I'm illegal ...");
	}

	private void checkFormat(Wom3ImageFormat format, String value)
	{
		l.setFormat(format);
		assertEquals(format, l.getFormat());
		assertEquals(value, l.getAttribute("format"));

		l.setFormat(null);
		assertEquals("", l.getAttribute("format"));
		assertNull(l.getFormat());

		l.setAttribute("format", value);
		assertEquals(format, l.getFormat());
		assertEquals(value, l.getAttribute("format"));

		l.setAttribute("format", null);
		assertEquals("", l.getAttribute("format"));
		assertNull(l.getFormat());
	}

	@Test
	public void testBorderAttribute() throws Exception
	{
		assertFalse(l.isBorder());

		l.setBorder(true);
		assertTrue(l.isBorder());
		assertEquals("border", l.getAttribute("border"));

		l.removeAttribute("border");
		assertFalse(l.isBorder());

		l.setAttribute("border", "Doesn't matter");
		assertTrue(l.isBorder());
		assertEquals("border", l.getAttribute("border"));

		l.setBorder(false);
		assertFalse(l.isBorder());
		assertEquals("", l.getAttribute("border"));
	}

	@Test
	public void testHAlignAttribute() throws Exception
	{
		checkHAlign(Wom3ImageHAlign.CENTER, "center");
		checkHAlign(Wom3ImageHAlign.DEFAULT, "default");
		checkHAlign(Wom3ImageHAlign.LEFT, "left");
		checkHAlign(Wom3ImageHAlign.NONE, "none");
		checkHAlign(Wom3ImageHAlign.RIGHT, "right");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalHAlignStringThrows() throws Exception
	{
		l.setAttribute("halign", "I'm illegal ...");
	}

	private void checkHAlign(Wom3ImageHAlign halign, String value)
	{
		l.setHAlign(halign);
		assertEquals(halign, l.getHAlign());
		assertEquals(value, l.getAttribute("halign"));

		l.setHAlign(null);
		assertEquals("", l.getAttribute("halign"));
		assertNull(l.getHAlign());

		l.setAttribute("halign", value);
		assertEquals(halign, l.getHAlign());
		assertEquals(value, l.getAttribute("halign"));

		l.setAttribute("halign", null);
		assertEquals("", l.getAttribute("halign"));
		assertNull(l.getHAlign());
	}

	@Test
	public void testVAlignAttribute() throws Exception
	{
		checkVAlign(Wom3ImageVAlign.BASELINE, "baseline");
		checkVAlign(Wom3ImageVAlign.BOTTOM, "bottom");
		checkVAlign(Wom3ImageVAlign.MIDDLE, "middle");
		checkVAlign(Wom3ImageVAlign.SUB, "sub");
		checkVAlign(Wom3ImageVAlign.SUPER, "super");
		checkVAlign(Wom3ImageVAlign.TEXT_BOTTOM, "text-bottom");
		checkVAlign(Wom3ImageVAlign.TEXT_TOP, "text-top");
		checkVAlign(Wom3ImageVAlign.TOP, "top");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalVAlignStringThrows() throws Exception
	{
		l.setAttribute("valign", "I'm illegal ...");
	}

	private void checkVAlign(Wom3ImageVAlign valign, String value)
	{
		l.setVAlign(valign);
		assertEquals(valign, l.getVAlign());
		assertEquals(value, l.getAttribute("valign"));

		l.setVAlign(null);
		assertEquals("", l.getAttribute("valign"));
		assertNull(l.getVAlign());

		l.setAttribute("valign", value);
		assertEquals(valign, l.getVAlign());
		assertEquals(value, l.getAttribute("valign"));

		l.setAttribute("valign", null);
		assertEquals("", l.getAttribute("valign"));
		assertNull(l.getVAlign());
	}

	@Test
	public void testWidthAttribute() throws Exception
	{
		l.setWidth(128);
		assertEquals((Integer) 128, l.getWidth());
		assertEquals("128", l.getAttribute("width"));

		l.setWidth(null);
		assertEquals("", l.getAttribute("width"));
		assertNull(l.getWidth());

		l.setAttribute("width", "128");
		assertEquals((Integer) 128, l.getWidth());
		assertEquals("128", l.getAttribute("width"));

		l.setAttribute("width", null);
		assertEquals("", l.getAttribute("width"));
		assertNull(l.getWidth());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalWidthStringThrows() throws Exception
	{
		l.setAttribute("width", "Onehundredtwentyeight");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeWidthStringThrows() throws Exception
	{
		l.setAttribute("width", "-10");
	}

	@Test
	public void testHeightAttribute() throws Exception
	{
		l.setHeight(128);
		assertEquals((Integer) 128, l.getHeight());
		assertEquals("128", l.getAttribute("height"));

		l.setHeight(null);
		assertEquals("", l.getAttribute("height"));
		assertNull(l.getHeight());

		l.setAttribute("height", "128");
		assertEquals((Integer) 128, l.getHeight());
		assertEquals("128", l.getAttribute("height"));

		l.setAttribute("height", null);
		assertEquals("", l.getAttribute("height"));
		assertNull(l.getHeight());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalHeightStringThrows() throws Exception
	{
		l.setAttribute("height", "Onehundredtwentyeight");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeHeightStringThrows() throws Exception
	{
		l.setAttribute("height", "-10");
	}

	@Test
	public void testUprightAttribute() throws Exception
	{
		assertFalse(l.isUpright());

		l.setUpright(true);
		assertTrue(l.isUpright());
		assertEquals("upright", l.getAttribute("upright"));

		l.removeAttribute("upright");
		assertFalse(l.isUpright());

		l.setAttribute("upright", "Doesn't matter");
		assertTrue(l.isUpright());
		assertEquals("upright", l.getAttribute("upright"));

		l.setUpright(false);
		assertFalse(l.isUpright());
		assertEquals("", l.getAttribute("upright"));
	}

	@Test
	public void testExtLinkAttribute() throws Exception
	{
		l.setExtLink(new URL("http://example.com"));
		assertEquals(new URL("http://example.com"), l.getExtLink());
		assertEquals("http://example.com", l.getAttribute("extlink"));

		l.setExtLink(null);
		assertEquals("", l.getAttribute("extlink"));
		assertNull(l.getExtLink());

		l.setAttribute("extlink", "http://example.com");
		assertEquals(new URL("http://example.com"), l.getExtLink());
		assertEquals("http://example.com", l.getAttribute("extlink"));

		l.setAttribute("extlink", null);
		assertEquals("", l.getAttribute("extlink"));
		assertNull(l.getExtLink());
	}

	@Test(expected = MalformedURLException.class)
	public void testIllegalExtLinkStringThrows() throws Throwable
	{
		try
		{
			l.setAttribute("extlink", "!§$%&/()=?");
		}
		catch (WrappedException e)
		{
			throw e.getCause();
		}
	}

	@Test(expected = MalformedURLException.class)
	public void testEmptyExtLinkStringThrows() throws Throwable
	{
		try
		{
			l.setAttribute("extlink", "");
		}
		catch (WrappedException e)
		{
			throw e.getCause();
		}
	}

	@Test
	public void testIntLinkAttribute() throws Exception
	{
		l.setIntLink("Some Page");
		assertEquals("Some Page", l.getIntLink());
		assertEquals("Some Page", l.getAttribute("intlink"));

		l.setIntLink(null);
		assertEquals("", l.getAttribute("intlink"));
		assertNull(l.getIntLink());

		l.setAttribute("intlink", "Some Page");
		assertEquals("Some Page", l.getIntLink());
		assertEquals("Some Page", l.getAttribute("intlink"));

		l.setAttribute("intlink", null);
		assertEquals("", l.getAttribute("intlink"));
		assertNull(l.getIntLink());
	}

	@Test
	public void testSetIntLinkAcceptsEmptyString() throws Exception
	{
		l.setIntLink("");
		l.setAttribute("intlink", "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalIntLinkStringThrows() throws Exception
	{
		l.setAttribute("intlink", "{}");
	}

	@Test
	public void testAltAttribute() throws Exception
	{
		l.setAlt("Some text");
		assertEquals("Some text", l.getAlt());
		assertEquals("Some text", l.getAttribute("alt"));

		l.setAlt(null);
		assertEquals("", l.getAttribute("alt"));
		assertNull(l.getAlt());

		l.setAttribute("alt", "Some text");
		assertEquals("Some text", l.getAlt());
		assertEquals("Some text", l.getAttribute("alt"));

		l.setAttribute("alt", null);
		assertEquals("", l.getAttribute("alt"));
		assertNull(l.getAlt());
	}

	@Test
	public void testCaptionSetterAndGetter() throws Exception
	{
		assertNull(l.getCaption());

		ImageCaptionImpl caption = (ImageCaptionImpl) TestHelperDoc.genElem("imgcaption");
		l.setCaption(caption);
		assertTrue(caption == l.getCaption());
		assertTrue(caption == l.getFirstChild());
		assertTrue(caption == l.getLastChild());

		l.setCaption(null);
		assertNull(l.getCaption());
		assertNull(l.getFirstChild());
		assertNull(l.getLastChild());
	}

	@Test
	public void testReplaceCaption() throws Exception
	{
		ImageCaptionImpl caption = (ImageCaptionImpl) TestHelperDoc.genElem("imgcaption");
		l.setCaption(caption);

		ImageCaptionImpl newCaption = (ImageCaptionImpl) TestHelperDoc.genElem("imgcaption");
		l.replaceChild(newCaption, caption);
		assertTrue(newCaption == l.getCaption());
		assertTrue(newCaption == l.getFirstChild());
		assertTrue(newCaption == l.getLastChild());
	}

	@Test
	public void testGetLinkTargetGetter() throws Exception
	{
		assertEquals(l.getSource(), l.getLinkTarget());
		String intLink = "Some other target";
		l.setIntLink(intLink);
		assertEquals(intLink, l.getLinkTarget());
		l.setIntLink(null);
		assertEquals(l.getSource(), l.getLinkTarget());
	}

	@Test
	@Ignore
	public void testGetLinkTitleGetter() throws Exception
	{
		assertEquals(l.getSource(), l.getLinkTitle().getFirstChild().getNodeValue());
		String alt = "Some alt text";
		l.setAlt(alt);
		assertEquals(alt, l.getLinkTitle().getFirstChild().getNodeValue());
		l.setAlt(null);
		assertEquals(l.getSource(), l.getLinkTitle().getFirstChild().getNodeValue());
	}
}
