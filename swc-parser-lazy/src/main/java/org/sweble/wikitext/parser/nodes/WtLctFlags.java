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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public interface WtLctFlags
		extends
			WtNode
{
	public static final WtLctNoFlags NO_FLAGS = new WtLctNoFlags();
	
	// =========================================================================
	
	public Set<String> getFlags();
	
	public Set<String> setFlags(Set<String> flags);
	
	public Set<String> getVariants();
	
	public Set<String> setVariants(Set<String> variants);
	
	public List<String> getGarbage();
	
	public List<String> setGarbage(List<String> garbage);
	
	// =========================================================================
	
	public class WtLctNoFlags
			extends
				WtEmptyImmutableNode
			implements
				WtLctFlags
	{
		private static final long serialVersionUID = 2465445739660029292L;
		
		@Override
		public int getNodeType()
		{
			return NT_LCT_FLAGS;
		}
		
		@Override
		public String getNodeName()
		{
			return WtLctFlags.class.getSimpleName();
		}
		
		@Override
		public boolean indicatesAbsence()
		{
			return true;
		}
		
		@Override
		public Set<String> getFlags()
		{
			return Collections.emptySet();
		}
		
		@Override
		public Set<String> setFlags(Set<String> flags)
		{
			throw new UnsupportedOperationException(genMsg());
		}
		
		@Override
		public Set<String> getVariants()
		{
			return Collections.emptySet();
		}
		
		@Override
		public Set<String> setVariants(Set<String> variants)
		{
			throw new UnsupportedOperationException(genMsg());
		}
		
		@Override
		public List<String> getGarbage()
		{
			return Collections.emptyList();
		}
		
		@Override
		public List<String> setGarbage(List<String> garbage)
		{
			throw new UnsupportedOperationException(genMsg());
		}
	}
	
	// =========================================================================
	
	public class WtLctFlagsImpl
			extends
				WtLeafNode
			implements
				WtLctFlags
	{
		private static final long serialVersionUID = 1L;
		
		// =====================================================================
		
		/**
		 * Only for use by de-serialization code.
		 */
		protected WtLctFlagsImpl()
		{
		}
		
		public WtLctFlagsImpl(
				Set<String> flags,
				Set<String> variants,
				List<String> garbage)
		{
			setFlags(flags);
			setVariants(variants);
			setGarbage(garbage);
		}
		
		@Override
		public int getNodeType()
		{
			return NT_LCT_FLAGS;
		}
		
		@Override
		public String getNodeName()
		{
			return WtLctFlags.class.getSimpleName();
		}
		
		// =====================================================================
		// Properties
		
		private Set<String> flags;
		
		public final Set<String> getFlags()
		{
			return this.flags;
		}
		
		public final Set<String> setFlags(Set<String> flags)
		{
			Set<String> old = this.flags;
			this.flags = flags;
			return old;
		}
		
		private Set<String> variants;
		
		public final Set<String> getVariants()
		{
			return this.variants;
		}
		
		public final Set<String> setVariants(Set<String> variants)
		{
			Set<String> old = this.variants;
			this.variants = variants;
			return old;
		}
		
		private List<String> garbage;
		
		public final List<String> getGarbage()
		{
			return this.garbage;
		}
		
		public final List<String> setGarbage(List<String> garbage)
		{
			List<String> old = this.garbage;
			this.garbage = garbage;
			return old;
		}
		
		@Override
		public final int getPropertyCount()
		{
			return 3 + getSuperPropertyCount();
		}
		
		private final int getSuperPropertyCount()
		{
			return super.getPropertyCount();
		}
		
		@Override
		public final AstNodePropertyIterator propertyIterator()
		{
			return new WtLeafNodePropertyIterator()
			{
				@Override
				protected int getPropertyCount()
				{
					return WtLctFlagsImpl.this.getPropertyCount();
				}
				
				@Override
				protected String getName(int index)
				{
					switch (index - getSuperPropertyCount())
					{
						case 0:
							return "flags";
						case 1:
							return "variants";
						case 2:
							return "garbage";
							
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
							return WtLctFlagsImpl.this.getFlags();
						case 1:
							return WtLctFlagsImpl.this.getVariants();
						case 2:
							return WtLctFlagsImpl.this.getGarbage();
							
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
						{
							@SuppressWarnings("unchecked")
							Set<String> flags = (Set<String>) value;
							return WtLctFlagsImpl.this.setFlags(flags);
						}
						case 1:
						{
							@SuppressWarnings("unchecked")
							Set<String> variants = (Set<String>) value;
							return WtLctFlagsImpl.this.setVariants(variants);
						}
						case 2:
						{
							@SuppressWarnings("unchecked")
							List<String> garbage = (List<String>) value;
							return WtLctFlagsImpl.this.setGarbage(garbage);
						}
						
						default:
							return super.setValue(index, value);
					}
				}
			};
		}
	}
}
