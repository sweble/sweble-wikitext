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
package org.sweble.wikitext.engine.astdom;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.sweble.wikitext.engine.Page;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration;
import org.sweble.wikitext.engine.wom.WomAttribute;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.lazy.ParserConfigInterface;
import org.sweble.wikitext.lazy.parser.Bold;
import org.sweble.wikitext.lazy.parser.Enumeration;
import org.sweble.wikitext.lazy.parser.EnumerationItem;
import org.sweble.wikitext.lazy.parser.HorizontalRule;
import org.sweble.wikitext.lazy.parser.Italics;
import org.sweble.wikitext.lazy.parser.Itemization;
import org.sweble.wikitext.lazy.parser.ItemizationItem;
import org.sweble.wikitext.lazy.parser.LazyParsedPage;
import org.sweble.wikitext.lazy.parser.Paragraph;
import org.sweble.wikitext.lazy.parser.Whitespace;
import org.sweble.wikitext.lazy.parser.XmlElement;
import org.sweble.wikitext.lazy.preprocessor.LazyPreprocessedPage;
import org.sweble.wikitext.lazy.preprocessor.XmlComment;
import org.sweble.wikitext.lazy.utils.XmlAttribute;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.ContentNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;
import de.fau.cs.osr.utils.FmtNotYetImplementedError;

public class AstToDomConverter
        extends
            AstVisitor
{
	private static final HashMap<String, XmlElementFactoryEnum> xmlElements;
	
	private final ParserConfigInterface config;
	
	private final String title;
	
	private final String path;
	
	private final String namespace;
	
	static
	{
		xmlElements = new HashMap<String, XmlElementFactoryEnum>();
		for (XmlElementFactoryEnum e : XmlElementFactoryEnum.values())
			xmlElements.put(e.toString().toLowerCase(), e);
	}
	
	// =========================================================================
	
	public AstToDomConverter(ParserConfigInterface config, String namespace, String path, String title)
	{
		super();
		this.config = config;
		this.namespace = namespace;
		this.path = path;
		this.title = title;
	}
	
	public static WomNode convert(SimpleWikiConfiguration config, PageTitle title, Page page)
	{
		String namespace = title.getNamespace().getCanonical();
		if (namespace.isEmpty())
			namespace = null;
		
		final AstToDomConverter converter = new AstToDomConverter(
		        config,
		        namespace,
		        null,
		        title.getTitle());
		
		return (WomNode) converter.go(page);
	}
	
	// =========================================================================
	
	public WomNode visit(LazyPreprocessedPage p)
	{
		return addContent(p, new Document(p, namespace, path, title));
	}
	
	public WomNode visit(LazyParsedPage p)
	{
		return addContent(p, new Document(p, namespace, path, title));
	}
	
	public WomNode visit(Page p)
	{
		return addContent(p, new Document(p, namespace, path, title));
	}
	
	public WomNode visit(Paragraph p)
	{
		return addContent(p, new ParagraphAdapter(p));
	}
	
	public WomNode visit(Text t)
	{
		return new TextAdapter(t);
	}
	
	public WomNode visit(Whitespace t)
	{
		return new WhitespaceAdapter(t);
	}
	
	public WomNode visit(XmlComment c)
	{
		return new XmlCommentAdapter(c);
	}
	
	public WomAttribute visit(XmlAttribute a)
	{
		return new XmlAttributeAdapter(config, a);
	}
	
	public WomNode visit(Bold bold)
	{
		return addContent(bold, new BoldAdapter(bold));
	}
	
	public WomNode visit(Italics italics)
	{
		return addContent(italics, new ItalicsAdapter(italics));
	}
	
	public WomNode visit(Enumeration e)
	{
		return addContent(e, new EnumerationAdapter(e));
	}
	
	public WomNode visit(EnumerationItem i)
	{
		return addContent(i, new EnumerationItemAdapter(i));
	}
	
	public WomNode visit(Itemization e)
	{
		return addContent(e, new ItemizationAdapter(e));
	}
	
	public WomNode visit(ItemizationItem i)
	{
		return addContent(i, new ItemizationItemAdapter(i));
	}
	
	public WomNode visit(HorizontalRule hr)
	{
		return new HorizontalRuleAdapter(hr);
	}
	
	public WomNode visit(XmlElement e)
	{
		XmlElementFactoryEnum f = xmlElements.get(e.getName().toLowerCase());
		if (f == null)
			throw new FmtNotYetImplementedError(
			        "Unmapable xml elements are not yet supported");
		
		WomNode result = f.create(e);
		
		for (WomAttribute attr : mapDropNull(e.getXmlAttributes(), WomAttribute.class))
			result.setAttributeNode(attr);
		
		for (WomNode child : mapDropNull(e.getBody()))
			result.appendChild(child);
		
		return result;
	}
	
	// =========================================================================
	
	private WomNode addContent(ContentNode node, WomNode result)
	{
		for (WomNode child : mapDropNull(node.getContent()))
			result.appendChild(child);
		return result;
	}
	
	private List<WomNode> mapDropNull(NodeList l)
	{
		if (l == null)
			return Collections.emptyList();
		
		LinkedList<WomNode> result = new LinkedList<WomNode>();
		for (AstNode n : l)
		{
			WomNode o = (WomNode) dispatch(n);
			if (o != null)
				result.add(o);
		}
		
		if (result.isEmpty())
			return Collections.emptyList();
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private <T> List<T> mapDropNull(NodeList l, Class<T> clazz)
	{
		return (List<T>) mapDropNull(l);
	}
}
