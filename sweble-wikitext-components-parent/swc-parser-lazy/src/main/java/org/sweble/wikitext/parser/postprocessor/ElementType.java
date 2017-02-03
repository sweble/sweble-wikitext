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

package org.sweble.wikitext.parser.postprocessor;

import static org.sweble.wikitext.parser.nodes.WtNode.NT_BODY;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_BOLD;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_DEFINITION_LIST;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_DEFINITION_LIST_DEF;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_DEFINITION_LIST_TERM;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_EXTERNAL_LINK;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_HEADING;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_HORIZONTAL_RULE;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_IMAGE_LINK;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_IM_END_TAG;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_IM_START_TAG;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_INTERNAL_LINK;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_ITALICS;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_LCT_VAR_CONV;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_LIST_ITEM;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_ORDERED_LIST;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_PARAGRAPH;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_PARSED_WIKITEXT_PAGE;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_SECTION;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_SEMI_PRE;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_TABLE;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_TABLE_CAPTION;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_TABLE_CELL;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_TABLE_HEADER;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_TABLE_IMPLICIT_TBODY;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_TABLE_ROW;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_UNORDERED_LIST;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_URL;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_ELEMENT;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_EMPTY_TAG;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_END_TAG;
import static org.sweble.wikitext.parser.nodes.WtNode.NT_XML_START_TAG;

import org.sweble.wikitext.parser.nodes.WtImageLink;
import org.sweble.wikitext.parser.nodes.WtNamedXmlElement;
import org.sweble.wikitext.parser.nodes.WtNode;

import de.fau.cs.osr.utils.DualHashBidiMap;

public enum ElementType
{
	// == Meta Element Types ===================================================

	PAGE,
	SECTION,
	SECTION_HEADING,
	SECTION_BODY,
	SEMIPRE,
	INLINE_IMG,
	FRAMED_IMG,
	LCT_VAR_CONV,
	UNKNOWN,

	// A replacements
	INT_LINK,
	EXT_LINK,
	URL,

	// == HTML Version 4.01 ====================================================

	/*
	A,
	*/
	ABBR, // Normal Tag
	ACRONYM, // Normal Tag
	ADDRESS,
	// APPLET
	AREA, // Normal Tag
	B,
	// BASE
	// BASEFONT
	BDO, // Normal Tag
	BIG,
	BLOCKQUOTE,
	// BODY
	BR,
	// BUTTON,
	CAPTION,
	CENTER,
	CITE, // Normal Tag
	CODE,
	COL,
	COLGROUP,
	DD,
	DEL, // Normal Tag
	DFN, // Normal Tag
	DIR,
	DIV,
	DL,
	DT,
	EM,
	// FIELDSET,
	FONT,
	// FORM
	// FRAME
	// FRAMESET
	H1, H2, H3, H4, H5, H6,
	// HEAD
	HR,
	// HTML
	I,
	// IFRAME
	IMG,
	// INPUT
	INS, // Normal Tag
	// ISINDEX
	KBD, // Normal Tag
	// LABEL
	// LEGEND
	LI,
	// LINK
	MAP, // Normal Tag
	MENU,
	// META
	// NOFRAMES
	// NOSCRIPT
	// OBJECT
	OL,
	// OPTGROUP
	// OPTION
	P,
	// PARAM
	PRE,
	Q, // Normal Tag
	S,
	SAMP, // Normal Tag
	// STRICT
	// SELECT
	SMALL,
	SPAN, // Normal Tag
	STRIKE,
	STRONG,
	// STYLE
	SUB, // Normal Tag
	SUP, // Normal Tag
	TABLE,
	TBODY,
	TD,
	// TEXTAREA
	TFOOT,
	TH,
	THEAD,
	// TITLE
	TR,
	TT,
	U,
	UL,
	VAR, // Normal Tag 
	;

	// =========================================================================

	public String getXmlTagName()
	{
		return (String) xmlElementTypeMap.getKey(this);
	}

