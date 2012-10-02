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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.sweble.wikitext.parser.nodes.ParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtText;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.Warning;
import de.fau.cs.osr.ptk.common.ast.Location;

/**
 * Collapses adjacent WtText nodes into a single WtText node.
 */
public class AstCompressor
		extends
			AstVisitor<WtNode>
{
	public static WtNode process(WtNode a)
	{
		return (WtNode) new AstCompressor().go(a);
	}
	
	/**
	 * @deprecated Will be removed
	 */
	public static ParsedWikitextPage process(ParsedWikitextPage a)
	{
		return (ParsedWikitextPage) new AstCompressor().go(a);
	}
	
	// =========================================================================
	
	@Override
	protected Object after(WtNode node, Object result)
	{
		return node;
	}
	
	// =========================================================================
	
	public void visit(WtNode n)
	{
		iterate(n);
	}
	
	public void visit(WtNodeList n)
	{
		ListIterator<WtNode> i = n.listIterator();
		while (i.hasNext())
		{
			WtNode current = i.next();
			if (current.getNodeType() == WtNode.NT_TEXT)
			{
				if (i.hasNext())
				{
					WtNode next = i.next();
					if (next.getNodeType() == WtNode.NT_TEXT)
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
	
	private void compress(ListIterator<WtNode> i, Location location)
	{
		String ct = "";
		HashMap<String, Object> ca = null;
		
		while (i.hasNext())
		{
			WtNode n = i.next();
			if (n.getNodeType() != WtNode.NT_TEXT)
			{
				i.previous();
				break;
			}
			
			WtText t = (WtText) n;
			
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
		
		WtText text = new WtText(ct);
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
