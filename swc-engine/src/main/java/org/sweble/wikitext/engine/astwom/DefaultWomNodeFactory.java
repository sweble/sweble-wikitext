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
package org.sweble.wikitext.engine.astwom;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.sweble.wikitext.engine.Page;
import org.sweble.wikitext.engine.astwom.adapters.BoldAdapter;
import org.sweble.wikitext.engine.astwom.adapters.CategoryAdapter;
import org.sweble.wikitext.engine.astwom.adapters.CommentAdapter;
import org.sweble.wikitext.engine.astwom.adapters.HorizontalRuleAdapter;
import org.sweble.wikitext.engine.astwom.adapters.PageAdapter;
import org.sweble.wikitext.engine.astwom.adapters.ParagraphAdapter;
import org.sweble.wikitext.engine.astwom.adapters.TextAdapter;
import org.sweble.wikitext.engine.config.Namespace;
import org.sweble.wikitext.engine.config.WikiConfigurationInterface;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.lazy.LinkTargetException;
import org.sweble.wikitext.lazy.LinkTargetParser;
import org.sweble.wikitext.lazy.ParserConfigInterface.TargetType;
import org.sweble.wikitext.lazy.parser.Bold;
import org.sweble.wikitext.lazy.parser.HorizontalRule;
import org.sweble.wikitext.lazy.parser.InternalLink;
import org.sweble.wikitext.lazy.parser.Newline;
import org.sweble.wikitext.lazy.parser.Paragraph;
import org.sweble.wikitext.lazy.parser.XmlElement;
import org.sweble.wikitext.lazy.preprocessor.XmlComment;
import org.sweble.wikitext.lazy.utils.XmlCharRef;
import org.sweble.wikitext.lazy.utils.XmlEntityRef;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;

