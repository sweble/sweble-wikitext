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

package org.sweble.wikitext.lazy.utils;

import java.util.Arrays;
import java.util.List;

import org.sweble.wikitext.lazy.parser.HorizontalRule;
import org.sweble.wikitext.lazy.parser.XmlElement;
import org.sweble.wikitext.lazy.preprocessor.Ignored;
import org.sweble.wikitext.lazy.preprocessor.ProtectedText;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.preprocessor.TemplateArgument;
import org.sweble.wikitext.lazy.preprocessor.XmlComment;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;

public class AstBuilder
{
	@Deprecated
	public static XmlCommentBuilder astComment()
	{
		return new XmlCommentBuilder();
	}
	
	public static XmlComment astComment(String text)
	{
		return new XmlComment(text);
	}
	
	public static Ignored astIgnored()
	{
		return new Ignored("This should be ignored");
	}
	
	public static Ignored astIgnored(String text)
	{
		return new Ignored(text);
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
	
	@Deprecated
	public static TextBuilder astText()
	{
		return new TextBuilder();
	}
	
	public static AstNode astProtectedText(String text)
	{
		return new ProtectedText(text);
	}
	
	public static Text astText(String text)
	{
		return new Text(text);
	}
	
	public static NodeList astList(AstNode... contents)
	{
		return new NodeList(Arrays.asList(contents));
	}
	
	public static TmplArgBuilder astTmplArg()
	{
		return new TmplArgBuilder();
	}
	
	public static XmlAttribBuilder astXmlAttribute()
	{
		return new XmlAttribBuilder();
	}
	
	public static TemplateBuilder astTemplate()
	{
		return new TemplateBuilder();
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
	
	public static final class TemplateBuilder
	{
		private NodeList name = null;
		
		private NodeList args = null;
		
		public TemplateBuilder withName(AstNode... name)
		{
			this.name = astList(name);
			return this;
		}
		
		public TemplateBuilder withArguments(TemplateArgument... args)
		{
			this.args = astList(args);
			return this;
		}
		
		public TemplateBuilder withArguments(List<TemplateArgument> args)
		{
			return withArguments(args.toArray(new TemplateArgument[args.size()]));
		}
		
		public Template build()
		{
			if (name == null)
				name = astList(astText("default template"));
			if (args == null)
				args = astList();
			return new Template(name, args);
		}
	}
}
