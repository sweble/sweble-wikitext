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

package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.WtRtData;

import de.fau.cs.osr.ptk.common.ast.AstParserEntity;
import de.fau.cs.osr.ptk.common.ast.Uninitialized;

public class WtParserEntity
		extends
			AstParserEntity<WtNode>
		implements
			WtNode, /* keep this here since WtIntermediate is only a "signal" interface. */
			WtIntermediate
{
	private static final long serialVersionUID = 7333107598118095040L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtParserEntity()
	{
		super(Uninitialized.X);
	}

	protected WtParserEntity(int id)
	{
		super(id);
	}

	// =========================================================================

	@Override
	public void setRtd(WtRtData rtd)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRtd(Object... glue)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRtd(String... glue)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public WtRtData getRtd()
	{
		return null;
	}

	@Override
	public void clearRtd()
	{
	}

	@Override
	public void suppressRtd()
	{
		throw new UnsupportedOperationException();
	}
}
