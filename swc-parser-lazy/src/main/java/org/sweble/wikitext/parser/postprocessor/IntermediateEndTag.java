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

package org.sweble.wikitext.parser.postprocessor;

import org.sweble.wikitext.parser.parser.XmlEndTag;

public class IntermediateEndTag
        extends
            XmlEndTag
        implements
            IntermediateTag
{
	private static final long serialVersionUID = 1L;
	
	private final IntermediateTags type;
	
	private final boolean synthetic;
	
	// =========================================================================
	
	public IntermediateEndTag(IntermediateTags type)
	{
		super(type.getElementName());
		
		this.type = type;
		this.synthetic = false;
	}
	
	public IntermediateEndTag(IntermediateTags type, boolean synthetic)
	{
		super(type.getElementName());
		
		this.type = type;
		this.synthetic = synthetic;
	}
	
	// =========================================================================
	
	@Override
	public boolean isSynthetic()
	{
		return synthetic;
	}
	
	public IntermediateTags getType()
	{
		return type;
	}
}
