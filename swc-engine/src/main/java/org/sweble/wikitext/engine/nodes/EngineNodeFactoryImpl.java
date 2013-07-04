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

import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.lognodes.EngineLog;
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
	public EngineNodeFactoryImpl(WikiConfig wikiConfig)
	{
		super(wikiConfig.getParserConfig());
		
		{
			Map<Class<?>, WtNode> prototypes = super.getPrototypes();
			prototypes.put(EngCompiledPage.class, new EngCompiledPage());
			prototypes.put(EngNowiki.class, new EngNowiki());
			prototypes.put(EngPage.class, new EngPage());
			prototypes.put(EngSoftErrorNode.class, new EngSoftErrorNode());
		}
		
		{
			Map<NamedMemberId, Object> defaultValueImmutables = super.getDefaultValueImmutables();
			defaultValueImmutables.put(new NamedMemberId(EngSoftErrorNode.class, "body"), WtBody.EMPTY);
		}
	}
	
	// =========================================================================
	
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
						"class",
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
	public EngCompiledPage compiledPage(
			EngPage page,
			List<Warning> warnings,
			EngineLog log)
	{
		return new EngCompiledPage(page, warnings, log);
	}
	
	@Override
	public EngCompiledPage compiledPage(
			EngPage page,
			List<Warning> warnings,
			WtEntityMap entityMap,
			EngineLog log)
	{
		return new EngCompiledPage(page, warnings, entityMap, log);
	}
	
	@Override
	public EngPage page(WtNodeList content)
	{
		return new EngPage(content);
	}
	
	// =========================================================================
	
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
