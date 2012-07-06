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

package org.sweble.wikitext.lazy.postprocessor;

import org.sweble.wikitext.lazy.ParserConfig;
import org.sweble.wikitext.lazy.parser.Bold;
import org.sweble.wikitext.lazy.parser.Italics;
import org.sweble.wikitext.lazy.parser.Paragraph;
import org.sweble.wikitext.lazy.utils.TextUtils;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

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
		public AstNode transform(
		        ParserConfig config,
		        ImTagOpen o,
		        ImTagClose c,
		        NodeList body)
		{
			Italics e = new Italics(body);
			if (config.isGatherRtData())
			{
				Object[] r0 = (o == null || o.isSynthetic()) ? null : TextUtils.joinRt("''");
				Object[] r1 = (c == null || c.isSynthetic()) ? null : TextUtils.joinRt("''");
				TextUtils.addRtData(e, r0, r1);
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
		public AstNode transform(
		        ParserConfig config,
		        ImTagOpen o,
		        ImTagClose c,
		        NodeList body)
		{
			Bold e = new Bold(body);
			if (config.isGatherRtData())
			{
				Object[] r0 = (o == null || o.isSynthetic()) ? null : TextUtils.joinRt("'''");
				Object[] r1 = (c == null || c.isSynthetic()) ? null : TextUtils.joinRt("'''");
				TextUtils.addRtData(e, r0, r1);
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
		public AstNode transform(
		        ParserConfig config,
		        ImTagOpen open,
		        ImTagClose close,
		        NodeList body)
		{
			Paragraph e = new Paragraph(body);
			return e;
		}
	};
	
	public abstract String getElementName();
	
	public abstract AstNode transform(
	        ParserConfig config,
	        ImTagOpen open,
	        ImTagClose close,
	        NodeList body);
	
	public AstNode createOpen(boolean synthetic)
	{
		return new ImTagOpen(this, synthetic);
	}
	
	public AstNode createClose(boolean synthetic)
	{
		return new ImTagClose(this, synthetic);
	}
}
