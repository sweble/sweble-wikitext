package org.sweble.wikitext.engine;

import org.sweble.wikitext.engine.nodes.CompleteEngineVisitor;
import org.sweble.wikitext.engine.nodes.EngCompiledPage;
import org.sweble.wikitext.engine.nodes.EngNowiki;
import org.sweble.wikitext.engine.nodes.EngPage;
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
import org.sweble.wom.WomNode;
import org.sweble.wom.impl.types.CommentImpl;
import org.sweble.wom.impl.types.PageImpl;
import org.sweble.wom.impl.types.TextImpl;
import org.sweble.wom.impl.types.ValueImpl;

import de.fau.cs.osr.ptk.common.AstVisitor;

public class AstToWomVisitor
		extends
			AstVisitor<WtNode>
		implements
			CompleteEngineVisitor<WomNode>
{
	private final PageTitle pageTitle;
	
	// =========================================================================
	
	public AstToWomVisitor(PageTitle pageTitle)
	{
		this.pageTitle = pageTitle;
	}
	
	// =========================================================================
	
	@Override
	public WomNode visit(WtLinkOptionLinkTarget n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtRedirect n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTableImplicitTableBody n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtXmlAttribute n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtXmlEmptyTag n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtXmlStartTag n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtImStartTag n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtExternalLink n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtInternalLink n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtSection n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTable n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTableCaption n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTableCell n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTableHeader n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTableRow n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTagExtension n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTemplate n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTemplateArgument n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtXmlElement n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtImageLink n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTemplateParameter n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtHorizontalRule n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtIllegalCodePoint n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtLinkOptionKeyword n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtLinkOptionResize n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtPageSwitch n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtSignature n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTicks n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtUrl n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtXmlCharRef n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtXmlEndTag n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtImEndTag n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtXmlEntityRef n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtNodeList n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtBody n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtBold n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtDefinitionList n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtDefinitionListDef n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtDefinitionListTerm n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtHeading n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtItalics n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtLinkOptionAltText n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtLinkOptions n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtLinkTitle n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtListItem n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtName n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtOnlyInclude n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtOrderedList n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtParsedWikitextPage n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtPreproWikitextPage n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtParagraph n)
	{
		ValueImpl p = new ValueImpl();
		for (WtNode c : n)
			p.appendChild((WomNode) dispatch(c));
		return p; 
		/*
		// TODO Auto-generated method stub
		return nyi();
		*/
	}
	
	@Override
	public WomNode visit(WtSemiPre n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtSemiPreLine n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTemplateArguments n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtUnorderedList n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtValue n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtWhitespace n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtXmlAttributes n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtText n)
	{
		return new TextImpl(n.getContent());
	}
	
	@Override
	public WomNode visit(WtIgnored n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtLinkOptionGarbage n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtNewline n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtPageName n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTagExtensionBody n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtXmlAttributeGarbage n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtXmlComment n)
	{
		return new CommentImpl(n.getContent());
	}
	
	@Override
	public WomNode visit(EngCompiledPage n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(EngNowiki n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(EngPage n)
	{
		PageImpl page = new PageImpl(
				pageTitle.getNamespaceAlias(),
				null,
				pageTitle.getDenormalizedFullTitle());
		
		for (WtNode c : n)
			page.getBody().appendChild((WomNode) dispatch(c));
		
		return page;
	}
	
	@Override
	public WomNode visit(EngSoftErrorNode n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	private WomNode nyi()
	{
		throw new UnsupportedOperationException("not yet implemented");
	}
}
