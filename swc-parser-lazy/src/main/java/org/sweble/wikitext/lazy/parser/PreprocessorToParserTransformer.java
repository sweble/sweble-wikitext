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

import org.sweble.wikitext.lazy.preprocessor.Ignored;
import org.sweble.wikitext.lazy.preprocessor.LazyPreprocessedPage;
import org.sweble.wikitext.lazy.preprocessor.OnlyInclude;
import org.sweble.wikitext.lazy.preprocessor.PreprocessedWikitext;
import org.sweble.wikitext.lazy.preprocessor.ProtectedText;
import org.sweble.wikitext.lazy.preprocessor.XmlComment;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.EntityMap;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;

public class PreprocessorToParserTransformer
{
	public static PreprocessedWikitext transform(
			LazyPreprocessedPage preprocessedArticle,
			EntityMap entityMap)
	{
		return new PreprocessedWikitext(
				(String) new TransformVisitor(entityMap, false).go(preprocessedArticle),
				entityMap);
	}
	
	public static PreprocessedWikitext transform(
			LazyPreprocessedPage preprocessedArticle)
	{
		EntityMap entityMap = preprocessedArticle.getEntityMap();
		return new PreprocessedWikitext(
				(String) new TransformVisitor(entityMap, false).go(preprocessedArticle),
				entityMap);
	}
	
	public static PreprocessedWikitext transform(
			LazyPreprocessedPage preprocessedArticle,
			EntityMap entityMap,
			boolean trim)
	{
		return new PreprocessedWikitext(
				(String) new TransformVisitor(entityMap, trim).go(preprocessedArticle),
				entityMap);
	}
	
	public static PreprocessedWikitext transform(
			LazyPreprocessedPage preprocessedArticle,
			boolean trim)
	{
		EntityMap entityMap = preprocessedArticle.getEntityMap();
		return new PreprocessedWikitext(
				(String) new TransformVisitor(entityMap, trim).go(preprocessedArticle),
				entityMap);
	}
	
	// =========================================================================
	
	protected static final class TransformVisitor
			extends
				AstVisitor
	{
		private StringBuilder builder;
		
		private EntityMap entityMap;
		
		private final boolean trim;
		
		public TransformVisitor(EntityMap entityMap, boolean trim)
		{
			this.entityMap = entityMap;
			this.trim = trim;
		}
		
		// =====================================================================
		
		@Override
		protected boolean before(AstNode node)
		{
			builder = new StringBuilder();
			return super.before(node);
		}
		
		@Override
		protected String after(AstNode node, Object result)
		{
			return builder.toString();
		}
		
		// =====================================================================
		
		public void visit(LazyPreprocessedPage n)
		{
			iterate(n);
		}
		
		public void visit(AstNode n)
		{
			makeParserEntity(n);
		}
		
		public void visit(ProtectedText n)
		{
			makeParserEntity(n);
		}
		
		private void makeParserEntity(AstNode n)
		{
			int id = entityMap.registerEntity(n);
			builder.append('\uE000');
			builder.append(id);
			builder.append('\uE001');
		}
		
		public void visit(NodeList n)
		{
			iterate(n);
		}
		
		public void visit(OnlyInclude n)
		{
			iterate(n);
		}
		
		public void visit(Text n)
		{
			builder.append(n.getContent());
		}
		
		public void visit(Ignored n)
		{
			// Always trimmed
		}
		
		public void visit(XmlComment n)
		{
			if (!trim)
			{
				visit((AstNode) n);
			}
		}
	}
}
