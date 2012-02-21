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

package org.sweble.wikitext.lazy.parser;

import java.io.Serializable;
import java.util.Arrays;

import org.sweble.wikitext.lazy.utils.RtWikitextPrinter;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.utils.StringUtils;

public class RtData
		implements
			Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final Object[][] rts;
	
	// =========================================================================
	
	public RtData(Object[][] rts)
	{
		this.rts = rts;
	}
	
	// =========================================================================
	
	public Object[][] getRts()
	{
		return rts;
	}
	
	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append("RtData: ");
		int i = 0;
		for (Object[] part : rts)
		{
			if (i != 0)
				b.append(", ");
			b.append('[');
			b.append(i++);
			if (part != null)
			{
				b.append("] = \"");
				for (Object o : part)
				{
					if (o == null)
						continue;
					
					String s;
					if (o instanceof AstNode)
					{
						s = RtWikitextPrinter.print((AstNode) o);
					}
					else
					{
						s = o.toString();
					}
					b.append(StringUtils.escJava(s));
				}
				b.append('"');
			}
			else
			{
				b.append(']');
			}
		}
		return b.toString();
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(rts);
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RtData other = (RtData) obj;
		if (rts == null)
		{
			if (other.rts != null)
				return false;
		}
		else
		{
			if (rts.length != other.rts.length)
				return false;
			
			for (int i = 0; i < rts.length; ++i)
			{
				Object[] t = this.rts[i];
				Object[] o = other.rts[i];
				if (!Arrays.equals(t, o))
					return false;
			}
		}
		return true;
	}
	
	public static RtData build(String... data)
	{
		Object[][] rtd = new Object[data.length][];
		
		int i = 0;
		for (Object o : data)
		{
			if (o != null)
				rtd[i++] = new Object[] { o };
		}
		
		return new RtData(rtd);
	}
}
