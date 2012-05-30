package org.sweble.wikitext.lazy.utils;

import org.sweble.wikitext.lazy.parser.HorizontalRule;
import org.sweble.wikitext.lazy.parser.XmlElement;
import org.sweble.wikitext.lazy.preprocessor.TemplateArgument;
import org.sweble.wikitext.lazy.preprocessor.XmlComment;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;

public class AstBuilder
{
	public static XmlCommentBuilder astComment()
	{
		return new XmlCommentBuilder();
	}
	
	public static HorizontalRuleBuilder astHr()
	{
		return new HorizontalRuleBuilder();
	}
	
	public static XmlElementBuilder astE()
	{
		return new XmlElementBuilder();
	}
	
	public static XmlAttributeBuilder astXmlAttrib()
	{
		return new XmlAttributeBuilder();
	}
	
	public static TextBuilder astText()
	{
		return new TextBuilder();
	}
	
	public static TmplArgBuilder astTmplArg()
	{
		return new TmplArgBuilder();
	}
	
	public static XmlAttribBuilder astXmlAttribute()
	{
		return new XmlAttribBuilder();
	}
	
	// =========================================================================
	
	public static final class XmlCommentBuilder
	{
		private String content;
		
		public XmlCommentBuilder withContent(String content)
		{
			this.content = content;
			return this;
		}
		
		public XmlComment build()
		{
			return new XmlComment(this.content);
		}
	}
	
	public static final class HorizontalRuleBuilder
	{
		public HorizontalRule build()
		{
			return new HorizontalRule();
		}
	}
	
	public static final class XmlElementBuilder
	{
		private String name = "element";
		
		private NodeList body = new NodeList(new Text("element body"));
		
		private NodeList attribs = null;
		
		public XmlElementBuilder withName(String name)
		{
			this.name = name;
			return this;
		}
		
		public XmlElementBuilder withBody(AstNode... content)
		{
			this.body = new NodeList();
			for (AstNode n : content)
				this.body.add(n);
			return this;
		}
		
		public XmlElementBuilder withAttribs(AstNode... attribs)
		{
			this.attribs = new NodeList();
			for (AstNode n : attribs)
				this.attribs.add(n);
			return this;
		}
		
		public XmlElement build()
		{
			return new XmlElement(this.name, this.body == null, this.attribs, this.body);
		}
	}
	
	public static final class TextBuilder
	{
		private String text = "default text";
		
		public TextBuilder withText(String text)
		{
			this.text = text;
			return this;
		}
		
		public Text build()
		{
			return new Text(text);
		}
	}
	
	public static final class XmlAttributeBuilder
	{
		private String name = "default name";
		
		private NodeList value = new NodeList(new Text("default value"));
		
		public XmlAttributeBuilder withName(String name)
		{
			this.name = name;
			return this;
		}
		
		public XmlAttributeBuilder withValue(String text)
		{
			return this.withValue(new Text(text));
		}
		
		public XmlAttributeBuilder withValue(AstNode... value)
		{
			this.value = new NodeList();
			for (AstNode n : value)
				this.value.add(n);
			return this;
		}
		
		public XmlAttribute build()
		{
			return new XmlAttribute(name, value, value != null);
		}
	}
	
	public static final class TmplArgBuilder
	{
		private NodeList name = null;
		
		private NodeList value;
		
		public TmplArgBuilder withName(String name)
		{
			this.name = new NodeList(new Text(name));
			return this;
		}
		
		public TmplArgBuilder withValue(String value)
		{
			this.value = new NodeList(new Text(value));
			return this;
		}
		
		public TemplateArgument build()
		{
			if (value == null)
				value = new NodeList();
			return new TemplateArgument(name, value, name != null);
		}
	}
	
	public static final class XmlAttribBuilder
	{
		private String name = "defaultAttrName";
		
		private NodeList value = null;
		
		public XmlAttribBuilder withName(String name)
		{
			this.name = name;
			return this;
		}
		
		public XmlAttribBuilder withValue(String value)
		{
			this.value = new NodeList(new Text(value));
			return this;
		}
		
		public XmlAttribBuilder withValue(AstNode... content)
		{
			this.value = new NodeList();
			for (AstNode n : content)
				this.value.add(n);
			return this;
		}
		
		public XmlAttribBuilder withoutValue()
		{
			this.value = null;
			return this;
		}
		
		public XmlAttribute build()
		{
			return new XmlAttribute(name, value, value != null);
		}
	}
}
