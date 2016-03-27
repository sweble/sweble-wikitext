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
	// --[ Wikitext specific ]--------------------------------------------------

	EngNowiki nowiki(String text);

	EngSoftErrorNode softError(String message);

	EngSoftErrorNode softError(WtNode pfn);

	EngSoftErrorNode softError(WtNodeList content, Exception e);

	EngProcessedPage processedPage(
			EngPage page,
			EngLogProcessingPass log,
			List<Warning> warnings);

	EngProcessedPage processedPage(
			EngPage page,
			EngLogProcessingPass log,
			List<Warning> warnings,
			WtEntityMap entityMap);

	EngPage page(WtNodeList content);

	// --[ Log nodes ]----------------------------------------------------------

	EngLogExpansionPass logExpansionPass();

	EngLogParserPass logParserPass();

	EngLogPostprocessorPass logPostprocessorPass();

	EngLogPreprocessorPass logPreprocessorPass();

	EngLogProcessingPass logProcessingPass();

	EngLogValidatorPass logValidatorPass();

	EngLogMagicWordResolution logMagicWordResolution(
			String name,
			boolean success);

	EngLogParameterResolution logParameterResolution(
			String name,
			boolean success);

	EngLogParserFunctionResolution logParserFunctionResolution(
			String name,
			boolean success);

	EngLogRedirectResolution logRedirectResolution(
			String target,
			boolean success);

	EngLogTagExtensionResolution logTagExtensionResolution(
			String target,
			boolean success);

	EngLogTransclusionResolution logTransclusionResolution(
			String target,
			boolean success);

	EngLogParserError logParserError(String message);

	EngLogUnhandledError logUnhandledError(Throwable exception, String dump);

	// --[ Modification ]-------------------------------------------------------

	<T extends WtXmlElement> T addCssClass(T elem, String cssClass);
}
