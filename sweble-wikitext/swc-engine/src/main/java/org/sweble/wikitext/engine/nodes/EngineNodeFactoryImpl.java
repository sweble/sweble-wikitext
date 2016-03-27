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
import java.util.Map;

import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactoryImpl;
import org.sweble.wikitext.parser.nodes.WtBody;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtXmlAttribute;
import org.sweble.wikitext.parser.nodes.WtXmlElement;

import de.fau.cs.osr.ptk.common.Warning;

public final class EngineNodeFactoryImpl
		extends
			WikitextNodeFactoryImpl
		implements
			EngineNodeFactory
{
	public EngineNodeFactoryImpl(ParserConfig parserConfig)
	{
		super(parserConfig);

		{
			Map<Class<?>, WtNode> prototypes = super.getPrototypes();
			prototypes.put(EngProcessedPage.class, new EngProcessedPage());
			prototypes.put(EngNowiki.class, new EngNowiki());
			prototypes.put(EngPage.class, new EngPage());
			prototypes.put(EngSoftErrorNode.class, new EngSoftErrorNode());
		}

		{
			Map<NamedMemberId, Object> defaultValueImmutables = super.getDefaultValueImmutables();
			defaultValueImmutables.put(new NamedMemberId(EngSoftErrorNode.class, "body"), WtBody.EMPTY);
		}
	}

	// --[ Wikitext specific ]--------------------------------------------------

	@Override
	public EngNowiki nowiki(String text)
	{
		return new EngNowiki(text);
	}

	@Override
	public EngSoftErrorNode softError(String message)
	{
		return softError(text(message));
	}

	@Override
	public EngSoftErrorNode softError(WtNode content)
	{
		return new EngSoftErrorNode(
				"strong",
				attrs(list(attr(
						name(list(text("class"))),
						value(list(text("error")))))),
				body(list(content)));
	}

	@Override
	public EngSoftErrorNode softError(WtNodeList content, Exception e)
	{
		EngSoftErrorNode error = softError(content);
		error.setAttribute("exception", e);
		return error;
	}

	@Override
	public EngProcessedPage processedPage(
			EngPage page,
			EngLogProcessingPass log,
			List<Warning> warnings)
	{
		return new EngProcessedPage(page, log, warnings);
	}

	@Override
	public EngProcessedPage processedPage(
			EngPage page,
			EngLogProcessingPass log,
			List<Warning> warnings,
			WtEntityMap entityMap)
	{
		return new EngProcessedPage(page, log, warnings, entityMap);
	}

	@Override
	public EngPage page(WtNodeList content)
	{
		return new EngPage(content);
	}

	// --[ Log nodes ]----------------------------------------------------------

	@Override
	public EngLogExpansionPass logExpansionPass()
	{
		return new EngLogExpansionPass();
	}

	@Override
	public EngLogParserPass logParserPass()
	{
		return new EngLogParserPass();
	}

	@Override
	public EngLogPostprocessorPass logPostprocessorPass()
	{
		return new EngLogPostprocessorPass();
	}

	@Override
	public EngLogPreprocessorPass logPreprocessorPass()
	{
		return new EngLogPreprocessorPass();
	}

	@Override
	public EngLogProcessingPass logProcessingPass()
	{
		return new EngLogProcessingPass();
	}

	@Override
	public EngLogValidatorPass logValidatorPass()
	{
		return new EngLogValidatorPass();
	}

	@Override
	public EngLogMagicWordResolution logMagicWordResolution(
			String name,
			boolean success)
	{
		return new EngLogMagicWordResolution(name, success);
	}

	@Override
	public EngLogParameterResolution logParameterResolution(
			String name,
			boolean success)
	{
		return new EngLogParameterResolution(name, success);
	}

	@Override
	public EngLogParserFunctionResolution logParserFunctionResolution(
			String name,
			boolean success)
	{
		return new EngLogParserFunctionResolution(name, success);
	}

	@Override
	public EngLogRedirectResolution logRedirectResolution(
			String target,
			boolean success)
	{
		return new EngLogRedirectResolution(target, success);
	}

	@Override
	public EngLogTagExtensionResolution logTagExtensionResolution(
			String target,
			boolean success)
	{
		return new EngLogTagExtensionResolution(target, success);
	}

	@Override
	public EngLogTransclusionResolution logTransclusionResolution(
			String target,
			boolean success)
	{
		return new EngLogTransclusionResolution(target, success);
	}

	@Override
	public EngLogParserError logParserError(String message)
	{
		return new EngLogParserError(message);
	}

	@Override
	public EngLogUnhandledError logUnhandledError(
			Throwable exception,
			String dump)
	{
		return new EngLogUnhandledError(exception, dump);
	}

	// --[ Modification ]-------------------------------------------------------

	@Override
	public <T extends WtXmlElement> T addCssClass(T elem, String cssClass)
	{
		if (elem.getXmlAttributes().isEmpty())
			elem.setXmlAttributes(attrs(list()));

		for (WtNode attr : elem.getXmlAttributes())
		{
			if (attr == null || !(attr instanceof WtXmlAttribute))
				continue;

			WtXmlAttribute a = (WtXmlAttribute) attr;
			if (!a.getName().equals("class"))
				continue;

			if (!a.hasValue())
				a.setValue(value(list()));
			a.getValue().add(text(" " + cssClass));
		}

		return elem;
	}
}
