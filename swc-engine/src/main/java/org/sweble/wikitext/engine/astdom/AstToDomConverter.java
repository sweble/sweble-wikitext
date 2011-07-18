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
import org.sweble.wikitext.engine.dom.DomAttribute;
import org.sweble.wikitext.engine.dom.DomNode;
import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration;
import org.sweble.wikitext.lazy.ParserConfigInterface;
import org.sweble.wikitext.lazy.parser.LazyParsedPage;
import org.sweble.wikitext.lazy.parser.Paragraph;
import org.sweble.wikitext.lazy.parser.XmlElement;
import org.sweble.wikitext.lazy.preprocessor.LazyPreprocessedPage;
import org.sweble.wikitext.lazy.utils.XmlAttribute;

import de.fau.cs.osr.ptk.common.Visitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;
import de.fau.cs.osr.utils.FmtNotYetImplementedError;

public class AstToDomConverter
        extends
            Visitor
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
	
	public static DomNode convert(SimpleWikiConfiguration config, PageTitle title, Page page)
	{
		final AstToDomConverter converter = new AstToDomConverter(
		        config,
		        title.getNamespace().getCanonical(),
		        null,
		        title.getTitle());
		
		return (DomNode) converter.go(page);
	}
	
	// =========================================================================
	
	public DomNode visit(LazyPreprocessedPage p)
	{
		final Document document = new Document(p, namespace, path, title);
		for (DomNode child : mapDropNull(p.getContent()))
			document.appendChild(child);
		return document;
	}
	
	public DomNode visit(LazyParsedPage p)
	{
		final Document document = new Document(p, namespace, path, title);
		for (DomNode child : mapDropNull(p.getContent()))
			document.appendChild(child);
		return document;
	}
	
	public DomNode visit(Page p)
	{
		final Document document = new Document(p, namespace, path, title);
		for (DomNode child : mapDropNull(p.getContent()))
			document.appendChild(child);
		return document;
	}
	
	public DomNode visit(Paragraph p)
	{
		final ParagraphAdapter paragraph = new ParagraphAdapter(p);
		for (DomNode child : mapDropNull(p.getContent()))
			paragraph.appendChild(child);
		return paragraph;
	}
	
	public DomNode visit(Text p)
	{
		return new TextAdaptor(p);
	}
	
	public DomNode visit(XmlElement e)
	{
		XmlElementFactoryEnum f = xmlElements.get(e.getName().toLowerCase());
		if (f == null)
			throw new FmtNotYetImplementedError(
			        "Unmapable xml elements are not yet supported");
		
		DomNode result = f.create(e);
		
		for (DomAttribute attr : mapDropNull(e.getXmlAttributes(), DomAttribute.class))
			result.setAttributeNode(attr);
		
		for (DomNode child : mapDropNull(e.getBody()))
			result.appendChild(child);
		
		return result;
	}
	
	public DomAttribute visit(XmlAttribute a)
	{
		return new XmlAttributeAdapter(config, a);
	}
	
	// =========================================================================
	
	private List<DomNode> mapDropNull(NodeList l)
	{
		if (l == null)
			return Collections.emptyList();
		
		LinkedList<DomNode> result = new LinkedList<DomNode>();
		for (AstNode n : l)
		{
			DomNode o = (DomNode) dispatch(n);
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
