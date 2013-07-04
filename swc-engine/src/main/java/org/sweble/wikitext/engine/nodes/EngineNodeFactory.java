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
package org.sweble.wikitext.engine.nodes;

import java.util.List;

import org.sweble.wikitext.engine.lognodes.EngineLog;
import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactory;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtXmlElement;

import de.fau.cs.osr.ptk.common.Warning;

public interface EngineNodeFactory
		extends
			WikitextNodeFactory
{
	EngNowiki nowiki(String text);
	
	EngSoftErrorNode softError(String message);
	
	EngSoftErrorNode softError(WtNode pfn);
	
	EngSoftErrorNode softError(WtNodeList content, Exception e);
	
	EngCompiledPage compiledPage(
			EngPage page,
			List<Warning> warnings,
			EngineLog log);
	
	EngCompiledPage compiledPage(
			EngPage page,
			List<Warning> warnings,
			WtEntityMap entityMap,
			EngineLog log);
	
	EngPage page(WtNodeList content);
	
	// --[ Modification ]-------------------------------------------------------
	
	<T extends WtXmlElement> T addCssClass(T elem, String cssClass);
}
