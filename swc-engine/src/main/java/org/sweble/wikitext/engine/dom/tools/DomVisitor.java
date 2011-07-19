package org.sweble.wikitext.engine.dom.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sweble.wikitext.engine.dom.DomAttribute;
import org.sweble.wikitext.engine.dom.DomNode;

import de.fau.cs.osr.ptk.common.VisitorBase;

public class DomVisitor
        extends
            VisitorBase<DomNode>
{
	@Override
	protected Object dispatch(DomNode node)
	{
		if (node == null)
			return null;
		return resolveAndVisit(node);
	}
	
	protected final void iterateChildren(DomNode node)
	{
		if (node != null)
		{
			DomNode i = node.getFirstChild();
			while (i != null)
			{
				dispatch(i);
				i = i.getNextSibling();
			}
		}
	}
	
	protected final void iterateAttributes(DomNode node)
	{
		if (node != null)
		{
			for (DomAttribute attr : node.getAttributes())
				dispatch(attr);
		}
	}
	
	protected final List<Object> map(DomNode node)
	{
		if (node == null)
			return Collections.emptyList();
		
		List<Object> result = new ArrayList<Object>();
		DomNode i = node.getFirstChild();
		while (i != null)
		{
			result.add(dispatch(i));
			i = i.getNextSibling();
		}
		return result;
	}
}
