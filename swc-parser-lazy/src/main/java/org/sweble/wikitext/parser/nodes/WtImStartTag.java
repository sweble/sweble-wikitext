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

import org.sweble.wikitext.parser.postprocessor.IntermediateTags;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class WtImStartTag
		extends
			WtXmlStartTag
{
	private static final long serialVersionUID = 1L;
	
	//private final IntermediateTags type;
	
	// =========================================================================
	
	/* No default ctor. This node should not get de-/serialized
	 */
	
	protected WtImStartTag(IntermediateTags type)
	{
		super(type.getElementName(), WtXmlAttributes.EMPTY);
		//this.type = type;
		this.synthetic = false;
	}
	
	protected WtImStartTag(IntermediateTags type, boolean synthetic)
	{
		super(type.getElementName(), WtXmlAttributes.EMPTY);
		//this.type = type;
		this.synthetic = synthetic;
	}
	
	// =========================================================================
	
	@Override
	public int getNodeType()
	{
		return NT_IM_START_TAG;
	}
	
	// =========================================================================
	
	//	public IntermediateTags getType()
	//	{
	//		return type;
	//	}
	
	// =========================================================================
	// Properties
	
	private final boolean synthetic;
	
	@Override
	public boolean isSynthetic()
	{
		return synthetic;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 1 + getSuperPropertyCount();
	}
	
	private final int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new WtXmlStartTagPropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtImStartTag.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "synthetic";
						
					default:
						return super.getName(index);
				}
			}
			
			@Override
			protected Object getValue(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return WtImStartTag.this.isSynthetic();
						
					default:
						return super.getValue(index);
				}
			}
			
			@Override
			protected Object setValue(int index, Object value)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						throw new UnsupportedOperationException();
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
