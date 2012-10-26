package org.sweble.wikitext.parser.nodes;

public interface CompleteWikitextVisitor
{
	// WtInnerNode1
	
	public Object visit(WtLinkOptionLinkTarget n);
	
	public Object visit(WtRedirect n);
	
	public Object visit(WtTableImplicitTableBody n);
	
	public Object visit(WtXmlAttribute n);
	
	public Object visit(WtXmlEmptyTag n);
	
	public Object visit(WtXmlStartTag n);
	
	public Object visit(WtImStartTag n);
	
	// WtInnerNode2
	
	public Object visit(WtExternalLink n);
	
	public Object visit(WtInternalLink n);
	
	public Object visit(WtSection n);
	
	public Object visit(WtTable n);
	
	public Object visit(WtTableCaption n);
	
	public Object visit(WtTableCell n);
	
	public Object visit(WtTableHeader n);
	
	public Object visit(WtTableRow n);
	
	public Object visit(WtTagExtension n);
	
	public Object visit(WtTemplate n);
	
	public Object visit(WtTemplateArgument n);
	
	public Object visit(WtXmlElement n);
	
	// WtInnerNode3
	
	public Object visit(WtImageLink n);
	
	public Object visit(WtTemplateParameter n);
	
	// WtLeafNode
	
	public Object visit(WtHorizontalRule n);
	
	public Object visit(WtIllegalCodePoint n);
	
	public Object visit(WtLinkOptionKeyword n);
	
	public Object visit(WtLinkOptionResize n);
	
	public Object visit(WtPageSwitch n);
	
	public Object visit(WtSignature n);
	
	public Object visit(WtTicks n);
	
	public Object visit(WtUrl n);
	
	public Object visit(WtXmlCharRef n);
	
	public Object visit(WtXmlEndTag n);
	
	public Object visit(WtImEndTag n);
	
	public Object visit(WtXmlEntityRef n);
	
	// WtNodeList
	
	public Object visit(WtNodeList n);
	
	// WtContentNode
	
	public Object visit(WtBody n);
	
	public Object visit(WtBold n);
	
	public Object visit(WtDefinitionList n);
	
	public Object visit(WtDefinitionListDef n);
	
	public Object visit(WtDefinitionListTerm n);
	
	public Object visit(WtHeading n);
	
	public Object visit(WtItalics n);
	
	public Object visit(WtLinkOptionAltText n);
	
	public Object visit(WtLinkOptions n);
	
	public Object visit(WtLinkTitle n);
	
	public Object visit(WtListItem n);
	
	public Object visit(WtName n);
	
	public Object visit(WtOnlyInclude n);
	
	public Object visit(WtOrderedList n);
	
	public Object visit(WtParsedWikitextPage n);
	
	public Object visit(WtPreproWikitextPage n);
	
	public Object visit(WtParagraph n);
	
	public Object visit(WtSemiPre n);
	
	public Object visit(WtSemiPreLine n);
	
	public Object visit(WtTemplateArguments n);
	
	public Object visit(WtUnorderedList n);
	
	public Object visit(WtValue n);
	
	public Object visit(WtWhitespace n);
	
	public Object visit(WtXmlAttributes n);
	
	// WtText
	
	public Object visit(WtText n);
	
	// WtStringNode
	
	public Object visit(WtIgnored n);
	
	public Object visit(WtLinkOptionGarbage n);
	
	public Object visit(WtNewline n);
	
	public Object visit(WtPageName n);
	
	public Object visit(WtTagExtensionBody n);
	
	public Object visit(WtXmlAttributeGarbage n);
	
	public Object visit(WtXmlComment n);
}
