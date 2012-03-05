package org.sweble.wikitext.engine.wom.tools;

import org.sweble.wikitext.engine.astwom.adapters.BodyAdapter;
import org.sweble.wikitext.engine.astwom.adapters.BoldAdapter;
import org.sweble.wikitext.engine.astwom.adapters.CommentAdapter;
import org.sweble.wikitext.engine.astwom.adapters.HorizontalRuleAdapter;
import org.sweble.wikitext.engine.astwom.adapters.PageAdapter;
import org.sweble.wikitext.engine.astwom.adapters.TextAdapter;
import org.sweble.wikitext.engine.wom.WomBody;
import org.sweble.wikitext.engine.wom.WomBold;
import org.sweble.wikitext.engine.wom.WomComment;
import org.sweble.wikitext.engine.wom.WomHorizontalRule;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomPage;

/**
 * Builder for a WOM tree using fluent interfaces.
 */
public class AstWomBuilder
{
	public static WomPageBuilder womPage()
	{
		return new WomPageBuilder();
	}
	
	public static WomBodyBuilder womBody()
	{
		return new WomBodyBuilder();
	}
	
	public static WomCommentBuilder womComment()
	{
		return new WomCommentBuilder();
	}
	
	public static WomHorizontalRuleBuilder womHr()
	{
		return new WomHorizontalRuleBuilder();
	}
	
	public static WomBoldBuilder womBold()
	{
		return new WomBoldBuilder();
	}
	
	public static WomTextBuilder womText()
	{
		return new WomTextBuilder();
	}
	
	// =========================================================================
	
	public static final class WomCommentBuilder
	{
		private String text = " Default Comment Text ";
		
		public WomCommentBuilder withText(String text)
		{
			this.text = text;
			return this;
		}
		
		public WomComment build()
		{
			return new CommentAdapter(text);
		}
	}
	
	public static final class WomBodyBuilder
	{
		private WomBody body = new BodyAdapter();
		
		public WomBodyBuilder withContent(WomNode... contents)
		{
			for (WomNode n : contents)
				this.body.appendChild(n);
			return this;
		}
		
		public WomBody build()
		{
			return this.body;
		}
	}
	
	public static final class WomPageBuilder
	{
		private String namespace = null;
		
		private String path = null;
		
		private String title = "Default Page";
		
		private WomBody body = null;
		
		public WomPageBuilder withBody(WomNode... contents)
		{
			this.body = new BodyAdapter();
			for (WomNode n : contents)
				this.body.appendChild(n);
			return this;
		}
		
		public WomPageBuilder withNamespace(String ns)
		{
			this.namespace = ns;
			return this;
		}
		
		public WomPageBuilder withPath(String path)
		{
			this.path = path;
			return this;
		}
		
		public WomPageBuilder withTitle(String title)
		{
			this.title = title;
			return this;
		}
		
		public WomPage build()
		{
			PageAdapter page = new PageAdapter(this.namespace, this.path, this.title);
			if (this.body != null)
				page.setBody(this.body);
			return page;
		}
	}
	
	public static final class WomHorizontalRuleBuilder
	{
		private HorizontalRuleAdapter hr = new HorizontalRuleAdapter();
		
		public WomHorizontalRule build()
		{
			return hr;
		}
	}
	
	public static final class WomBoldBuilder
	{
		public WomBold build()
		{
			return new BoldAdapter();
		}
	}
	
	public static final class WomTextBuilder
	{
		private String text;
		
		public WomTextBuilder withText(String text)
		{
			this.text = text;
			return this;
		}
		
		public WomNode build()
		{
			return new TextAdapter(null, this.text);
		}
	}
}
