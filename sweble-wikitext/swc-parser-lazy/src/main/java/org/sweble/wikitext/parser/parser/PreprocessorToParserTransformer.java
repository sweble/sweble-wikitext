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

package org.sweble.wikitext.parser.parser;

import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.nodes.WtIgnored;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtOnlyInclude;
import org.sweble.wikitext.parser.nodes.WtPreproWikitextPage;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtXmlComment;
import org.sweble.wikitext.parser.preprocessor.PreprocessedWikitext;

import de.fau.cs.osr.ptk.common.AstVisitor;

public class PreprocessorToParserTransformer
{
	public static PreprocessedWikitext transform(
			WtPreproWikitextPage preprocessedArticle)
	{
		return transform(preprocessedArticle, false);
	}

	public static PreprocessedWikitext transform(
			WtPreproWikitextPage preprocessedArticle,
			boolean trim)
	{
		TransformVisitor tv = new TransformVisitor(trim);
		return (PreprocessedWikitext) tv.go(preprocessedArticle);
	}

	// =========================================================================

	protected static final class TransformVisitor
			extends
				AstVisitor<WtNode>
	{
		private StringBuilder builder;

		private WtEntityMap entityMap;

		private final boolean trim;

		// =====================================================================

		public TransformVisitor(boolean trim)
		{
			this.trim = trim;
		}

		// =====================================================================

		@Override
		protected WtNode before(WtNode node)
		{
			builder = new StringBuilder();
			return super.before(node);
		}

		@Override
		protected PreprocessedWikitext after(WtNode node, Object result)
		{
			return new PreprocessedWikitext(builder.toString(), entityMap);
		}

		// =====================================================================

		public void visit(WtPreproWikitextPage n)
		{
			entityMap = n.getEntityMap();
			iterate(n);
		}

		public void visit(WtNodeList n)
		{
			iterate(n);
		}

		public void visit(WtOnlyInclude n)
		{
			iterate(n);
		}

		public void visit(WtText n)
		{
			builder.append(n.getContent());
		}

		// =====================================================================

		public void visit(WtNode n)
		{
			makeParserEntity(n);
		}

		public void visit(WtIgnored n)
		{
			if (!trim)
				makeParserEntity(n);
		}

		public void visit(WtXmlComment n)
		{
			if (!trim)
				makeParserEntity(n);
		}

		// =====================================================================

		private void makeParserEntity(WtNode n)
		{
			int id = entityMap.registerEntity(n);
			builder.append('\uE000');
			builder.append(id);
			builder.append('\uE001');
		}
	}
}
