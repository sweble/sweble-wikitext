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

package org.sweble.wikitext.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import de.fau.cs.osr.ptk.common.VisitingException;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.ParserEntity;
import de.fau.cs.osr.ptk.common.ast.Text;

public class PtkAstVisitor
{
	public final Object go(AstNode node)
	{
		return dispatch(node);
	}
	
	// =========================================================================
	
	/**
	 * Dispatches to the appropriate visit() method and returns the result of
	 * the visitation. If the given node is <code>null</code> this method
	 * returns immediately with <code>null</code> as result.
	 */
	protected final Object dispatch(AstNode node)
	{
		try
		{
			if (node == null)
				return null;
			
			return resolveAndVisit(node, node.getNodeType());
		}
		catch (VisitingException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new VisitingException(node, e);
		}
	}
	
	protected final void iterate(AstNode node)
	{
		if (node != null)
		{
			for (AstNode n : node)
				dispatch(n);
		}
	}
	
	protected final List<Object> map(AstNode node)
	{
		if (node == null)
			return Collections.emptyList();
		
		List<Object> result = new ArrayList<Object>(node.size());
		for (AstNode n : node)
			result.add(dispatch(n));
		return result;
	}
	
	/**
	 * Iterates over the children of an AST node and replaces each child node
	 * with the result of the visitation of the respective child. If the given
	 * AST node is a NodeList, the call will be passed to mapInPlace(NodeList)
	 * which has special semantics.
	 */
	protected final void mapInPlace(AstNode node)
	{
		if (node == null)
		{
			return;
		}
		else if (node.getNodeType() == AstNode.NT_NODE_LIST)
		{
			mapInPlace((NodeList) node);
		}
		else
		{
			ListIterator<AstNode> i = node.listIterator();
			while (i.hasNext())
			{
				AstNode current = i.next();
				AstNode result = (AstNode) dispatch(current);
				if (result != current)
					i.set(result);
			}
		}
	}
	
	/**
	 * Iterates over a NodeList and replaces each node with the result of the
	 * visitation. If a visit() call returns <code>null</code>, the node that
	 * was passed to the visit() method will be removed from the list. If the
	 * visit() call returns another NodeList as result, the result's children
	 * will replace the node that was being visited. The visitation will
	 * continue after the embedded children. Otherwise, each visited node will
	 * simply be replaced by the result of the visit() call.
	 */
	protected final void mapInPlace(NodeList list)
	{
		if (list == null)
			return;
		
		ListIterator<AstNode> i = list.listIterator();
		while (i.hasNext())
		{
			AstNode current = i.next();
			
			AstNode result = (AstNode) dispatch(current);
			if (result == current)
				continue;
			
			if (result == null)
			{
				i.remove();
			}
			else if (result.getNodeType() == AstNode.NT_NODE_LIST)
			{
				i.remove();
				i.add(result);
			}
			else
			{
				i.set(result);
			}
		}
	}
	
	// =========================================================================
	
	protected Object resolveAndVisit(AstNode node, int type) throws Exception
	{
		switch (type)
		{
			case AstNode.NT_NODE_LIST:
				return visit((NodeList) node);
			case AstNode.NT_TEXT:
				return visit((Text) node);
			case AstNode.NT_PARSER_ENTITY:
				return visit((ParserEntity) node);
			default:
				return visitUnspecific(node);
		}
	}
	
	// =========================================================================
	
	protected Object visitUnspecific(AstNode node) throws Exception
	{
		return node;
	}
	
	protected Object visit(Text node) throws Exception
	{
		return visitUnspecific(node);
	}
	
	protected Object visit(NodeList node) throws Exception
	{
		return visitUnspecific(node);
	}
	
	protected Object visit(ParserEntity node) throws Exception
	{
		return visitUnspecific(node);
	}
}
