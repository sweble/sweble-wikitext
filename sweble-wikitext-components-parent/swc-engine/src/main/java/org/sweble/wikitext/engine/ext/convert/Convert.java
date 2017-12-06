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

package org.sweble.wikitext.engine.ext.convert;

import de.fau.cs.osr.utils.StringTools;
import java.util.List;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngSoftErrorNode;
import org.sweble.wikitext.engine.nodes.EngineRtData;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.StringConversionException;


/**
 * @see https://en.wikipedia.org/wiki/Template:Convert/doc
 * @see https://en.wikipedia.org/wiki/Help:Convert
 */
public class Convert
		extends
			ParserFunctionBase
{
	public Convert()
	{
		super("convert");
	}

	public Convert(WikiConfig wikiConfig)
	{
		super(wikiConfig, "convert");
	}

	
	@Override
	public WtNode invoke(
			WtNode pnf,
			ExpansionFrame frame,
			List<? extends WtNode> args)
	{

		String format = expandArgToString(frame, args, 0);
		if (format == null)
			return error("Cannot convert format argument to string!");

		double value;
		if (args.size() >= 2)
		{
			String valueStr = expandArgToString(frame, args, 1);
			if (valueStr == null)
				return error("Cannot convert value argument to string!");
			
			try {
				value = Double.parseDouble(valueStr);
			} catch (Exception ex) {
				
			}
		}

		
		String unitSource = null;
		if (args.size() >= 3)
		{
			unitSource = expandArgToString(frame, args, 2);
			if (unitSource == null)
				return error("Cannot convert source unit argument to string!");
			
			Units src = null;
			for (Units ut : Units.values())
			{
				if (unitSource.equals(ut.getUnitCode()))
				{
					src = ut;
				}
			}

			if(src == null) {
				return error("Cannot convert source unit argument!");
			}
		}

		String unitDest = null;
		if (args.size() >= 4)
		{
			unitDest = expandArgToString(frame, args, 3);
			if (unitDest == null)
				return error("Cannot convert destination unit argument to string!");
			
			Units dest = null;
			for (Units ut : Units.values())
			{
				if (unitSource.equals(ut.getUnitCode()))
				{
					dest = ut;
				}
			}

			if(dest == null)
			{
				return error("Cannot convert destination unit argument!");
			}
		}

		// FIXME: WIP just a temporary test.
		return nf().text("42");
	}


	private String expandArgToString(
			ExpansionFrame preprocessorFrame,
			List<? extends WtNode> args,
			final int index)
	{
		WtNode arg = preprocessorFrame.expand(args.get(index));

		tu().trim(arg);

		String format = null;
		try
		{
			format = tu().astToText(arg).trim();
		} catch (StringConversionException e1)
		{
		}
		return format;
	}

	private EngSoftErrorNode error(String msg)
	{
		return EngineRtData.set(nf().softError(
				EngineRtData.set(nf().nowiki(StringTools.escHtml(msg)))));
	}
}