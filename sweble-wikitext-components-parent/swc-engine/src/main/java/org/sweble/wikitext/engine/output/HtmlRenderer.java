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
package org.sweble.wikitext.engine.output;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.CompleteEngineVisitorNoReturn;
import org.sweble.wikitext.engine.nodes.EngNowiki;
import org.sweble.wikitext.engine.nodes.EngPage;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.nodes.EngSoftErrorNode;
import org.sweble.wikitext.engine.nodes.EngineNodeFactory;
import org.sweble.wikitext.engine.utils.EngineAstTextUtils;
import org.sweble.wikitext.engine.utils.UrlEncoding;
import org.sweble.wikitext.parser.nodes.WtBody;
import org.sweble.wikitext.parser.nodes.WtBold;
import org.sweble.wikitext.parser.nodes.WtDefinitionList;
import org.sweble.wikitext.parser.nodes.WtDefinitionListDef;
import org.sweble.wikitext.parser.nodes.WtDefinitionListTerm;
import org.sweble.wikitext.parser.nodes.WtExternalLink;
import org.sweble.wikitext.parser.nodes.WtHeading;
import org.sweble.wikitext.parser.nodes.WtHorizontalRule;
import org.sweble.wikitext.parser.nodes.WtIgnored;
import org.sweble.wikitext.parser.nodes.WtIllegalCodePoint;
import org.sweble.wikitext.parser.nodes.WtImEndTag;
import org.sweble.wikitext.parser.nodes.WtImStartTag;
import org.sweble.wikitext.parser.nodes.WtImageLink;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageHorizAlign;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageViewFormat;
import org.sweble.wikitext.parser.nodes.WtInternalLink;
import org.sweble.wikitext.parser.nodes.WtItalics;
import org.sweble.wikitext.parser.nodes.WtLinkOptionAltText;
import org.sweble.wikitext.parser.nodes.WtLinkOptionGarbage;
import org.sweble.wikitext.parser.nodes.WtLinkOptionKeyword;
import org.sweble.wikitext.parser.nodes.WtLinkOptionLinkTarget;
import org.sweble.wikitext.parser.nodes.WtLinkOptionResize;
import org.sweble.wikitext.parser.nodes.WtLinkOptions;
import org.sweble.wikitext.parser.nodes.WtLinkTitle;
import org.sweble.wikitext.parser.nodes.WtListItem;
import org.sweble.wikitext.parser.nodes.WtName;
import org.sweble.wikitext.parser.nodes.WtNewline;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtOnlyInclude;
import org.sweble.wikitext.parser.nodes.WtOrderedList;
import org.sweble.wikitext.parser.nodes.WtPageName;
import org.sweble.wikitext.parser.nodes.WtPageSwitch;
import org.sweble.wikitext.parser.nodes.WtParagraph;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtPreproWikitextPage;
import org.sweble.wikitext.parser.nodes.WtRedirect;
import org.sweble.wikitext.parser.nodes.WtSection;
import org.sweble.wikitext.parser.nodes.WtSemiPre;
import org.sweble.wikitext.parser.nodes.WtSemiPreLine;
import org.sweble.wikitext.parser.nodes.WtSignature;
import org.sweble.wikitext.parser.nodes.WtTable;
import org.sweble.wikitext.parser.nodes.WtTableCaption;
import org.sweble.wikitext.parser.nodes.WtTableCell;
import org.sweble.wikitext.parser.nodes.WtTableHeader;
import org.sweble.wikitext.parser.nodes.WtTableImplicitTableBody;
import org.sweble.wikitext.parser.nodes.WtTableRow;
import org.sweble.wikitext.parser.nodes.WtTagExtension;
import org.sweble.wikitext.parser.nodes.WtTagExtensionBody;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateArgument;
import org.sweble.wikitext.parser.nodes.WtTemplateArguments;
import org.sweble.wikitext.parser.nodes.WtTemplateParameter;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtTicks;
import org.sweble.wikitext.parser.nodes.WtUnorderedList;
import org.sweble.wikitext.parser.nodes.WtUrl;
import org.sweble.wikitext.parser.nodes.WtValue;
import org.sweble.wikitext.parser.nodes.WtWhitespace;
import org.sweble.wikitext.parser.nodes.WtXmlAttribute;
import org.sweble.wikitext.parser.nodes.WtXmlAttributeGarbage;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes;
import org.sweble.wikitext.parser.nodes.WtXmlCharRef;
import org.sweble.wikitext.parser.nodes.WtXmlComment;
import org.sweble.wikitext.parser.nodes.WtXmlElement;
import org.sweble.wikitext.parser.nodes.WtXmlEmptyTag;
import org.sweble.wikitext.parser.nodes.WtXmlEndTag;
import org.sweble.wikitext.parser.nodes.WtXmlEntityRef;
import org.sweble.wikitext.parser.nodes.WtXmlStartTag;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wikitext.parser.utils.StringConversionException;
import org.sweble.wikitext.parser.utils.WtRtDataPrinter;

