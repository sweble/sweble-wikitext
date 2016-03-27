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

import java.io.IOException;
import java.io.Writer;

import org.sweble.wikitext.parser.nodes.WtNode;

import de.fau.cs.osr.ptk.common.PrinterInterface;

public final class TypedRtDataPrinter
		implements
			PrinterInterface
{
	@Override
	public String getPrintoutType()
	{
		return "wikitext";
	}

	@Override
	public void print(Object ast, Writer out) throws IOException
	{
		WtRtDataPrinter.print(out, (WtNode) ast);
	}
}
