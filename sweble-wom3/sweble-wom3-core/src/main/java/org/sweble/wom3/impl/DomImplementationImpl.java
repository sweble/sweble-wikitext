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

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3DomImplementation;
import org.sweble.wom3.Wom3ElementNode;
import org.sweble.wom3.Wom3Node;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class DomImplementationImpl
		implements
			Wom3DomImplementation,
			Serializable
{
	private static final long serialVersionUID = 1L;

	private static final Map<ElemTypeId, Class<? extends Wom3ElementNode>> nodeImplMap =
			new HashMap<ElemTypeId, Class<? extends Wom3ElementNode>>();

	private static final DomImplementationImpl singleton =
			new DomImplementationImpl();

	// =========================================================================

	static
	{
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "abbr", AbbrImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "article", ArticleImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "articles", ArticlesImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "b", BoldImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "big", BigImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "blockquote", BlockquoteImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "body", BodyImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "br", BreakImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "caption", TableCaptionImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "category", CategoryImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "center", CenterImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "cite", CiteImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "code", CodeImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "comment", CommentImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "dd", DefinitionListDefImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "del", DelImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "dfn", DfnImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "div", DivImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "dl", DefinitionListImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "dt", DefinitionListTermImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "em", EmphasizeImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "extlink", ExtLinkImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "font", FontImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "for", ForImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "heading", HeadingImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "hr", HorizontalRuleImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "i", ItalicsImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "image", ImageImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "imgcaption", ImageCaptionImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "ins", InsImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "intlink", IntLinkImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "kbd", KbdImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "li", ListItemImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "nowiki", NowikiImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "ol", OrderedListImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "p", ParagraphImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "pre", PreImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "redirect", RedirectImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "ref", RefImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "repl", ReplImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "rtd", RtdImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "s", StrikeImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "samp", SampImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "section", SectionImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "signature", SignatureImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "small", SmallImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "span", SpanImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "strike", StrikeImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "strong", StrongImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "sub", SubImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "subst", SubstImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "sup", SupImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "table", TableImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "tbody", TableBodyImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "tcaption", TableCaptionImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "td", TableCellImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "text", TextImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "th", TableHeaderImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "title", TitleImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "tr", TableRowImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "tt", TeletypeImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "u", UnderlineImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "ul", UnorderedListImpl.class);
		addNodeImplInternal(Wom3Node.WOM_NS_URI, "var", VarImpl.class);
	}

	// =========================================================================

	public static DomImplementationImpl get()
	{
		return singleton;
	}

	protected static Class<? extends Wom3ElementNode> addNodeImplInternal(
			String namespaceUri,
			String localPart,
			Class<? extends Wom3ElementNode> clazz)
	{
		return nodeImplMap.put(new ElemTypeId(namespaceUri, localPart), clazz);
	}

	protected static Class<? extends Wom3ElementNode> getNodeImplInternal(
			String namespaceUri,
			String localPart)
	{
		return nodeImplMap.get(new ElemTypeId(namespaceUri, localPart));
	}

	protected static Map<ElemTypeId, Class<? extends Wom3ElementNode>> getNodeImpls()
	{
		return Collections.unmodifiableMap(nodeImplMap);
	}

	// =========================================================================

	protected DomImplementationImpl()
	{
	}

	// =========================================================================

	@Override
	public boolean hasFeature(String feature, String version)
	{
		return false;
	}

	/**
	 * @since DOM Level 3
	 */
	@Override
	public Object getFeature(String feature, String version)
	{
		return null;
	}

	/**
	 * @since DOM Level 2
	 */
	@Override
	public DocumentType createDocumentType(
			String qualifiedName,
			String publicId,
			String systemId) throws DOMException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @since DOM Level 2
	 */
	@Override
	public DocumentImpl createDocument(
			String namespaceURI,
			String qualifiedName,
			DocumentType doctype) throws DOMException
	{
		DocumentImpl doc = new DocumentImpl(this);

		if (doctype != null)
			doc.appendChild(doctype);

		if (qualifiedName != null || namespaceURI != null)
		{
			// We create the document element if we have either a qualified 
			// name, a namespace URI or both
			Element root = doc.createElementNS(namespaceURI, qualifiedName);
			doc.appendChild(root);
		}

		return doc;
	}

	// =========================================================================

	public Class<? extends Wom3ElementNode> addNodeImplementation(
			String namespaceUri,
			String localPart,
			Class<? extends Wom3ElementNode> clazz)
	{
		return addNodeImplInternal(namespaceUri, localPart, clazz);
	}

	public Class<? extends Wom3ElementNode> getNodeImpl(
			String namespaceUri,
			String localPart)
	{
		return getNodeImplInternal(namespaceUri, localPart);
	}

	// =========================================================================

	protected Wom3ElementNode createElement(DocumentImpl doc, String tagName)
	{
		Class<? extends Wom3ElementNode> impl = getNodeImpl(null, tagName);
		if (impl == null)
			return new ElementImpl(doc, tagName);

		return createElement(doc, impl, null);
	}

	protected Wom3ElementNode createElement(
			DocumentImpl doc,
			String namespaceURI,
			String qualifiedName)
	{
		int colon = qualifiedName.indexOf(':');
		String prefix;
		String localName;
		if (colon < 0)
		{
			prefix = null;
			localName = qualifiedName;
		}
		else
		{
			prefix = qualifiedName.substring(0, colon);
			localName = qualifiedName.substring(colon + 1);
		}

		Class<? extends Wom3ElementNode> impl = getNodeImpl(namespaceURI, localName);
		if (impl == null)
			return new ElementNsImpl(doc, namespaceURI, qualifiedName);

		return createElement(doc, impl, prefix);
	}

	protected Wom3ElementNode createElement(
			DocumentImpl doc,
			Class<? extends Wom3ElementNode> impl,
			String prefix)
	{
		Constructor<?> ctor = null;
		try
		{
			Constructor<?>[] ctors = impl.getConstructors();
			for (Constructor<?> candidate : ctors)
			{
				Class<?>[] paramTypes = candidate.getParameterTypes();
				if (paramTypes.length != 1)
					continue;
				Class<?> docType = paramTypes[0];
				if (docType.isAssignableFrom(doc.getClass()))
				{
					ctor = candidate;
					break;
				}
			}
		}
		catch (SecurityException e)
		{
			throw new RuntimeException("The constructor of the implementation " +
					"class for node `" + impl.getName() + "' is not accessible", e);
		}

		if (ctor == null)
			throw new RuntimeException("The implementation class for node `" +
					impl.getName() + "' does not provide the right kind of constructor");

		Exception e = null;
		try
		{
			Wom3ElementNode element = (Wom3ElementNode) ctor.newInstance(doc);
			if (prefix != null)
				element.setPrefix(prefix);
			return element;
		}
		catch (InstantiationException e_)
		{
			e = e_;
		}
		catch (IllegalAccessException e_)
		{
			e = e_;
		}
		catch (IllegalArgumentException e_)
		{
			e = e_;
		}
		catch (InvocationTargetException e_)
		{
			e = e_;
		}

		throw new RuntimeException("Failed to call the constructor of the " +
				"implementation class for node `" + impl.getName() + "'", e);
	}

	// =========================================================================

	public static final class ElemTypeId
	{
		public final String namespaceUri;

		public final String localPart;

		public ElemTypeId(String namespaceUri, String localPart)
		{
			assert localPart != null;
			this.namespaceUri = namespaceUri;
			this.localPart = localPart;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + localPart.hashCode();
			result = prime * result + ((namespaceUri == null) ? 0 : namespaceUri.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ElemTypeId other = (ElemTypeId) obj;
			if (localPart == null)
			{
				if (other.localPart != null)
					return false;
			}
			else if (!localPart.equals(other.localPart))
				return false;
			if (namespaceUri == null)
			{
				if (other.namespaceUri != null)
					return false;
			}
			else if (!namespaceUri.equals(other.namespaceUri))
				return false;
			return true;
		}
	}
}
