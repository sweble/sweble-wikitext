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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.sweble.wom3.Wom3ElementNode;
import org.sweble.wom3.Wom3Node;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BackbonePositionTest
{
	public static final short DISCONNECTED = Node.DOCUMENT_POSITION_DISCONNECTED;

	public static final short PRECEDING = Node.DOCUMENT_POSITION_PRECEDING;

	public static final short FOLLOWING = Node.DOCUMENT_POSITION_FOLLOWING;

	public static final short CONTAINS = Node.DOCUMENT_POSITION_CONTAINS;

	public static final short CONTAINED_BY = Node.DOCUMENT_POSITION_CONTAINED_BY;

	public static final short IMPLEMENTATION_SPECIFIC = Node.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC;

	// =========================================================================

	private Document refDoc;

	private Document womDoc;

	private Element div;

	private Element b;

	private Element i;

	private Element bq;

	private Element span;

	private Element br;

	private Element s;

	@SuppressWarnings("unused")
	private Attr bqx, bqy, bqz, i1, i2, i3, sa, sb, sc;

	// =========================================================================

	public BackbonePositionTest() throws ParserConfigurationException
	{
		DomImplementationImpl impl = DomImplementationImpl.get();
		womDoc = impl.createDocument(Wom3Node.WOM_NS_URI, "article", null);
		womDoc.removeChild(womDoc.getDocumentElement());

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		refDoc = docBuilder.newDocument();

		// Height 1 ( == root )
		div = elem("div");
		refDoc.appendChild(div);
		womDoc.appendChild(getPartner(div));

		// Height 2
		b = elem("b");
		append(div, b);
		i = elem("i");
		append(div, i);

		// Height 3
		bq = elem("blockquote");
		append(b, bq);
		span = elem("span");
		append(b, span);
		br = elem("br");
		append(i, br);
		s = elem("s");
		append(i, s);

		// bq attribs
		bqx = attr(bq, "bqx");
		bqy = attr(bq, "bqy");
		bqz = attr(bq, "bqz");

		// i attribs
		i1 = attr(i, "i1");
		i2 = attr(i, "i2");
		i3 = attr(i, "i3");

		// s attribs
		sa = attr(s, "sa");
		sb = attr(s, "sb");
		sc = attr(s, "sc");
	}

	// =========================================================================

	@Test
	public void testCompareDivNodes() throws Exception
	{
		check(0, div, div);
	}

	@Test
	public void testBfollowsDIV() throws Exception
	{
		check(CONTAINED_BY | FOLLOWING, div, b);
	}

	@Test
	public void testDIVprecedesB() throws Exception
	{
		check(CONTAINS | PRECEDING, b, div);
	}

	@Test
	public void testBprecedesI() throws Exception
	{
		check(PRECEDING, i, b);
	}

	@Test
	public void testBprecedesS() throws Exception
	{
		check(PRECEDING, s, b);
	}

	@Test
	public void testIfollowsB() throws Exception
	{
		check(FOLLOWING, b, i);
	}

	@Test
	public void testSfollowsB() throws Exception
	{
		check(FOLLOWING, b, s);
	}

	@Test
	public void testBRfollowsSPAN() throws Exception
	{
		check(FOLLOWING, span, br);
	}

	@Test
	public void testSPANprecedesBR() throws Exception
	{
		check(PRECEDING, br, span);
	}

	@Test
	public void testBRfollowsDIV() throws Exception
	{
		check(FOLLOWING | CONTAINED_BY, div, br);
	}

	@Test
	public void testDIVprecedesBR() throws Exception
	{
		check(PRECEDING | CONTAINS, br, div);
	}

	// =========================================================================

	@Test
	public void testI1precedesI2() throws Exception
	{
		check(PRECEDING | IMPLEMENTATION_SPECIFIC, i2, i1);
	}

	@Test
	public void testI2followsI1() throws Exception
	{
		check(FOLLOWING | IMPLEMENTATION_SPECIFIC, i1, i2);
	}

	@Test
	public void testSprecedesSB() throws Exception
	{
		check(PRECEDING | CONTAINS, sb, s);
	}

	@Test
	public void testSBfollowsS() throws Exception
	{
		check(FOLLOWING | CONTAINED_BY, s, sb);
	}

	@Test
	public void testSfollowsI2() throws Exception
	{
		check(FOLLOWING, i2, s);
	}

	@Test
	public void testI2precedesS() throws Exception
	{
		check(PRECEDING, s, i2);
	}

	@Test
	public void testBQXprecedesSC() throws Exception
	{
		check(PRECEDING, sc, bqx);
	}

	@Test
	public void testBQXfollowsSC() throws Exception
	{
		check(FOLLOWING, bqx, sc);
	}

	@Test
	public void testIprecedesSB() throws Exception
	{
		check(PRECEDING | CONTAINS, sb, i);
	}

	@Test
	public void testSBprecedesI() throws Exception
	{
		check(FOLLOWING | CONTAINED_BY, i, sb);
	}

	// =========================================================================

	private Attr attr(Element e, String name)
	{
		Attr ref = refDoc.createAttribute(name);
		Attr wom = womDoc.createAttribute(name);
		ref.setUserData("wom", wom, null);
		e.setAttributeNode(ref);
		((Wom3ElementNode) getPartner(e)).setAttributeNode(wom);
		return ref;
	}

	private Element elem(String tag)
	{
		Element ref = refDoc.createElementNS(Wom3Node.WOM_NS_URI, tag);
		Element wom = womDoc.createElementNS(Wom3Node.WOM_NS_URI, tag);
		ref.setUserData("wom", wom, null);
		return ref;
	}

	private Wom3Node getPartner(Node e)
	{
		return (Wom3Node) e.getUserData("wom");
	}

	private void append(Node parent, Node child)
	{
		parent.appendChild(child);
		getPartner(parent).appendChild(getPartner(child));
	}

	private void check(int expected, Node a, Node b)
	{
		short refActual = a.compareDocumentPosition(b);

		short actual = getPartner(a).compareDocumentPosition(getPartner(b));

		assertEquals(expected, refActual);
		assertEquals(expected, actual);
	}
}