	public boolean isSpecial()
	{
		switch (this)
		{
			case PAGE:
			case SECTION:
			case SECTION_HEADING:
			case SEMIPRE:
			case INLINE_IMG:
			case FRAMED_IMG:

				// --------------

			case ADDRESS:
				// case APPLET:
			case AREA:
				// case ARTICLE:
				// case ASIDE:
				// case BASE:
				// case BASEFONT:
				// case BGSOUND:
			case BLOCKQUOTE:
				// case BODY:
			case BR:
				// case BUTTON:
			case CAPTION:
			case CENTER:
			case COL:
			case COLGROUP:
				// case COMMAND:
			case DD:
				// case DETAILS:
			case DIR:
			case DIV:
			case DL:
			case DT:
				// case EMBED:
				// case FIELDSET:
				// case FIGCAPTION:
				// case FIGURE:
				// case FOOTER:
				// case FORM:
				// case FRAME:
				// case FRAMESET:
			case H1:
			case H2:
			case H3:
			case H4:
			case H5:
			case H6:
				// case HEAD:
				// case HEADER:
				// case HGROUP:
			case HR:
				// case HTML:
				// case IFRAME:
			case IMG:
				// case INPUT:
				// case ISINDEX:
			case LI:
				// case LINK:
				// case LISTING:
				// case MARQUEE:
			case MENU:
				// case META:
				// case NAV:
				// case NOEMBED:
				// case NOFRAMES:
				// case NOSCRIPT:
				// case OBJECT:
			case OL:
			case P:
				// case PARAM:
				// case PLAINTEXT:
			case PRE:
				// case SCRIPT:
				// case SECTION:
				// case SELECT:
				// case SOURCE:
				// case STYLE:
				// case SUMMARY:
			case TABLE:
			case TBODY:
			case TD:
				// case TEXTAREA:
			case TFOOT:
			case TH:
			case THEAD:
				// case TITLE:
			case TR:
				// case TRACK:
			case UL:
				// case WBR:
				// case XMP:
				return true;

			default:
				return false;
		}
	}

	public boolean isFormatting()
	{
		switch (this)
		{
			case INT_LINK:
			case EXT_LINK:
			case URL:
			case LCT_VAR_CONV:
				// case A:
			case B:
			case BIG:
			case CODE:
			case EM:
			case FONT:
			case I:
				// case NOBR:
			case S:
			case SMALL:
			case STRIKE:
			case STRONG:
			case TT:
			case U:
				return true;

			default:
				return false;
		}
	}

	// =========================================================================

	private static final DualHashBidiMap xmlElementTypeMap = new DualHashBidiMap();

	static
	{
		/* Bug 35: We must not recognize <a> tags!
		xmlElementTypeMap.put("a", A);
		*/
		xmlElementTypeMap.put("#int-link", INT_LINK);
		xmlElementTypeMap.put("#ext-link", EXT_LINK);
		xmlElementTypeMap.put("#url", URL);

		xmlElementTypeMap.put("abbr", ABBR);
		xmlElementTypeMap.put("acronym", ACRONYM);
		xmlElementTypeMap.put("address", ADDRESS);
		xmlElementTypeMap.put("area", AREA);
		xmlElementTypeMap.put("b", B);
		xmlElementTypeMap.put("bdo", BDO);
		xmlElementTypeMap.put("big", BIG);
		xmlElementTypeMap.put("blockquote", BLOCKQUOTE);
		xmlElementTypeMap.put("br", BR);
		xmlElementTypeMap.put("caption", CAPTION);
		xmlElementTypeMap.put("center", CENTER);
		xmlElementTypeMap.put("cite", CITE);
		xmlElementTypeMap.put("code", CODE);
		xmlElementTypeMap.put("col", COL);
		xmlElementTypeMap.put("colgroup", COLGROUP);
		xmlElementTypeMap.put("dd", DD);
		xmlElementTypeMap.put("del", DEL);
		xmlElementTypeMap.put("dfn", DFN);
		xmlElementTypeMap.put("dir", DIR);
		xmlElementTypeMap.put("div", DIV);
		xmlElementTypeMap.put("dl", DL);
		xmlElementTypeMap.put("dt", DT);
		xmlElementTypeMap.put("em", EM);
		xmlElementTypeMap.put("font", FONT);
		xmlElementTypeMap.put("h1", H1);
		xmlElementTypeMap.put("h2", H2);
		xmlElementTypeMap.put("h3", H3);
		xmlElementTypeMap.put("h4", H4);
		xmlElementTypeMap.put("h5", H5);
		xmlElementTypeMap.put("h6", H6);
		xmlElementTypeMap.put("hr", HR);
		xmlElementTypeMap.put("i", I);
		// Image elements are not supported as pure HTML
		//xmlElementTypeMap.put("img", IMG);
		xmlElementTypeMap.put("ins", INS);
		xmlElementTypeMap.put("kbd", KBD);
		xmlElementTypeMap.put("li", LI);
		xmlElementTypeMap.put("map", MAP);
		xmlElementTypeMap.put("menu", MENU);
		xmlElementTypeMap.put("ol", OL);
		xmlElementTypeMap.put("p", P);
		xmlElementTypeMap.put("pre", PRE);
		xmlElementTypeMap.put("q", Q);
		xmlElementTypeMap.put("s", S);
		xmlElementTypeMap.put("samp", SAMP);
		xmlElementTypeMap.put("small", SMALL);
		xmlElementTypeMap.put("span", SPAN);
		xmlElementTypeMap.put("strike", STRIKE);
		xmlElementTypeMap.put("strong", STRONG);
		xmlElementTypeMap.put("sub", SUB);
		xmlElementTypeMap.put("sup", SUP);
		xmlElementTypeMap.put("table", TABLE);
		xmlElementTypeMap.put("tbody", TBODY);
		xmlElementTypeMap.put("td", TD);
		xmlElementTypeMap.put("tfoot", TFOOT);
		xmlElementTypeMap.put("th", TH);
		xmlElementTypeMap.put("thead", THEAD);
		xmlElementTypeMap.put("tr", TR);
		xmlElementTypeMap.put("tt", TT);
		xmlElementTypeMap.put("u", U);
		xmlElementTypeMap.put("ul", UL);
		xmlElementTypeMap.put("var", VAR);

		// It's a BIDI map, the following lines would remove the 
		// non-intermediate tags!

		// Intermediate tags
		//xmlElementTypeMap.put("@b", B);
		//xmlElementTypeMap.put("@i", I);
		//xmlElementTypeMap.put("@p", P);
		xmlElementTypeMap.put("#inline-img", INLINE_IMG);
		xmlElementTypeMap.put("#framed-img", FRAMED_IMG);
		xmlElementTypeMap.put("#semipre", SEMIPRE);
		xmlElementTypeMap.put("#lct-var-conv", LCT_VAR_CONV);
	}

