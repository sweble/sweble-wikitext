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

import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WtRtData;
import org.sweble.wikitext.parser.nodes.WtImEndTag;
import org.sweble.wikitext.parser.nodes.WtImStartTag;
import org.sweble.wikitext.parser.nodes.WtParagraph;
import org.sweble.wikitext.parser.nodes.WtBold;
import org.sweble.wikitext.parser.nodes.WtItalics;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;

public enum IntermediateTags
{
	ITALICS
	{
		@Override
		public String getElementName()
		{
			return "@i";
		}
		
		@Override
		public WtNode transform(
				ParserConfig config,
				WtImStartTag o,
				WtImEndTag c,
				WtNodeList body)
		{
			WtItalics e = new WtItalics(body);
			if (config.isGatherRtData())
			{
				String r0 = (o == null || o.isSynthetic()) ? null : "''";
				String r1 = (c == null || c.isSynthetic()) ? null : "''";
				e.setRtd(r0, WtRtData.SEP, r1);
			}
			return e;
		}
	},
	
	BOLD
	{
		@Override
		public String getElementName()
		{
			return "@b";
		}
		
		@Override
		public WtNode transform(
				ParserConfig config,
				WtImStartTag o,
				WtImEndTag c,
				WtNodeList body)
		{
			WtBold e = new WtBold(body);
			if (config.isGatherRtData())
			{
				String r0 = (o == null || o.isSynthetic()) ? null : "'''";
				String r1 = (c == null || c.isSynthetic()) ? null : "'''";
				e.setRtd(r0, WtRtData.SEP, r1);
			}
			return e;
		}
	},
	
	PARAGRAPH
	{
		@Override
		public String getElementName()
		{
			return "@p";
		}
		
		@Override
		public WtNode transform(
				ParserConfig config,
				WtImStartTag open,
				WtImEndTag close,
				WtNodeList body)
		{
			WtParagraph e = new WtParagraph(body);
			return e;
		}
	};
	
	public abstract String getElementName();
	
	public abstract WtNode transform(
			ParserConfig config,
			WtImStartTag open,
			WtImEndTag close,
			WtNodeList body);
	
	public WtNode createOpen(boolean synthetic)
	{
		return new WtImStartTag(this, synthetic);
	}
	
	public WtNode createClose(boolean synthetic)
	{
		return new WtImEndTag(this, synthetic);
	}
}
