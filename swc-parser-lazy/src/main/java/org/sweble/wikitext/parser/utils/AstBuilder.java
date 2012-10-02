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

package org.sweble.wikitext.parser.utils;

import java.util.Arrays;
import java.util.List;

import org.sweble.wikitext.parser.nodes.HorizontalRule;
import org.sweble.wikitext.parser.nodes.Ignored;
import org.sweble.wikitext.parser.nodes.TagExtension;
import org.sweble.wikitext.parser.nodes.Template;
import org.sweble.wikitext.parser.nodes.TemplateArgument;
import org.sweble.wikitext.parser.nodes.WikitextNode;
import org.sweble.wikitext.parser.nodes.WtList;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.XmlAttribute;
import org.sweble.wikitext.parser.nodes.XmlComment;
import org.sweble.wikitext.parser.nodes.XmlElement;
import org.sweble.wikitext.parser.preprocessor.ProtectedText;

public class AstBuilder
{
	public static TemplateArgumentBuilder astTemplateArg()
	{
		return new TemplateArgumentBuilder();
	}
	
	public static TagExtensionBuilder astTagExtension()
	{
		return new TagExtensionBuilder();
	}
	
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
	
	@Deprecated
	public static TextBuilder astText()
	{
		return new TextBuilder();
	}
	
	public static WikitextNode astProtectedText(String text)
	{
		return new ProtectedText(text);
	}
	
	public static WtText astText(String text)
	{
		return new WtText(text);
	}
	
	public static WtText astProtected(String text)
	{
		return new ProtectedText(text);
	}
	
	public static WtList astList(WikitextNode... contents)
	{
		return new WtList(Arrays.asList(contents));
	}
	
	public static TmplArgBuilder astTmplArg()
	{
		return new TmplArgBuilder();
	}
	
	public static XmlAttribBuilder astXmlAttrib()
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
		
		private WtList body = astList(astText("element body"));
		
		private WtList attribs = null;
		
		public XmlElementBuilder withName(String name)
		{
			this.name = name;
			return this;
		}
		
		public XmlElementBuilder withBody(WikitextNode... content)
		{
			this.body = new WtList();
			for (WikitextNode n : content)
				this.body.add(n);
			return this;
		}
		
		public XmlElementBuilder withAttribs(WikitextNode... attribs)
		{
			this.attribs = new WtList();
			for (WikitextNode n : attribs)
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
		
		public WtText build()
		{
			return new WtText(text);
		}
	}
	
	public static final class XmlAttribBuilder
	{
		private String name = "defaultAttrName";
		
		private WtList value = null;
		
		public XmlAttribBuilder withName(String name)
		{
			this.name = name;
			return this;
		}
		
		public XmlAttribBuilder withValue(String value)
		{
			this.value = new WtList(new WtText(value));
			return this;
		}
		
		public XmlAttribBuilder withValue(WikitextNode... content)
		{
			this.value = new WtList();
			for (WikitextNode n : content)
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
	
	public static final class TmplArgBuilder
	{
		private WtList name = null;
		
		private WtList value;
		
		public TmplArgBuilder withName(String name)
		{
			this.name = new WtList(new WtText(name));
			return this;
		}
		
		public TmplArgBuilder withValue(String value)
		{
			this.value = new WtList(new WtText(value));
			return this;
		}
		
		public TemplateArgument build()
		{
			if (value == null)
				value = new WtList();
			return new TemplateArgument(name, value, name != null);
		}
	}
	
	public static final class TemplateBuilder
	{
		private WtList name = null;
		
		private WtList args = null;
		
		public TemplateBuilder withName(WikitextNode... name)
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
	
	public static final class TagExtensionBuilder
	{
		private String name = "someTagExtension";
		
		private WtList xmlAttributes = astList();
		
		private String body = "";
		
		public TagExtensionBuilder withName(String name)
		{
			this.name = name;
			return this;
		}
		
		public TagExtensionBuilder withAttribs(WikitextNode... attribs)
		{
			this.xmlAttributes = astList(attribs);
			return this;
		}
		
		public TagExtensionBuilder withBody(String body)
		{
			this.body = body;
			return this;
		}
		
		public TagExtension build()
		{
			return new TagExtension(name, xmlAttributes, body);
		}
	}
	
	public static final class TemplateArgumentBuilder
	{
		private WtList name = astList(astText("argName"));
		
		private WtList value = astList(astText("value"));
		
		public TemplateArgumentBuilder withoutName()
		{
			this.name = null;
			return this;
		}
		
		public TemplateArgumentBuilder withName(WikitextNode... name)
		{
			this.name = astList(name);
			return this;
		}
		
		public TemplateArgumentBuilder setValue(WikitextNode... value)
		{
			this.value = astList(value);
			return this;
		}
		
		public TemplateArgument build()
		{
			return new TemplateArgument(name, value, name != null);
		}
	}
}