	// =========================================================================

	public static ElementType getType(WtNode n)
	{
		switch (n.getNodeType())
		{
			case NT_PARSED_WIKITEXT_PAGE:
				return PAGE;
			case NT_SECTION:
				return SECTION;
			case NT_HEADING:
				return SECTION_HEADING;
			case NT_BODY:
				// FIXME: Pretending that body can only be a section body might 
				//        be dangerous?
				return SECTION_BODY;

			case NT_DEFINITION_LIST:
				return DL;
			case NT_ORDERED_LIST:
				return OL;
			case NT_PARAGRAPH:
				return P;
			case NT_UNORDERED_LIST:
				return UL;
			case NT_LIST_ITEM:
				return LI;
			case NT_DEFINITION_LIST_DEF:
				return DD;
			case NT_DEFINITION_LIST_TERM:
				return DT;
			case NT_TABLE:
				return TABLE;
			case NT_TABLE_CAPTION:
				return CAPTION;
			case NT_TABLE_CELL:
				return TD;
			case NT_TABLE_HEADER:
				return TH;
			case NT_TABLE_ROW:
				return TR;
			case NT_TABLE_IMPLICIT_TBODY:
				return TBODY;
			case NT_BOLD:
				return B;
			case NT_ITALICS:
				return I;
			case NT_SEMI_PRE:
				return SEMIPRE;
			case NT_HORIZONTAL_RULE:
				return HR;

			case NT_IMAGE_LINK:
				return TreeBuilder.isInlineImage((WtImageLink) n) ?
						INLINE_IMG :
						FRAMED_IMG;

			case NT_INTERNAL_LINK:
				return INT_LINK;
			case NT_EXTERNAL_LINK:
				return EXT_LINK;
			case NT_URL:
				return URL;

			case NT_XML_START_TAG:
			case NT_IM_START_TAG:
			case NT_XML_END_TAG:
			case NT_IM_END_TAG:
			case NT_XML_EMPTY_TAG:
			case NT_XML_ELEMENT:
				return getType((WtNamedXmlElement) n);

			case NT_LCT_VAR_CONV:
				return LCT_VAR_CONV;

			default:
				throw new AssertionError();
		}
	}

	public static ElementType getType(WtNamedXmlElement e)
	{
		String name = e.getName().toLowerCase();
		if (name.isEmpty())
			throw new AssertionError();

		if (name.charAt(0) == '@')
		{
			return (ElementType) xmlElementTypeMap.get(name.substring(1));
		}
		else
		{
			ElementType type = (ElementType) xmlElementTypeMap.get(name);
			if (type == null)
				return UNKNOWN;

			return type;
		}
	}
}
