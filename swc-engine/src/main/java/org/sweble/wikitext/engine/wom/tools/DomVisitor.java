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
package org.sweble.wikitext.engine.wom.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sweble.wikitext.engine.wom.WomAttribute;
import org.sweble.wikitext.engine.wom.WomNode;

import de.fau.cs.osr.ptk.common.VisitorBase;

public class DomVisitor
        extends
            VisitorBase<WomNode>
{
	@Override
	protected Object dispatch(WomNode node)
	{
		if (node == null)
			return null;
		return resolveAndVisit(node);
	}
	
	protected final void iterateChildren(WomNode node)
	{
		if (node != null)
		{
			WomNode i = node.getFirstChild();
			while (i != null)
			{
				dispatch(i);
				i = i.getNextSibling();
			}
		}
	}
	
	protected final void iterateAttributes(WomNode node)
	{
		if (node != null)
		{
			for (WomAttribute attr : node.getAttributes())
				dispatch(attr);
		}
	}
	
	protected final List<Object> map(WomNode node)
	{
		if (node == null)
			return Collections.emptyList();
		
		List<Object> result = new ArrayList<Object>();
		WomNode i = node.getFirstChild();
		while (i != null)
		{
			result.add(dispatch(i));
			i = i.getNextSibling();
		}
		return result;
	}
}
