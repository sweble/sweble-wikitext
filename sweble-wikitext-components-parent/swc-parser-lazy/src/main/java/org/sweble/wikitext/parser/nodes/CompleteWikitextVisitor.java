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

public interface CompleteWikitextVisitor<T>
{
	// WtInnerNode1

	public T visit(WtLinkOptionLinkTarget n);

	public T visit(WtRedirect n);

	public T visit(WtTableImplicitTableBody n);

	public T visit(WtXmlAttribute n);

	public T visit(WtXmlEmptyTag n);

	public T visit(WtXmlStartTag n);

	public T visit(WtImStartTag n);

	// WtInnerNode2

	public T visit(WtExternalLink n);

	public T visit(WtInternalLink n);

	public T visit(WtSection n);

	public T visit(WtTable n);

	public T visit(WtTableCaption n);

	public T visit(WtTableCell n);

	public T visit(WtTableHeader n);

	public T visit(WtTableRow n);

	public T visit(WtTagExtension n);

	public T visit(WtTemplate n);

	public T visit(WtTemplateArgument n);

	public T visit(WtXmlElement n);

	// WtInnerNode3

	public T visit(WtImageLink n);

	public T visit(WtTemplateParameter n);

	// WtLeafNode

	public T visit(WtHorizontalRule n);

	public T visit(WtIllegalCodePoint n);

	public T visit(WtLinkOptionKeyword n);

	public T visit(WtLinkOptionResize n);

	public T visit(WtPageSwitch n);

	public T visit(WtSignature n);

	public T visit(WtTicks n);

	public T visit(WtUrl n);

	public T visit(WtXmlCharRef n);

	public T visit(WtXmlEndTag n);

	public T visit(WtImEndTag n);

	public T visit(WtXmlEntityRef n);

	// WtNodeList

	public T visit(WtNodeList n);

	// WtContentNode

	public T visit(WtBody n);

	public T visit(WtBold n);

	public T visit(WtDefinitionList n);

	public T visit(WtDefinitionListDef n);

	public T visit(WtDefinitionListTerm n);

	public T visit(WtHeading n);

	public T visit(WtItalics n);

	public T visit(WtLinkOptionAltText n);

	public T visit(WtLinkOptions n);

	public T visit(WtLinkTitle n);

	public T visit(WtListItem n);

	public T visit(WtName n);

	public T visit(WtOnlyInclude n);

	public T visit(WtOrderedList n);

	public T visit(WtParsedWikitextPage n);

	public T visit(WtPreproWikitextPage n);

	public T visit(WtParagraph n);

	public T visit(WtSemiPre n);

	public T visit(WtSemiPreLine n);

	public T visit(WtTemplateArguments n);

	public T visit(WtUnorderedList n);

	public T visit(WtValue n);

	public T visit(WtWhitespace n);

	public T visit(WtXmlAttributes n);

	// WtText

	public T visit(WtText n);

	// WtStringNode

	public T visit(WtIgnored n);

	public T visit(WtLinkOptionGarbage n);

	public T visit(WtNewline n);

	public T visit(WtPageName n);

	public T visit(WtTagExtensionBody n);

	public T visit(WtXmlAttributeGarbage n);

	public T visit(WtXmlComment n);
}
