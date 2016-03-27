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
package org.sweble.wikitext.engine.utils;

import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.AstTextUtils;

public interface EngineAstTextUtils
		extends
			AstTextUtils
{
	public static final int DO_NOT_CONVERT_NOWIKI = AstTextUtils.AST_TO_TEXT_LAST_OPTION + 1;

	public static final int AST_TO_TEXT_LAST_OPTION = AstTextUtils.AST_TO_TEXT_LAST_OPTION + 2;

	public abstract WtNode trim(WtNode n);

	public abstract WtNode trimLeft(WtNode n);

	public abstract WtNode trimRight(WtNode n);
}
