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

package org.sweble.wikitext.engine.ext;

import java.util.List;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.utils.ApplyToText;
import org.sweble.wikitext.engine.utils.EngineTextUtils;
import org.sweble.wikitext.lazy.preprocessor.Template;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public class ParserFunctionLc
		extends
			ParserFunctionBase
{
	private static final long serialVersionUID = 1L;
	
	public ParserFunctionLc()
	{
		super("lc");
	}
	
	@Override
	public AstNode invoke(
			Template template,
			ExpansionFrame preprocessorFrame,
			List<? extends AstNode> args)
	{
		if (args.size() < 1)
			return new NodeList();
		
		AstNode arg0 = preprocessorFrame.expand(args.get(0));
		
		EngineTextUtils.trim(arg0);
		
		new ApplyToText(new ApplyToText.Functor()
		{
			@Override
			public String apply(String text)
			{
				return text.toLowerCase();
			}
		}).go(arg0);
		
		return arg0;
	}
}
