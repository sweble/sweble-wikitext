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

package org.sweble.wikitext.parser.nodes;

public interface CompleteWikitextVisitorNoReturn
{
	// WtInnerNode1

	public void visit(WtLinkOptionLinkTarget n);

	public void visit(WtRedirect n);

	public void visit(WtTableImplicitTableBody n);

	public void visit(WtXmlAttribute n);

	public void visit(WtXmlEmptyTag n);

	public void visit(WtXmlStartTag n);

	public void visit(WtImStartTag n);

	// WtInnerNode2

	public void visit(WtExternalLink n);

	public void visit(WtInternalLink n);

	public void visit(WtSection n);

	public void visit(WtTable n);

	public void visit(WtTableCaption n);

	public void visit(WtTableCell n);

	public void visit(WtTableHeader n);

	public void visit(WtTableRow n);

	public void visit(WtTagExtension n);

	public void visit(WtTemplate n);

	public void visit(WtTemplateArgument n);

	public void visit(WtXmlElement n);

	// WtInnerNode3

	public void visit(WtImageLink n);

	public void visit(WtTemplateParameter n);

	// WtLeafNode

	public void visit(WtHorizontalRule n);

	public void visit(WtIllegalCodePoint n);

	public void visit(WtLinkOptionKeyword n);

	public void visit(WtLinkOptionResize n);

	public void visit(WtPageSwitch n);

	public void visit(WtSignature n);

	public void visit(WtTicks n);

	public void visit(WtUrl n);

	public void visit(WtXmlCharRef n);

	public void visit(WtXmlEndTag n);

	public void visit(WtImEndTag n);

	public void visit(WtXmlEntityRef n);

	// WtNodeList

	public void visit(WtNodeList n);

	// WtContentNode

	public void visit(WtBody n);

	public void visit(WtBold n);

	public void visit(WtDefinitionList n);

	public void visit(WtDefinitionListDef n);

	public void visit(WtDefinitionListTerm n);

	public void visit(WtHeading n);

	public void visit(WtItalics n);

	public void visit(WtLinkOptionAltText n);

	public void visit(WtLinkOptions n);

	public void visit(WtLinkTitle n);

	public void visit(WtListItem n);

	public void visit(WtName n);

	public void visit(WtOnlyInclude n);

	public void visit(WtOrderedList n);

	public void visit(WtParsedWikitextPage n);

	public void visit(WtPreproWikitextPage n);

	public void visit(WtParagraph n);

	public void visit(WtSemiPre n);

	public void visit(WtSemiPreLine n);

	public void visit(WtTemplateArguments n);

	public void visit(WtUnorderedList n);

	public void visit(WtValue n);

	public void visit(WtWhitespace n);

	public void visit(WtXmlAttributes n);

	// WtText

	public void visit(WtText n);

	// WtStringNode

	public void visit(WtIgnored n);

	public void visit(WtLinkOptionGarbage n);

	public void visit(WtNewline n);

	public void visit(WtPageName n);

	public void visit(WtTagExtensionBody n);

	public void visit(WtXmlAttributeGarbage n);

	public void visit(WtXmlComment n);
}
