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

package org.sweble.wikitext.lazy;

import org.sweble.wikitext.lazy.parser.WarningSeverity;
import org.sweble.wikitext.lazy.postprocessor.ScopeType;
import org.sweble.wikitext.lazy.utils.XmlEntityResolver;

public interface ParserConfigInterface
        extends
            XmlEntityResolver
{
	public enum TargetType
	{
		INVALID,
		PAGE,
		IMAGE,
	}
	
	boolean isValidXmlEntityRef(String name);
	
	boolean isMagicWord(String word);
	
	boolean isUrlProtocol(String proto);
	
	boolean isWarningLevelEnabled(WarningSeverity severity);
	
	boolean isAutoCorrect();
	
	boolean isWarningsEnabled();
	
	boolean isGatherRtData();
	
	String getInternalLinkPrefixPattern();
	
	String getInternalLinkPostfixPattern();
	
	TargetType classifyTarget(String target);
	
	boolean isNamespace(String nsName);
	
	boolean isInterwikiName(String nsName);
	
	boolean isLocalInterwikiName(String nsName);
	
	boolean isValidExtensionTagName(String name);
	
	/**
	 * Returns the resolved value of an XML entity.
	 * 
	 * @param name
	 *            The name of the XML entity.
	 * @return The resolved value or <code>null</code> if the XML entity is
	 *         unknown.
	 */
	String resolveXmlEntity(String name);
	
	boolean isXmlElementAllowed(String name);
	
	boolean isXmlElementEmptyOnly(String name);
	
	ScopeType getXmlElementType(String name);
}
