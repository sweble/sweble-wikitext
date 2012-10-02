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
import org.sweble.wikitext.parser.nodes.Bold;
import org.sweble.wikitext.parser.nodes.Italics;
import org.sweble.wikitext.parser.nodes.Paragraph;
import org.sweble.wikitext.parser.nodes.WikitextNode;
import org.sweble.wikitext.parser.nodes.WtList;

import de.fau.cs.osr.ptk.common.ast.RtDataPtk;

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
		public WikitextNode transform(
				ParserConfig config,
				IntermediateStartTag o,
				IntermediateEndTag c,
				WtList body)
		{
			Italics e = new Italics(body);
			if (config.isGatherRtData())
			{
				String r0 = (o == null || o.isSynthetic()) ? null : "''";
				String r1 = (c == null || c.isSynthetic()) ? null : "''";
				e.setRtd(r0, RtDataPtk.SEP, r1);
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
		public WikitextNode transform(
				ParserConfig config,
				IntermediateStartTag o,
				IntermediateEndTag c,
				WtList body)
		{
			Bold e = new Bold(body);
			if (config.isGatherRtData())
			{
				String r0 = (o == null || o.isSynthetic()) ? null : "'''";
				String r1 = (c == null || c.isSynthetic()) ? null : "'''";
				e.setRtd(r0, RtDataPtk.SEP, r1);
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
		public WikitextNode transform(
				ParserConfig config,
				IntermediateStartTag open,
				IntermediateEndTag close,
				WtList body)
		{
			Paragraph e = new Paragraph(body);
			return e;
		}
	};
	
	public abstract String getElementName();
	
	public abstract WikitextNode transform(
			ParserConfig config,
			IntermediateStartTag open,
			IntermediateEndTag close,
			WtList body);
	
	public WikitextNode createOpen(boolean synthetic)
	{
		return new IntermediateStartTag(this, synthetic);
	}
	
	public WikitextNode createClose(boolean synthetic)
	{
		return new IntermediateEndTag(this, synthetic);
	}
}
