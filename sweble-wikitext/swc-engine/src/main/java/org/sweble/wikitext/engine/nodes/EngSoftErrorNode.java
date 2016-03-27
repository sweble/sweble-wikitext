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

import org.sweble.wikitext.parser.nodes.WtBody;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes;
import org.sweble.wikitext.parser.nodes.WtXmlElement;

public class EngSoftErrorNode
		extends
			WtXmlElement
		implements
			EngNode
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected EngSoftErrorNode()
	{
		super();
	}

	protected EngSoftErrorNode(
			String name,
			WtXmlAttributes xmlAttributes,
			WtBody body)
	{
		super(name, xmlAttributes, body);
	}

	protected EngSoftErrorNode(String name, WtXmlAttributes xmlAttributes)
	{
		super(name, xmlAttributes);
	}

	@Override
	public int getNodeType()
	{
		return NT_SOFT_ERROR;
	}
}
