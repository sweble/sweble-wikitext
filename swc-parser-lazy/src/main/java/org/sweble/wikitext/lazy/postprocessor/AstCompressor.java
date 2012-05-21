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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.sweble.wikitext.lazy.parser.LazyParsedPage;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.Warning;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.Location;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;

public class AstCompressor
		extends
			AstVisitor
{
	public static AstNode process(AstNode a)
	{
		return (AstNode) new AstCompressor().go(a);
	}
	
	/**
	 * @deprecated Will be removed
	 */
	public static LazyParsedPage process(LazyParsedPage a)
	{
		return (LazyParsedPage) new AstCompressor().go(a);
	}
	
	// =========================================================================
	
	@Override
	protected Object after(AstNode node, Object result)
	{
		return node;
	}
	
	// =========================================================================
	
	public void visit(AstNode n)
	{
		iterate(n);
	}
	
	public void visit(NodeList n)
	{
		ListIterator<AstNode> i = n.listIterator();
		while (i.hasNext())
		{
			AstNode current = i.next();
			if (current.getNodeType() == AstNode.NT_TEXT)
			{
				if (i.hasNext())
				{
					AstNode next = i.next();
					if (next.getNodeType() == AstNode.NT_TEXT)
					{
						i.previous();
						i.previous();
						compress(i, current.getNativeLocation());
					}
					else
					{
						dispatch(current);
						dispatch(next);
					}
				}
			}
			else
			{
				dispatch(current);
			}
		}
	}
	
	private void compress(ListIterator<AstNode> i, Location location)
	{
		String ct = "";
		HashMap<String, Object> ca = null;
		
		while (i.hasNext())
		{
			AstNode n = i.next();
			if (n.getNodeType() != AstNode.NT_TEXT)
			{
				i.previous();
				break;
			}
			
			Text t = (Text) n;
			
			ct += t.getContent();
			
			Map<String, Object> attrs = t.getAttributes();
			if (!attrs.isEmpty())
			{
				if (ca == null)
				{
					ca = new HashMap<String, Object>(attrs);
				}
				else
				{
					mergeAttributes(ca, attrs);
				}
			}
			
			i.remove();
		}
		
		Text text = new Text(ct);
		if (ca != null)
			text.setAttributes(ca);
		if (location != null)
			text.setNativeLocation(location);
		
		i.add(text);
	}
	
	@SuppressWarnings("unchecked")
	private void mergeAttributes(
			HashMap<String, Object> ca,
			Map<String, Object> attrs)
	{
		for (Entry<String, Object> attr : attrs.entrySet())
		{
			Object old = ca.put(attr.getKey(), attr.getValue());
			if (old != null)
			{
				if (attr.getKey() == "warnings")
				{
					List<Warning> w0 = (List<Warning>) old;
					List<Warning> w1 = (List<Warning>) attr.getValue();
					
					ArrayList<Warning> wNew = new ArrayList<Warning>(
							w0.size() + w1.size());
					
					wNew.addAll(w0);
					wNew.addAll(w1);
					
					ca.put(attr.getKey(), wNew);
				}
				else
				{
					// FIXME: Better: just cancel merging ...
					throw new UnsupportedOperationException(
							"Merging of text nodes would overwrite attributes!");
				}
			}
		}
	}
}
