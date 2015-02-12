package org.sweble.wikitext.engine.ext.scripting;

import java.util.Map;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.TagExtensionBase;
import org.sweble.wikitext.engine.config.TagExtensionGroup;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.ext.builtin.BuiltInTagExtensions;
import org.sweble.wikitext.engine.ext.ref.RefTagExt;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtTagExtension;
import org.sweble.wikitext.parser.nodes.WtTagExtensionBody;
import org.sweble.wikitext.parser.nodes.WtXmlElement;

public class ScriptingTagExtensions
	extends
	TagExtensionGroup
	{	
	// =========================================================================
	// ==
	// == <script>
	// ==
	// =========================================================================
	// by Manuel
	
	protected ScriptingTagExtensions(WikiConfig wikiConfig) {
		if (wikiConfig.isScriptingEnabled()) {
			
			addTagExtension(new TagExtensionScript(wikiConfig));
			addTagExtension(new TagExtensionExternalScript(wikiConfig));
			addTagExtension(new TagExtensionForm(wikiConfig));
			addTagExtension(new TagExtensionInput(wikiConfig));
			addTagExtension(new TagExtensionButton(wikiConfig));
			addTagExtension(new TagExtensionTextarea(wikiConfig));
		}

	}

	public static ScriptingTagExtensions group(WikiConfig wikiConfig)
	{
		return new ScriptingTagExtensions(wikiConfig);
	}

	//TagExtensionScript
	public static final class TagExtensionScript
			extends
				TagExtensionBase
	{
		private static final long serialVersionUID = 1L;
		
		/**
		 * For un-marshaling only.
		 */
		public TagExtensionScript()
		{
			super("script");
		}
		
		public TagExtensionScript(WikiConfig wikiConfig)
		{
			super(wikiConfig, "script");
		}
		
		@Override
		public WtNode invoke(
				ExpansionFrame frame,
				WtTagExtension tagExt,
				Map<String, WtNodeList> attrs,
				WtTagExtensionBody body)
		{
			WtXmlElement script = nf().elem(
					"script",
					tagExt.getXmlAttributes(),
					nf().body(nf().list(nf().text(body.getContent()))));
			script.setRtd(tagExt.getRtd());
			return script;
		}
	}

	//TagExtensionExternalScript
	public static final class TagExtensionExternalScript
			extends
				TagExtensionBase
	{
		private static final long serialVersionUID = 1L;
		
		/**
		 * For un-marshaling only.
		 */
		public TagExtensionExternalScript()
		{
			super("external-script");
		}
		
		public TagExtensionExternalScript(WikiConfig wikiConfig)
		{
			super(wikiConfig, "external-script");
		}
		
		@Override
		public WtNode invoke(
				ExpansionFrame frame,
				WtTagExtension tagExt,
				Map<String, WtNodeList> attrs,
				WtTagExtensionBody body)
		{
			WtXmlElement script = nf().elem(
					"external-script",
					tagExt.getXmlAttributes(),
					nf().body(nf().list(nf().text(body.getContent()))));
			script.setRtd(tagExt.getRtd());
			return script;
		}
	}


	//TagExtensionForm
	public static final class TagExtensionForm
			extends
				TagExtensionBase
	{
		private static final long serialVersionUID = 1L;
		
		public TagExtensionForm()
		{
			super("form");
		}
		
		public TagExtensionForm(WikiConfig wikiConfig)
		{
			super(wikiConfig, "form");
		}
		
		@Override
		public WtNode invoke(
				ExpansionFrame frame,
				WtTagExtension tagExt,
				Map<String, WtNodeList> attrs,
				WtTagExtensionBody body)
		{
			WtXmlElement script = nf().elem(
					"form",
					tagExt.getXmlAttributes(),
					nf().body(nf().list(nf().text(body.getContent()))));
			script.setRtd(tagExt.getRtd());
			return script;
		}
	}

	//TagExtensionInput
	public static final class TagExtensionInput
			extends
				TagExtensionBase
	{
		private static final long serialVersionUID = 1L;
		
		public TagExtensionInput()
		{
			super("input");
		}
		
		public TagExtensionInput(WikiConfig wikiConfig)
		{
			super(wikiConfig, "input");
		}
		
		@Override
		public WtNode invoke(
				ExpansionFrame frame,
				WtTagExtension tagExt,
				Map<String, WtNodeList> attrs,
				WtTagExtensionBody body)
		{
			WtXmlElement script = nf().elem(
					"input",
					tagExt.getXmlAttributes(),
					nf().body(nf().list(nf().text(body.getContent()))));
			script.setRtd(tagExt.getRtd());
			return script;
		}
	}
	//TagExtensionButton
	public static final class TagExtensionButton
			extends
				TagExtensionBase
	{
		private static final long serialVersionUID = 1L;
		
		public TagExtensionButton()
		{
			super("button");
		}
		
		public TagExtensionButton(WikiConfig wikiConfig)
		{
			super(wikiConfig, "button");
		}
		
		@Override
		public WtNode invoke(
				ExpansionFrame frame,
				WtTagExtension tagExt,
				Map<String, WtNodeList> attrs,
				WtTagExtensionBody body)
		{
			WtXmlElement script = nf().elem(
					"button",
					tagExt.getXmlAttributes(),
					nf().body(nf().list(nf().text(body.getContent()))));
			script.setRtd(tagExt.getRtd());
			return script;
		}
	}
	//TagExtensionTextarea
	public static final class TagExtensionTextarea
			extends
				TagExtensionBase
	{
		private static final long serialVersionUID = 1L;
		
		public TagExtensionTextarea()
		{
			super("textarea");
		}
		
		public TagExtensionTextarea(WikiConfig wikiConfig)
		{
			super(wikiConfig, "textarea");
		}
		
		@Override
		public WtNode invoke(
				ExpansionFrame frame,
				WtTagExtension tagExt,
				Map<String, WtNodeList> attrs,
				WtTagExtensionBody body)
		{
			WtXmlElement script = nf().elem(
					"textarea",
					tagExt.getXmlAttributes(),
					nf().body(nf().list(nf().text(body.getContent()))));
			script.setRtd(tagExt.getRtd());
			return script;
		}
	}

	//TagExtensionSelection
	public static final class TagExtensionSelection
			extends
				TagExtensionBase
	{
		private static final long serialVersionUID = 1L;
		
		public TagExtensionSelection()
		{
			super("select");
		}
		
		public TagExtensionSelection(WikiConfig wikiConfig)
		{
			super(wikiConfig, "select");
		}
		
		@Override
		public WtNode invoke(
				ExpansionFrame frame,
				WtTagExtension tagExt,
				Map<String, WtNodeList> attrs,
				WtTagExtensionBody body)
		{
			WtXmlElement script = nf().elem(
					"select",
					tagExt.getXmlAttributes(),
					nf().body(nf().list(nf().text(body.getContent()))));
			script.setRtd(tagExt.getRtd());
			return script;
		}
	}
	//options,label

}
