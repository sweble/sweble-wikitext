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

package org.sweble.wikitext.parser.postprocessor;

import org.sweble.wikitext.parser.nodes.WikitextNodeFactory;
import org.sweble.wikitext.parser.nodes.WtImEndTag;
import org.sweble.wikitext.parser.nodes.WtImStartTag;
import org.sweble.wikitext.parser.nodes.WtNode;

public enum IntermediateTags
{
	ITALICS
	{
		@Override
		public String getElementName()
		{
			return "@i";
		}

		public WtNode createOpen(WikitextNodeFactory nf, boolean repair)
		{
			WtNode tag = super.createOpen(nf, repair);
			if (!repair)
				tag.setRtd("''");
			return tag;
		}

		public WtNode createClose(WikitextNodeFactory nf, boolean repair)
		{
			WtNode tag = super.createClose(nf, repair);
			if (!repair)
				tag.setRtd("''");
			return tag;
		}
	},

	// =========================================================================

	BOLD
	{
		@Override
		public String getElementName()
		{
			return "@b";
		}

		public WtNode createOpen(WikitextNodeFactory nf, boolean repair)
		{
			WtNode tag = super.createOpen(nf, repair);
			if (!repair)
				tag.setRtd("'''");
			return tag;
		}

		public WtNode createClose(WikitextNodeFactory nf, boolean repair)
		{
			WtNode tag = super.createClose(nf, repair);
			if (!repair)
				tag.setRtd("'''");
			return tag;
		}
	},

	// =========================================================================

	PARAGRAPH
	{
		@Override
		public String getElementName()
		{
			return "@p";
		}
	};

	// =========================================================================

	public abstract String getElementName();

	public WtNode createOpen(WikitextNodeFactory nf, boolean repair)
	{
		WtImStartTag tag = nf.imStartTag(this);
		/*
		if (repair)
			WtNodeFlags.setRepairNode(tag);
		*/
		return tag;
	}

	public WtNode createClose(WikitextNodeFactory nf, boolean repair)
	{
		WtImEndTag tag = nf.imEndTag(this);
		/*
		if (repair)
			WtNodeFlags.setRepairNode(tag);
		*/
		return tag;
	}
}
