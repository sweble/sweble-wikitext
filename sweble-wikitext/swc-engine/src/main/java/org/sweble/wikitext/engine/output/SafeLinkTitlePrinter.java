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

import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.CompleteEngineVisitorNoReturn;
import org.sweble.wikitext.engine.nodes.EngNowiki;
import org.sweble.wikitext.engine.nodes.EngPage;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.nodes.EngSoftErrorNode;
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
import org.sweble.wikitext.parser.utils.WtRtDataPrinter;

import de.fau.cs.osr.utils.FmtNotYetImplementedError;
import de.fau.cs.osr.utils.visitor.VisitingException;

/**
 * Print the title of an internal link but strip out stuff that doesn't belong
 * in an XML attribute.
 * 
 * This is used for example to generate the alt text or title attribute for an
 * image.
 */
public class SafeLinkTitlePrinter
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
		p.print(esc(n.getContent(), true));
	}

	@Override
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
		iterate(n);
	}

	@Override
	public void visit(WtDefinitionList n)
	{
		iterate(n);
	}

	@Override
	public void visit(WtDefinitionListDef n)
	{
		iterate(n);
	}

	@Override
	public void visit(WtDefinitionListTerm n)
	{
		iterate(n);
	}

	@Override
	public void visit(WtExternalLink n)
	{
		// TODO: Implement
		throw new FmtNotYetImplementedError();
	}

	@Override
	public void visit(WtHeading n)
	{
		iterate(n);
	}

	@Override
	public void visit(WtHorizontalRule n)
	{
		// Wouldn't know what to render here...
	}

	@Override
	public void visit(WtIgnored n)
	{
		// Well, ignore it...
	}

	@Override
	public void visit(WtIllegalCodePoint n)
	{
		final String cp = n.getCodePoint();
		for (int i = 0; i < cp.length(); ++i)
			pf("&amp;#%d;", (int) cp.charAt(i));

	}

	@Override
	public void visit(WtImageLink n)
	{
		// TODO: Implement
		throw new FmtNotYetImplementedError();
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
		if (n.hasTitle())
		{
			dispatch(n.getTitle());
		}
		else
		{
			String linkTarget = n.getTarget().getAsString();
			PageTitle target;
			try
			{
				target = PageTitle.make(wikiConfig, linkTarget);
			}
			catch (LinkTargetException e)
			{
				throw new VisitingException(e);
			}
			p.print(esc(HtmlRenderer.makeLinkTitle(n, target), true));
		}
	}

	public void visit(WtItalics n)
	{
		iterate(n);
	}

	@Override
	public void visit(WtLinkOptionAltText n)
	{
		iterate(n);
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

	public void visit(WtLinkTitle n)
	{
		iterate(n);
	}

	@Override
	public void visit(WtListItem n)
	{
		iterate(n);
	}

	@Override
	public void visit(WtName n)
	{
		iterate(n);
	}

	public void visit(WtNewline n)
	{
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

	@Override
	public void visit(WtOrderedList n)
	{
		iterate(n);
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

	@Override
	public void visit(WtParagraph n)
	{
		iterate(n);
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

	@Override
	public void visit(WtSection n)
	{
		dispatch(n.getHeading());
		dispatch(n.getBody());
	}

	@Override
	public void visit(WtSemiPre n)
	{
		iterate(n);
	}

	@Override
	public void visit(WtSemiPreLine n)
	{
		iterate(n);
	}

	@Override
	public void visit(WtSignature n)
	{
		// TODO: Implement
		throw new FmtNotYetImplementedError();
	}

	@Override
	public void visit(WtTable n)
	{
		// TODO: Implement
		throw new FmtNotYetImplementedError();
	}

	@Override
	public void visit(WtTableCaption n)
	{
		// TODO: Implement
		throw new FmtNotYetImplementedError();
	}

	@Override
	public void visit(WtTableCell n)
	{
		// TODO: Implement
		throw new FmtNotYetImplementedError();
	}

	@Override
	public void visit(WtTableHeader n)
	{
		// TODO: Implement
		throw new FmtNotYetImplementedError();
	}

	@Override
	public void visit(WtTableRow n)
	{
		// TODO: Implement
		throw new FmtNotYetImplementedError();
	}

	@Override
	public void visit(WtTableImplicitTableBody n)
	{
		iterate(n.getBody());
	}

	public void visit(WtTagExtension n)
	{
		printAsWikitext(n);
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
		p.print(esc(n.getContent(), true));
	}

	@Override
	public void visit(WtTicks n)
	{
		// Should not happen ...
		throw new InternalError();
	}

	@Override
	public void visit(WtUnorderedList n)
	{
		iterate(n);
	}

	@Override
	public void visit(WtUrl n)
	{
		// TODO: Implement
		throw new FmtNotYetImplementedError();
	}

	@Override
	public void visit(WtValue n)
	{
		iterate(n);
	}

	@Override
	public void visit(WtWhitespace n)
	{
		iterate(n);
	}

	@Override
	public void visit(WtXmlAttribute n)
	{
		// TODO: Implement
		throw new FmtNotYetImplementedError();
	}

	@Override
	public void visit(WtXmlAttributeGarbage n)
	{
		logger.warn("Attribute garbage: " + WtRtDataPrinter.print(n));
	}

	@Override
	public void visit(WtXmlAttributes n)
	{
		iterate(n);
	}

	public void visit(WtXmlCharRef n)
	{
		pf("&#%d;", n.getCodePoint());
	}

	@Override
	public void visit(WtXmlComment n)
	{
		// Hide those...
	}

	public void visit(WtXmlElement n)
	{
		iterate(n.getBody());
	}

	@Override
	public void visit(WtXmlEmptyTag n)
	{
		printAsWikitext(n);
	}

	@Override
	public void visit(WtXmlEndTag n)
	{
		printAsWikitext(n);
	}

	public void visit(WtXmlEntityRef n)
	{
		pf("&%s;", n.getName());
	}

	@Override
	public void visit(WtXmlStartTag n)
	{
		printAsWikitext(n);
	}

	// =========================================================================

	private void printAsWikitext(WtNode n)
	{
		p.indentAtBol(esc(WtRtDataPrinter.print(n)));
	}

	// =====================================================================

	private static final Logger logger = LoggerFactory.getLogger(HtmlRenderer.class);

	private final WikiConfig wikiConfig;

	// =========================================================================

	public SafeLinkTitlePrinter(Writer writer, WikiConfig wikiConfig)
	{
		super(writer);
		this.wikiConfig = wikiConfig;
	}
}
