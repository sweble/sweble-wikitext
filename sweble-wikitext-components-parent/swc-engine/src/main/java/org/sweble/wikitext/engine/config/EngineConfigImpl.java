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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(
		name = "EngineConfig",
		namespace = "org.sweble.wikitext.engine")
@XmlType(propOrder = {
		"trimTransparentBeforeParsing" })
@XmlAccessorType(XmlAccessType.NONE)
public class EngineConfigImpl
		implements
			EngineConfig
{
	@XmlElement()
	private boolean trimTransparentBeforeParsing;

	// =========================================================================

	@Override
	public boolean isTrimTransparentBeforeParsing()
	{
		return trimTransparentBeforeParsing;
	}

	public void setTrimTransparentBeforeParsing(
			boolean trimTransparentBeforeParsing)
	{
		this.trimTransparentBeforeParsing = trimTransparentBeforeParsing;
	}

	// =========================================================================

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (trimTransparentBeforeParsing ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EngineConfigImpl other = (EngineConfigImpl) obj;
		if (trimTransparentBeforeParsing != other.trimTransparentBeforeParsing)
			return false;
		return true;
	}
}
