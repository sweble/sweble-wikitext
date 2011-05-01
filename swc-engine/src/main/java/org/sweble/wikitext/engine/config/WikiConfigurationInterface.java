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

package org.sweble.wikitext.engine.config;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.TagExtensionBase;
import org.sweble.wikitext.lazy.ParserConfigInterface;

public interface WikiConfigurationInterface
        extends
            Serializable,
            ParserConfigInterface
{
	public Namespace getNamespace(String name);
	
	public Namespace getTemplateNamespace();
	
	public Namespace getNamespace(int id);
	
	public Namespace getDefaultNamespace();
	
	// --------
	
	public Interwiki getInterwiki(String prefix);
	
	public Interwiki getLocalInterwiki();
	
	// --------
	
	public MagicWord getMagicWord(String name);
	
	// --------
	
	public ParserFunctionBase getParserFunction(String name);
	
	// --------
	
	public Collection<TagExtensionBase> getTagExtensions();
	
	public TagExtensionBase getTagExtension(String name);
	
	// --------
	
	public HashSet<String> getAllowedHtmlTags();
	
	public HashSet<String> getEmptyOnlyHtmlTags();
	
	public HashSet<String> getPropagatableHtmlTags();
	
	// --------
	
	public String getWikiUrl();
}