public class DefaultWomNodeFactory
		extends
			AstVisitor
		implements
			WomNodeFactory
{
	private final WikiConfigurationInterface config;
	
	private final String title;
	
	private final Namespace CATEGORY_NAMESPACE;
	
	private final LinkedList<CategoryAdapter> categories = new LinkedList<CategoryAdapter>();
	
	private NodeList container;
	
	// =========================================================================
	
	public DefaultWomNodeFactory(WikiConfigurationInterface config, String title)
	{
		this.config = config;
		this.title = title;
		
		CATEGORY_NAMESPACE = config.getNamespace("Category");
	}
	
	public List<CategoryAdapter> getCategories()
	{
		return Collections.unmodifiableList(categories);
	}
	
	// =========================================================================
	
	@Override
	public WomNode create(NodeList container, AstNode node)
	{
		this.container = container;
		return (WomNode) go(node);
	}
	
	@Override
	public String resolveXmlEntity(String name)
	{
		return config.resolveXmlEntity(name);
	}
	
	// =========================================================================
	
	public WomNode visit(Page page)
	{
		PageAdapter p = new PageAdapter(this, page, title);
		
		for (CategoryAdapter c : categories)
			p.registerCategory(c);
		
		return p;
	}
	
	public WomNode visit(Text text)
	{
		return new TextAdapter(text);
	}
	
	public WomNode visit(Newline newline)
	{
		return new TextAdapter(newline);
	}
	
	public WomNode visit(XmlComment comment)
	{
		return new CommentAdapter(comment);
	}
	
	public WomNode visit(Paragraph para)
	{
		return new ParagraphAdapter(this, para);
	}
	
	public WomNode visit(Bold bold)
	{
		return new BoldAdapter(this, bold);
	}
	
	public WomNode visit(HorizontalRule hr)
	{
		return new HorizontalRuleAdapter(hr);
	}
	
	public WomNode visit(InternalLink link) throws LinkTargetException
	{
		LinkTargetParser ltp = new LinkTargetParser();
		
		// can throw ... but shouldn't ...
		ltp.parse(config, link.getTarget());
		
		TargetType type = config.classifyTarget(link.getTarget());
		if (type == TargetType.IMAGE)
		{
			// TODO: Generate ImageLink
			return null;
		}
		else
		{
			if (type != TargetType.PAGE)
				throw new InternalError();
			
			Namespace namespace = null;
			String ns = ltp.getNamespace();
			if (ns != null)
				namespace = config.getNamespace(ns);
			
			if (namespace != null && namespace.equals(CATEGORY_NAMESPACE))
			{
				//if (this.page == null)
				//	throw new RuntimeException("Cannot register category with page");
				
				if (this.container == null)
					throw new RuntimeException("Cannot associate AST category node with its container");
				
				categories.add(new CategoryAdapter(this.container, link));
				
				return null;
			}
			else
			{
				// TODO: Generate PageLink
				return null;
			}
		}
	}
	
	public WomNode visit(XmlElement elem)
	{
		WomNode result = createFromXmlElement(elem);
		if (result == null)
			throw new UnsupportedXmlElement(elem);
		return result;
	}
	
	public WomNode visit(XmlEntityRef ref)
	{
		return new TextAdapter(this, ref);
	}
	
	public WomNode visit(XmlCharRef ref)
	{
		return new TextAdapter(ref);
	}
	
	// =========================================================================
	
	private WomNode createFromXmlElement(XmlElement e)
	{
		XmlElementFactoryEnum resolved =
				XmlElementFactoryEnum.valueOf(e.getName().toUpperCase());
		if (resolved == null)
			return null;
		
		return resolved.create(this, e);
	}
	
	public enum XmlElementFactoryEnum
	{
		/*
		BLOCKQUOTE
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementBlockquoteAdapter(e);
			}
		},
		
		DIV
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementDivAdapter(e);
			}
		},
		
		PRE
		{
			@Override
			public DomNode create(XmlElement e)
			{
				return new PREAdapter(e);
			}
		},
		
		CENTER
		{
			@Override
			public DomNode create(XmlElement e)
			{
				return new CENTERAdapter(e);
			}
		},
		*/
		
		P
		{
			@Override
			public WomNode create(WomNodeFactory womNodeFactory, XmlElement e)
			{
				return new ParagraphAdapter(womNodeFactory, e);
			}
		},
		
		HR
		{
			@Override
			public WomNode create(WomNodeFactory womNodeFactory, XmlElement e)
			{
				return new HorizontalRuleAdapter(womNodeFactory, e);
			}
		},
		
		// =====================================================================
		
		/*
		OL
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementOrderedListAdapter(e);
			}
		},
		
		UL
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementUnorderedListAdapter(e);
			}
		},
		
		LI
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementListItemAdapter(e);
			}
		},
		*/
		
		// =====================================================================
		
		/*
		DL
		{
			@Override
			public DomNode create(XmlElement e)
			{
				return new DLAdapter(e);
			}
		},

		DD
		{
			@Override
			public DomNode create(XmlElement e)
			{
				return new DDAdapter(e);
			}
		},
		
		DT
		{
			@Override
			public DomNode create(XmlElement e)
			{
				return new DTAdapter(e);
			}
		},
		*/
		
		// =====================================================================
		
		/*
		TABLE
		{
			@Override
			public DomNode create(XmlElement e)
			{
				return new TABLEAdapter(e);
			}
		},
		
		TD
		{
			@Override
			public DomNode create(XmlElement e)
			{
				return new TDAdapter(e);
			}
		},
		
		TR
		{
			@Override
			public DomNode create(XmlElement e)
			{
				return new TRAdapter(e);
			}
		},
		*/
		
		// =====================================================================
		
		/*
		ABBR
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementAbbrAdapter(e);
			}
		},
		
		BIG
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementBigAdapter(e);
			}
		},
		*/
		
		B
		{
			@Override
			public WomNode create(WomNodeFactory womNodeFactory, XmlElement e)
			{
				return new BoldAdapter(womNodeFactory, e);
			}
		},
		
		/*
		BR
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementBrAdapter(e);
			}
		},
		
		CITE
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementCiteAdapter(e);
			}
		},
		
		CODE
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementCodeAdapter(e);
			}
		},
		
		DFN
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementDfnAdapter(e);
			}
		},
		
		EM
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementEmAdapter(e);
			}
		},
		
		FONT
		{
			@Override
			public DomNode create(XmlElement e)
			{
				return new FONTAdapter(e);
			}
		},
		
		I
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementItalicsAdapter(e);
			}
		},
		
		KBD
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementKbdAdapter(e);
			}
		},
		
		S
		{
			@Override
			public DomNode create(XmlElement e)
			{
				return new SAdapter(e);
			}
		},
		
		SAMP
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementSampAdapter(e);
			}
		},
		
		SMALL
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementSmallAdapter(e);
			}
		},
		
		SPAN
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementSpanAdapter(e);
			}
		},
		
		STRIKE
		{
			@Override
			public DomNode create(XmlElement e)
			{
				return new STRIKEAdapter(e);
			}
		},
		
		STRONG
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementStrongAdapter(e);
			}
		},
		
		SUB
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementSubAdapter(e);
			}
		},
		
		SUP
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementSupAdapter(e);
			}
		},
		
		TT
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementTeletypeAdapter(e);
			}
		},
		
		U
		{
			@Override
			public DomNode create(XmlElement e)
			{
				return new UAdapter(e);
			}
		},
		
		VAR
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementVarAdapter(e);
			}
		},
		*/
		
		// =====================================================================
		
		/*
		DEL
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementDelAdapter(e);
			}
		},
		
		INS
		{
			@Override
			public WomNode create(XmlElement e)
			{
				return new XmlElementInsAdapter(e);
			}
		}*/;
		
		// =====================================================================
		
		public abstract WomNode create(
				WomNodeFactory womNodeFactory,
				XmlElement e);
	}
}
