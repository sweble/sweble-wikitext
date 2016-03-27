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

package org.sweble.wikitext.parser.comparer;

import java.util.HashSet;
import java.util.Set;

import org.sweble.wikitext.parser.nodes.WtBody;
import org.sweble.wikitext.parser.nodes.WtContentNode.WtEmptyContentNode;
import org.sweble.wikitext.parser.nodes.WtLinkOptions;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtTemplateArguments;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.comparer.AstComparer;
import de.fau.cs.osr.utils.ComparisonException;
import de.fau.cs.osr.utils.DeepComparer;
import de.fau.cs.osr.utils.DeepComparerDelegate;

public class WtComparer
{
	private static DeepComparerDelegate delegate;

	/**
	 * Compare two AST subtrees for equality.
	 * 
	 * Property and attribute values are compared using the <code>equals</code>
	 * method.
	 * 
	 * @param rootA
	 *            First subtree.
	 * @param rootB
	 *            Second subtree.
	 * @param compareAttributes
	 *            Whether to include the node attributes in the comparison.
	 */
	public static void compareAndThrow(
			AstNode<?> rootA,
			AstNode<?> rootB,
			boolean compareAttributes,
			boolean compareLocation) throws ComparisonException
	{
		makeComparer(compareAttributes, compareLocation).compare(rootA, rootB);
	}

	public static boolean compareNoThrow(
			AstNode<?> rootA,
			AstNode<?> rootB,
			boolean compareAttributes,
			boolean compareLocation)
	{
		try
		{
			compareAndThrow(rootA, rootB, compareAttributes, compareLocation);
			return true;
		}
		catch (ComparisonException e)
		{
			return false;
		}
	}

	public synchronized static DeepComparer makeComparer(
			boolean compareAttributes,
			boolean compareLocation)
	{
		if (delegate == null)
			delegate = new EmptyContentNodeComparer();

		DeepComparer comparer = AstComparer.makeComparer(
				compareAttributes,
				compareLocation);

		comparer.addComparer(delegate);
		return comparer;
	}

	public static class EmptyContentNodeComparer
			implements
				DeepComparerDelegate
	{
		private final Set<WtNode> emptyImmutables = new HashSet<WtNode>();

		public EmptyContentNodeComparer()
		{
			emptyImmutables.add(WtBody.EMPTY);
			emptyImmutables.add(WtLinkOptions.EMPTY);
			emptyImmutables.add(WtNodeList.EMPTY);
			emptyImmutables.add(WtTemplateArguments.EMPTY);
			emptyImmutables.add(WtXmlAttributes.EMPTY);
		}

		@Override
		public boolean compare(Object a, Object b, DeepComparer comparer) throws ComparisonException
		{
			if (emptyImmutables.contains(a))
			{
				if (!((WtEmptyContentNode) a).equals(b))
					throw new ComparisonException(a, b);
				return true;
			}
			else if (emptyImmutables.contains(b))
			{
				if (((WtEmptyContentNode) b).equals(a))
					throw new ComparisonException(a, b);
				return true;
			}
			else
				return false;
		}
	}
}
