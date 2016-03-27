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

import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;

public interface AstTextUtils
{

	/**
	 * When converting an AST to text, the conversion will abort with a
	 * StringConversionException if it encounters an unresolved XML entity
	 * reference. If a partial conversion is performed, the conversion will stop
	 * at a unresolved XML entity reference.
	 */
	public static final int FAIL_ON_UNRESOLVED_ENTITY_REF = 1;

	public static final int AST_TO_TEXT_LAST_OPTION = 1;

	// =========================================================================

	public String astToText(WtNode node) throws StringConversionException;

	public String astToText(WtNode node, int... options) throws StringConversionException;

	public PartialConversion astToTextPartial(WtNode node);

	public PartialConversion astToTextPartial(WtNode node, int... options);

	// =========================================================================

	public interface PartialConversion
	{
		/**
		 * Concatenated text of the first nodes which could be converted.
		 */
		public String getText();

		/**
		 * The remaining nodes, the first of which stopped the conversion.
		 */
		public WtNodeList getTail();
	}

}
