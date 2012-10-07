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

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.Uninitialized;

public class WtXmlComment
		extends
			WtStringNodeImpl
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtXmlComment()
	{
		super(Uninitialized.X);
	}
	
	public WtXmlComment(String content)
	{
		super(content);
		setPrefix("");
		setSuffix("");
	}
	
	public WtXmlComment(String content, String prefix, String suffix)
	{
		super(content);
		setPrefix(prefix);
		setSuffix(suffix);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_XML_COMMENT;
	}
	
	// =========================================================================
	// Properties
	
	private String prefix;
	
	public final String getPrefix()
	{
		return this.prefix;
	}
	
	public final String setPrefix(String prefix)
	{
		if (prefix == null)
			throw new NullPointerException();
		String old = this.prefix;
		this.prefix = prefix;
		return old;
	}
	
	private String suffix;
	
	public final String getSuffix()
	{
		return this.suffix;
	}
	
	public final String setSuffix(String suffix)
	{
		if (suffix == null)
			throw new NullPointerException();
		String old = this.suffix;
		this.suffix = suffix;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 2 + getSuperPropertyCount();
	}
	
	public final int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new WtStringContentNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtXmlComment.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "prefix";
					case 1:
						return "suffix";
						
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
						return WtXmlComment.this.getPrefix();
					case 1:
						return WtXmlComment.this.getSuffix();
						
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
						return WtXmlComment.this.setPrefix((String) value);
					case 1:
						return WtXmlComment.this.setSuffix((String) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