import de.fau.cs.osr.utils.FmtNotYetImplementedError;
import de.fau.cs.osr.utils.StringTools;
import de.fau.cs.osr.utils.visitor.VisitingException;

public class HtmlRenderer
		extends
			HtmlRendererBase
		implements
			CompleteEngineVisitorNoReturn
{
	@Override
	public void visit(EngProcessedPage n)
	{
		dispatch(n.getPage());
	}

	@Override
	public void visit(EngNowiki n)
	{
		wrapText(n.getContent());
	}

	public void visit(EngPage n)
	{
		iterate(n);
	}

	@Override
	public void visit(EngSoftErrorNode n)
	{
		visit((WtXmlElement) n);
	}

	@Override
	public void visit(WtBody n)
	{
		iterate(n);
	}

	public void visit(WtBold n)
	{
		p.indentAtBol("<b>");
		p.incIndent();
		iterate(n);
		p.decIndent();
		p.indentAtBol("</b>");
	}

	public void visit(WtDefinitionList n)
	{
		p.indentln("<dl>");
		p.incIndent();
		iterate(n);
		p.decIndent();
		p.indentln("</dl>");
	}

	public void visit(WtDefinitionListDef n)
	{
		p.indentln("<dd>");
		p.incIndent();
		iterate(n);
		p.decIndent();
		p.indentln("</dd>");
	}

	public void visit(WtDefinitionListTerm n)
	{
		p.indentln("<dt>");
		p.incIndent();
		iterate(n);
		p.decIndent();
		p.indentln("</dt>");
	}

	public void visit(WtExternalLink n)
	{
		if (n.hasTitle())
		{
			p.indentAtBol();

			pt("<a rel=\"nofollow\" class=\"external text\" href=\"%s\">%!</a>",
					callback.makeUrl(n.getTarget()),
					n.getTitle());
		}
		else
		{
			throw new FmtNotYetImplementedError();
		}
	}

	@Override
	public void visit(WtHeading n)
	{
		// We handle this case in WtSection and don't dispatch to the heading.
		throw new InternalError();
	}

	@Override
	public void visit(WtHorizontalRule n)
	{
		p.indentAtBol("<hr />");
	}

	@Override
	public void visit(WtIgnored n)
	{
		// Well, ignore it ...
	}

	@Override
	public void visit(WtIllegalCodePoint n)
	{
		p.indentAtBol();

		final String cp = n.getCodePoint();
		for (int i = 0; i < cp.length(); ++i)
			pf("&amp;#%d;", (int) cp.charAt(i));
	}

	public void visit(WtImageLink n)
	{
		if (!n.getTarget().isResolved())
		{
			printAsWikitext(n);
			return;
		}

		PageTitle target;
		try
		{
			target = PageTitle.make(wikiConfig, n.getTarget().getAsString());
		}
		catch (LinkTargetException e)
		{
			throw new VisitingException(e);
		}

		int imgWidth = n.getWidth();
		int imgHeight = n.getHeight();

		switch (n.getFormat())
		{
			case THUMBNAIL: // FALL THROUGH
			case FRAMELESS:
				if (imgWidth <= 0)
					imgWidth = 180;
				break;
			default:
				break;
		}

		if (n.getUpright())
		{
			imgWidth = 140;
			imgHeight = -1;
		}

		MediaInfo info;
		try
		{
			info = callback.getMediaInfo(
					target.getNormalizedFullTitle(),
					imgWidth,
					imgHeight);
		}
		catch (Exception e)
		{
			throw new VisitingException(e);
		}

		boolean exists = (info != null && info.getImgUrl() != null);

		boolean isImage = !target.getTitle().endsWith(".ogg");

		if (exists && imgHeight > 0)
		{
			int altWidth = imgHeight * info.getImgWidth() / info.getImgHeight();
			if (altWidth < imgWidth)
			{
				imgWidth = altWidth;
				try
				{
					info = callback.getMediaInfo(
							target.getNormalizedFullTitle(),
							imgWidth,
							imgHeight);
				}
				catch (Exception e)
				{
					throw new VisitingException(e);
				}
			}
		}

		boolean scaled = imgWidth > 0 || imgHeight > 0;

		String imgUrl = null;
		if (exists)
		{
			imgUrl = info.getImgUrl();
			if (scaled && info.getThumbUrl() != null)
				imgUrl = info.getThumbUrl();
		}

		String aClasses = "";
		String imgClasses = "";

		switch (n.getFormat())
		{
			case THUMBNAIL:
				imgClasses += " thumbimage";
				break;
			default:
				break;
		}

		if (n.getBorder())
			imgClasses += " thumbborder";

		// -- does the image link something? --

		WtUrl linkUrl = null;
		PageTitle linkTarget = target;
		switch (n.getLink().getTargetType())
		{
			case NO_LINK:
				linkTarget = null;
				break;
			case PAGE:
			{
				WtPageName pageName = (WtPageName) n.getLink().getTarget();
				if (pageName.isResolved())
				{
					try
					{
						linkTarget = PageTitle.make(wikiConfig, pageName.getAsString());
					}
					catch (LinkTargetException e)
					{
						throw new VisitingException(e);
					}
				}
				else
				{
					linkTarget = null;
				}
				break;
			}
			case URL:
				linkTarget = null;
				linkUrl = (WtUrl) n.getLink().getTarget();
				break;
			case DEFAULT:
				if (exists && isImage)
					aClasses += " image";
				break;
		}

		// -- string caption --

		String strCaption = null;
		if (n.hasTitle())
			strCaption = makeImageCaption(n);

		// -- <img> alt --

		String alt = null;
		if (n.hasAlt())
			alt = makeImageAltText(n);

		// -- <a> classes

		if (!aClasses.isEmpty())
			aClasses = String.format(" class=\"%s\"", aClasses.trim());

		// -- <a> title --

		String aTitle = "";
		if (n.getFormat() != ImageViewFormat.FRAMELESS)
		{
			if (strCaption != null)
			{
				aTitle = strCaption;
			}
			else if (linkTarget != null)
			{
				aTitle = makeImageTitle(n, target);//makeUrl(linkTarget);
			}
			else if (linkUrl != null)
			{
				aTitle = callback.makeUrl(linkUrl);
			}
		}
		if (!aTitle.isEmpty())
			aTitle = String.format(" title=\"%s\"", aTitle);

		// -- width & height --

		int width = -1;
		int height = -1;

		if (exists)
		{
			width = scaled ? info.getThumbWidth() : info.getImgWidth();

			height = scaled ? info.getThumbHeight() : info.getImgHeight();
		}
		else
			width = 180;

		// -- generate html --

		boolean hasThumbFrame = isImage &&
				n.getFormat() == ImageViewFormat.THUMBNAIL ||
				n.getHAlign() != ImageHorizAlign.UNSPECIFIED;

		if (hasThumbFrame)
		{
			String align = "";
			switch (n.getHAlign())
			{
				case CENTER:
					align = " center";
					break;
				case LEFT:
					align = " tleft";
					break;
				case RIGHT: // FALL THROUGH
				case NONE: // FALL THROUGH
				default:
					align = " tright";
					break;
			}

			String thumb = "";
			String inner = "floatnone";
			String style = "";
			if (n.getFormat() == ImageViewFormat.THUMBNAIL)
			{
				thumb = "thumb";
				inner = "thumbinner";
				style = String.format(" style=\"width:%dpx;\"", width + 2);
			}

			p.indent();
			pf("<div class=\"%s\">", (thumb + align).trim());
			p.incIndent();
			p.indent();
			pf("<div class=\"%s\"%s>", inner, style);
			p.println();
			p.incIndent();

			aTitle = "";
			if (!exists)
				aTitle = String.format(" title=\"%s\"", makeImageTitle(n, target));
		}
		else
		{
			if (alt == null)
				alt = strCaption;
		}

		if (alt == null)
			alt = "";

		p.indentAtBol();
		if (linkTarget != null || linkUrl != null)
		{
			pf("<a href=\"%s\"%s%s>",
					linkTarget != null ? callback.makeUrl(linkTarget) : callback.makeUrl(linkUrl),
					aClasses,
					aTitle);
		}

		if (!imgClasses.isEmpty())
			imgClasses = String.format(" class=\"%s\"", imgClasses.trim());

		if (exists)
		{
			if (isImage)
			{
				pt("<img alt=\"%s\" src=\"%s\" width=\"%d\" height=\"%d\"%s />",
						alt.trim(),
						imgUrl,
						width,
						height,
						imgClasses);
			}
			else
			{
				p.print(esc(makeImageTitle(n, target)));
			}
		}
		else
		{
			p.print(esc(makeImageTitle(n, target)));
		}

		if (linkTarget != null || linkUrl != null)
			p.print("</a>");

		if (n.getFormat() == ImageViewFormat.THUMBNAIL)
		{
			if (exists)
			{
				p.indentln("<div class=\"thumbcaption\">");
				p.incIndent();
				p.indentln("<div class=\"magnify\">");
				p.incIndent();
				p.indent();
				pf("<a href=\"%s\" class=\"internal\" title=\"Enlarge\"><img src=\"/mediawiki/skins/common/images/magnify-clip.png\" width=\"15\" height=\"11\" alt=\"\" /></a>",
						callback.makeUrl(linkTarget));
				p.decIndent();
				p.indentln("</div>");
				dispatch(n.getTitle());
				p.decIndent();
				p.indentln("</div>");
			}
			else
			{
				p.indent();
				pt("<div class=\"thumbcaption\">%!</div>", n.getTitle());
			}
		}

		if (hasThumbFrame)
		{
			p.decIndent();
			p.indentln("</div>");
			p.decIndent();
			p.indentln("</div>");
		}
	}

	@Override
	public void visit(WtImEndTag n)
	{
		// Should not happen ...
		throw new InternalError();
	}

	@Override
	public void visit(WtImStartTag n)
	{
		// Should not happen ...
		throw new InternalError();
	}

	public void visit(WtInternalLink n)
	{
		if (!n.getTarget().isResolved())
		{
			printAsWikitext(n);
			return;
		}

		p.indentAtBol();

		PageTitle target;
		try
		{
			target = PageTitle.make(wikiConfig, n.getTarget().getAsString());
		}
		catch (LinkTargetException e)
		{
			throw new VisitingException(e);
		}

		// FIXME: I think these should be removed in the parser already?!
		if (target.getNamespace() == wikiConfig.getNamespace("Category"))
			return;

		if (!callback.resourceExists(target))
		{
			String title = target.getDenormalizedFullTitle();

			String path = UrlEncoding.WIKI.encode(target.getNormalizedFullTitle());

			if (n.hasTitle())
			{
				pt("<a href=\"%s\" class=\"new\" title=\"%s (page does not exist)\">%=%!%=</a>",
						callback.makeUrlMissingTarget(path),
						title,
						n.getPrefix(),
						n.getTitle(),
						n.getPostfix());
			}
			else
			{
				String linkText = makeTitleFromTarget(n, target);

				pt("<a href=\"%s\" class=\"new\" title=\"%s (page does not exist)\">%=%=%=</a>",
						callback.makeUrlMissingTarget(path),
						title,
						n.getPrefix(),
						linkText,
						n.getPostfix());
			}
		}
		else
		{
			if (!target.equals(pageTitle))
			{
				if (n.hasTitle())
				{
					pt("<a href=\"%s\" title=\"%s\">%=%!%=</a>",
							callback.makeUrl(target),
							makeLinkTitle(n, target),
							n.getPrefix(),
							n.getTitle(),
							n.getPostfix());
				}
				else
				{
					pt("<a href=\"%s\" title=\"%s\">%=%=%=</a>",
							callback.makeUrl(target),
							makeLinkTitle(n, target),
							n.getPrefix(),
							makeTitleFromTarget(n, target),
							n.getPostfix());
				}
			}
			else
			{
				if (n.hasTitle())
				{
					pt("<strong class=\"selflink\">%=%!%=</strong>",
							n.getPrefix(),
							n.getTitle(),
							n.getPostfix());
				}
				else
				{
					pt("<strong class=\"selflink\">%=%=%=</strong>",
							n.getPrefix(),
							makeTitleFromTarget(n, target),
							n.getPostfix());
				}
			}
		}
	}

	public void visit(WtItalics n)
	{
		p.indentAtBol("<i>");
		p.incIndent();
		iterate(n);
		p.decIndent();
		p.indentAtBol("</i>");
	}

	@Override
	public void visit(WtLinkOptionAltText n)
	{
		// Should not happen ...
		throw new InternalError();
	}

	@Override
	public void visit(WtLinkOptionGarbage n)
	{
		// Should not happen ...
		throw new InternalError();
	}

	@Override
	public void visit(WtLinkOptionKeyword n)
	{
		// Should not happen ...
		throw new InternalError();
	}

	@Override
	public void visit(WtLinkOptionLinkTarget n)
	{
		// Should not happen ...
		throw new InternalError();
	}

	@Override
	public void visit(WtLinkOptionResize n)
	{
		// Should not happen ...
		throw new InternalError();
	}

	@Override
	public void visit(WtLinkOptions n)
	{
		// Should not happen ...
		throw new InternalError();
	}

	@Override
	public void visit(WtLinkTitle n)
	{
		iterate(n);
	}

	public void visit(WtListItem n)
	{
		p.indentln("<li>");
		p.incIndent();
		iterate(n);
		p.decIndent();
		p.indentln("</li>");
	}

	@Override
	public void visit(WtName n)
	{
		iterate(n);
	}

	public void visit(WtNewline n)
	{
		if (!p.atBol())
			p.print(" ");
	}

	@Override
	public void visit(WtNodeList n)
	{
		iterate(n);
	}

	@Override
	public void visit(WtOnlyInclude n)
	{
		iterate(n);
	}

	public void visit(WtOrderedList n)
	{
		p.indentln("<ol>");
		p.incIndent();
		iterate(n);
		p.decIndent();
		p.indentln("</ol>");
	}

	@Override
	public void visit(WtPageName n)
	{
		// Should not happen ...
		throw new InternalError();
	}

	@Override
	public void visit(WtPageSwitch n)
	{
		// Hide those...
	}

	public void visit(WtParagraph n)
	{
		p.indentln("<p>");
		p.incIndent();
		iterate(n);
		p.decIndent();
		p.indentln("</p>");
	}

	@Override
	public void visit(WtParsedWikitextPage n)
	{
		iterate(n);
	}

	@Override
	public void visit(WtPreproWikitextPage n)
	{
		iterate(n);
	}

	@Override
	public void visit(WtRedirect n)
	{
		// TODO: Implement
		throw new FmtNotYetImplementedError();
	}

	public void visit(WtSection n)
	{
		p.indent();
		pt("<h%d><span class=\"mw-headline\" id=\"%s\">%!</span></h%d>",
				n.getLevel(),
				makeSectionTitle(n.getHeading()),
				n.getHeading(),
				n.getLevel());

		p.println();
		dispatch(n.getBody());
	}

	public void visit(WtSemiPre n)
	{
		p.indent();
		++inPre;
		pt("<pre>%!</pre>", n);
		--inPre;
		p.println();
	}

	public void visit(WtSemiPreLine n)
	{
		iterate(n);
		p.println();
	}

	@Override
	public void visit(WtSignature n)
	{
		// TODO: Implement
		throw new FmtNotYetImplementedError();
	}

	public void visit(WtTable n)
	{
		p.indent();
		pt("<table%!>", cleanAttribs(n.getXmlAttributes()));
		p.println();

		p.incIndent();
		fixTableBody(n.getBody());
		p.decIndent();

		p.indentln("</table>");
	}

	@Override
	public void visit(WtTableCaption n)
	{
		p.indent();
		pt("<caption%!>", cleanAttribs(n.getXmlAttributes()));
		p.println();
		p.incIndent();
		dispatch(getCellContent(n.getBody()));
		p.decIndent();
		p.indentln("</caption>");
	}

	public void visit(WtTableCell n)
	{
		p.indent();
		pt("<td%!>", cleanAttribs(n.getXmlAttributes()));
		p.println();
		p.incIndent();
		dispatch(getCellContent(n.getBody()));
		p.decIndent();
		p.indentln("</td>");
	}

	public void visit(WtTableHeader n)
	{
		p.indent();
		pt("<th%!>", cleanAttribs(n.getXmlAttributes()));
		p.println();
		p.incIndent();
		dispatch(getCellContent(n.getBody()));
		p.decIndent();
		p.indentln("</th>");
	}

	public void visit(WtTableRow n)
	{
		boolean cellsDefined = false;
		for (WtNode cell : n.getBody())
		{
			switch (cell.getNodeType())
			{
				case WtNode.NT_TABLE_CELL:
				case WtNode.NT_TABLE_HEADER:
					cellsDefined = true;
					break;
			}
		}

		if (cellsDefined)
		{
			p.indent();
			pt("<tr%!>", cleanAttribs(n.getXmlAttributes()));
			p.println();
			p.incIndent();
			dispatch(getCellContent(n.getBody()));
			p.decIndent();
			p.indentln("</tr>");
		}
		else
		{
			iterate(n.getBody());
		}
	}

	public void visit(WtTableImplicitTableBody n)
	{
		iterate(n.getBody());
	}

	public void visit(WtTagExtension n)
	{
		// TODO: Should not get skipped!
		if (n.getName().trim().equalsIgnoreCase("ref"))
			return;
		if (n.getName().trim().equalsIgnoreCase("references"))
			return;

		printAsWikitext(n);

		/*
		pc("&lt;%s%!&gt;%=&lt;/%s&gt;",
				n.getName(),
				n.getXmlAttributes(),
				n.getBody().getContent(),
				n.getName());
		*/
	}

	@Override
	public void visit(WtTagExtensionBody n)
	{
		// Should not happen ...
		throw new InternalError();
	}

	@Override
	public void visit(WtTemplate n)
	{
		printAsWikitext(n);
	}

	@Override
	public void visit(WtTemplateArgument n)
	{
		printAsWikitext(n);
	}

	@Override
	public void visit(WtTemplateArguments n)
	{
		printAsWikitext(n);
	}

	@Override
	public void visit(WtTemplateParameter n)
	{
		printAsWikitext(n);
	}

	public void visit(WtText n)
	{
		wrapText(n.getContent());
	}

	@Override
	public void visit(WtTicks n)
	{
		// Should not happen ...
		throw new InternalError();
	}

	public void visit(WtUnorderedList n)
	{
		p.indentln("<ul>");
		p.incIndent();
		iterate(n);
		p.decIndent();
		p.indentln("</ul>");
	}

	public void visit(WtUrl n)
	{
		p.indentAtBol();

		String url = callback.makeUrl(n);
		pf("<a href=\"%s\">%s</a>", url, url);
	}

	@Override
	public void visit(WtValue n)
	{
		iterate(n);
	}

	@Override
	public void visit(WtWhitespace n)
	{
		if (!p.atBol())
			p.println(" ");
	}

	public void visit(WtXmlAttribute n)
	{
		if (!n.getName().isResolved())
		{
			logger.warn("Unresolved attribute name: " + WtRtDataPrinter.print(n));
		}
		else
		{
			if (n.hasValue())
			{
				pt(" %s=\"%~\"", n.getName().getAsString(), cleanAttribValue(n.getValue()));
			}
			else
			{
				pf(" %s=\"%<s\"", n.getName().getAsString());
			}
		}
	}

	public void visit(WtXmlAttributeGarbage n)
	{
		logger.warn("Attribute garbage: " + WtRtDataPrinter.print(n));
	}

	@Override
	public void visit(WtXmlAttributes n)
	{
		for (WtNode n1 : n)
		{
			switch (n1.getNodeType())
			{
				case WtNode.NT_XML_ATTRIBUTE:
				case WtNode.NT_XML_ATTRIBUTE_GARBAGE:
					dispatch(n1);
					break;
				default:
					logger.warn("Non-attribute node in attributes collection: " + WtRtDataPrinter.print(n));
					break;
			}
		}
	}

	public void visit(WtXmlCharRef n)
	{
		p.indentAtBol();
		pf("&#%d;", n.getCodePoint());
	}

	@Override
	public void visit(WtXmlComment n)
	{
		// Hide those...
	}

	public void visit(WtXmlElement n)
	{
		if (n.hasBody())
		{
			if (blockElements.contains(n.getName().toLowerCase()))
			{
				p.indent();
				pt("<%s%!>", n.getName(), cleanAttribs(n.getXmlAttributes()));
				p.println();
				p.incIndent();
				dispatch(n.getBody());
				p.decIndent();
				p.indent();
				pf("</%s>", n.getName());
				p.println();
			}
			else
			{
				p.indentAtBol();
				pt("<%s%!>", n.getName(), cleanAttribs(n.getXmlAttributes()));
				p.incIndent();
				dispatch(n.getBody());
				p.decIndent();
				p.indentAtBol();
				pf("</%s>", n.getName());
			}
		}
		else
		{
			p.indentAtBol();
			pt("<%s%! />", n.getName(), cleanAttribs(n.getXmlAttributes()));
		}
	}

	public void visit(WtXmlEmptyTag n)
	{
		printAsWikitext(n);
	}

	public void visit(WtXmlEndTag n)
	{
		printAsWikitext(n);
	}

	public void visit(WtXmlEntityRef n)
	{
		p.indentAtBol();
		pf("&%s;", n.getName());
	}

	public void visit(WtXmlStartTag n)
	{
		printAsWikitext(n);
	}

	// =====================================================================

	private void wrapText(String text)
	{
		if (inPre > 0)
		{
			p.print(esc(text));
		}
		else
		{
			p.indentAtBol(esc(StringTools.collapseWhitespace(text)));
		}
	}

	/*
	private void wrapText(String text)
	{
		if (inPre > 0)
		{
			p.print(esc(text));
		}
		else
		{
			
			int i = 0;
			int len = text.length();
			
			while (i < len)
			{
				char ch;
				
				// If at beginning of line skip whitespace
				if (p.atBol())
				{
					while (i < len)
					{
						ch = text.charAt(i);
						if (!Character.isWhitespace(ch))
							break;
						++i;
					}
				}
				
				if (i >= len)
					break;
				
				p.flush();
				int col = p.getColumn();
				int border = 80 + p.getIndent() * 4;
				
				int j = i;
				while (j < len)
				{
					ch = text.charAt(j++);
					if (col >= border && Character.isWhitespace(ch))
						break;
					if (ch == '\n')
						break;
				}
				
				String substr = text.substring(i, j);
				if (!substr.isEmpty())
					p.indentAtBol(esc(StringTools.collapseWhitespace(substr)));
				
				if (i < len)
					p.println();
				
				i = j;
			}
		}
	}
	*/

	private void printAsWikitext(WtNode n)
	{
		// TODO: Implement
		//throw new FmtNotYetImplementedError();
		//p.indentAtBol();
	}

	private String toWikitext(WtNode value)
	{
		// TODO: Implement
		//throw new FmtNotYetImplementedError();
		return "";
	}

	// =====================================================================

	private String makeSectionTitle(WtHeading n)
	{
		byte[] title;
		try
		{
			title = makeTitleFromNodes(n).getBytes("UTF8");
		}
		catch (UnsupportedEncodingException e)
		{
			throw new VisitingException(e);
		}

		StringBuilder b = new StringBuilder();
		for (byte u : title)
		{
			if (u < 0)
			{
				b.append('.');
				b.append(String.format("%02X", u));
			}
			else if (u == ' ')
			{
				b.append('_');
			}
			else
			{
				b.append((char) u);
			}
		}

		return b.toString();
	}

	private String makeImageAltText(WtImageLink n)
	{
		return makeTitleFromNodes(n.getAlt());
	}

	protected String makeImageCaption(WtImageLink n)
	{
		return makeTitleFromNodes(n.getTitle());
	}

	private String makeTitleFromNodes(WtNodeList titleNode)
	{
		StringWriter w = new StringWriter();
		SafeLinkTitlePrinter p = new SafeLinkTitlePrinter(w, wikiConfig);
		p.go(titleNode);
		return w.toString();
	}

	// =====================================================================

	static String makeLinkTitle(WtInternalLink n, PageTitle target)
	{
		return target.getDenormalizedFullTitle();
	}

	protected String makeImageTitle(WtImageLink n, PageTitle target)
	{
		return target.getDenormalizedFullTitle();
	}

	private String makeTitleFromTarget(WtInternalLink n, PageTitle target)
	{
		return makeTitleFromTarget(target, n.getTarget());
	}

	private String makeTitleFromTarget(PageTitle target, WtPageName title)
	{
		String targetStr = title.getAsString();
		if (target.hasInitialColon() && !targetStr.isEmpty() && targetStr.charAt(0) == ':')
			targetStr = targetStr.substring(1);
		return targetStr;
	}

	// =====================================================================

	/**
	 * Pull garbage in between rows in front of the table.
	 */
	private void fixTableBody(WtNodeList body)
	{
		boolean hadRow = false;
		WtTableRow implicitRow = null;
		for (WtNode c : body)
		{
			switch (c.getNodeType())
			{
				case WtNode.NT_TABLE_HEADER: // fall through!
				case WtNode.NT_TABLE_CELL:
				{
					if (hadRow)
					{
						dispatch(c);
					}
					else
					{
						if (implicitRow == null)
							implicitRow = nf.tr(nf.emptyAttrs(), nf.body(nf.list()));
						implicitRow.getBody().add(c);
					}
					break;
				}

				case WtNode.NT_TABLE_CAPTION:
				{
					if (!hadRow && implicitRow != null)
						dispatch(implicitRow);
					implicitRow = null;
					dispatch(c);
					break;
				}

				case WtNode.NT_TABLE_ROW:
				{
					if (!hadRow && implicitRow != null)
						dispatch(implicitRow);
					hadRow = true;
					dispatch(c);
					break;
				}

				default:
				{
					if (!hadRow && implicitRow != null)
						implicitRow.getBody().add(c);
					else
						dispatch(c);
					break;
				}
			}
		}
	}

	/**
	 * If the cell content is only one paragraph, the content of the paragraph
	 * is returned. Otherwise the whole cell content is returned. This is done
	 * to render cells with a single paragraph without the paragraph tags.
	 */
	protected static WtNode getCellContent(WtNodeList body)
	{
		if (body.size() >= 1 && body.get(0) instanceof WtParagraph)
		{
			boolean ok = true;
			for (int i = 1; i < body.size(); ++i)
			{
				if (!(body.get(i) instanceof WtNewline))
				{
					ok = false;
					break;
				}
			}

			if (ok)
				body = (WtParagraph) body.get(0);
		}

		return body;
	}

	// =====================================================================

	protected String cleanAttribValue(WtNodeList value)
	{
		try
		{
			return StringTools.collapseWhitespace(tu.astToText(value)).trim();
		}
		catch (StringConversionException e)
		{
			return toWikitext(value);
		}
	}

	protected WtNodeList cleanAttribs(WtNodeList xmlAttributes)
	{
		ArrayList<WtXmlAttribute> clean = null;

		WtXmlAttribute style = null;
		for (WtNode a : xmlAttributes)
		{
			if (a instanceof WtXmlAttribute)
			{
				WtXmlAttribute attr = (WtXmlAttribute) a;
				if (!attr.getName().isResolved())
					continue;

				String name = attr.getName().getAsString().toLowerCase();
				if (name.equals("style"))
				{
					style = attr;
				}
				else if (name.equals("width"))
				{
					if (clean == null)
						clean = new ArrayList<WtXmlAttribute>();
					clean.add(attr);
				}
				else if (name.equals("align"))
				{
					if (clean == null)
						clean = new ArrayList<WtXmlAttribute>();
					clean.add(attr);
				}
			}
		}

		if (clean == null || clean.isEmpty())
			return xmlAttributes;

		String newStyle = "";
		if (style != null)
			newStyle = cleanAttribValue(style.getValue());

		for (WtXmlAttribute a : clean)
		{
			if (!a.getName().isResolved())
				continue;

			String name = a.getName().getAsString().toLowerCase();
			if (name.equals("align"))
			{
				newStyle = String.format(
						"text-align: %s; ",
						cleanAttribValue(a.getValue())) + newStyle;
			}
			else
			{
				newStyle = String.format(
						"%s: %s; ",
						name,
						cleanAttribValue(a.getValue())) + newStyle;
			}
		}

		WtXmlAttribute newStyleAttrib = nf.attr(
				nf.name(nf.list(nf.text("style"))),
				nf.value(nf.list(nf.text(newStyle))));

		WtNodeList newAttribs = nf.attrs(nf.list());
		for (WtNode a : xmlAttributes)
		{
			if (a == style)
			{
				newAttribs.add(newStyleAttrib);
			}
			else if (clean.contains(a))
			{
				// Remove
			}
			else
			{
				// Copy the rest
				newAttribs.add(a);
			}
		}

		if (style == null)
			newAttribs.add(newStyleAttrib);

		return newAttribs;
	}

	// =========================================================================

	public static <T extends WtNode> String print(
			HtmlRendererCallback callback,
			WikiConfig wikiConfig,
			PageTitle pageTitle,
			T node)
	{
		return print(callback, wikiConfig, new StringWriter(), pageTitle, node).toString();
	}

	public static <T extends WtNode> Writer print(
			HtmlRendererCallback callback,
			WikiConfig wikiConfig,
			Writer writer,
			PageTitle pageTitle,
			T node)
	{
		new HtmlRenderer(callback, wikiConfig, pageTitle, writer).go(node);
		return writer;
	}

	// =========================================================================

	protected static final Logger logger = LoggerFactory.getLogger(HtmlRenderer.class);

	protected static final Set<String> blockElements = new HashSet<String>();

	protected final WikiConfig wikiConfig;

	protected final PageTitle pageTitle;

	protected final EngineNodeFactory nf;

	protected final EngineAstTextUtils tu;

	protected final HtmlRendererCallback callback;

	protected int inPre = 0;

	static
	{
		// left out del and ins, added table elements
		blockElements.add("div");
		blockElements.add("address");
		blockElements.add("blockquote");
		blockElements.add("center");
		blockElements.add("dir");
		blockElements.add("div");
		blockElements.add("dl");
		blockElements.add("fieldset");
		blockElements.add("form");
		blockElements.add("h1");
		blockElements.add("h2");
		blockElements.add("h3");
		blockElements.add("h4");
		blockElements.add("h5");
		blockElements.add("h6");
		blockElements.add("hr");
		blockElements.add("isindex");
		blockElements.add("menu");
		blockElements.add("noframes");
		blockElements.add("noscript");
		blockElements.add("ol");
		blockElements.add("p");
		blockElements.add("pre");
		blockElements.add("table");
		blockElements.add("ul");
		blockElements.add("center");
		blockElements.add("caption");
		blockElements.add("tr");
		blockElements.add("td");
		blockElements.add("th");
		blockElements.add("colgroup");
		blockElements.add("thead");
		blockElements.add("tbody");
		blockElements.add("tfoot");
	}

	// =========================================================================

	protected HtmlRenderer(
			HtmlRendererCallback callback,
			WikiConfig wikiConfig,
			PageTitle pageTitle,
			Writer w)
	{
		super(w);
		this.callback = callback;
		this.wikiConfig = wikiConfig;
		this.pageTitle = pageTitle;
		this.nf = wikiConfig.getNodeFactory();
		this.tu = wikiConfig.getAstTextUtils();
	}
}
