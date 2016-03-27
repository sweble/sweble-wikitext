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

import java.io.ObjectStreamException;

import org.sweble.wikitext.parser.nodes.WtLinkTarget.LinkTargetType;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.Uninitialized;

public interface WtLinkOptionLinkTarget
		extends
			WtNode
{
	public static final WtLinkOptionLinkTargetDefault DEFAULT =
			new WtLinkOptionLinkTargetDefault();

	// =========================================================================

	public LinkTargetType getTargetType();

	public void setTargetType(LinkTargetType targetType);

	public boolean hasTarget();

	public void setTarget(WtLinkTarget target);

	public WtLinkTarget getTarget();

	// =========================================================================

	public static final class WtLinkOptionLinkTargetDefault
			extends
				WtInnerImmutableNode1
			implements
				WtLinkOptionLinkTarget
	{
		private static final long serialVersionUID = -1064749733891892633L;

		// =====================================================================

		private WtLinkOptionLinkTargetDefault()
		{
			super(WtLinkTarget.NO_LINK);
		}

		// =====================================================================

		@Override
		public String getNodeName()
		{
			return WtLinkOptionLinkTarget.class.getSimpleName();
		}

		@Override
		public int getNodeType()
		{
			return NT_LINK_OPTION_LINK_TARGET;
		}

		protected Object readResolve() throws ObjectStreamException
		{
			return WtLinkOptionLinkTarget.DEFAULT;
		}

		// =====================================================================
		// Properties

		@Override
		public LinkTargetType getTargetType()
		{
			return LinkTargetType.DEFAULT;
		}

		@Override
		public void setTargetType(LinkTargetType targetType)
		{
			throw new UnsupportedOperationException(genMsg());
		}

		@Override
		public final int getPropertyCount()
		{
			return getSuperPropertyCount() + 1;
		}

		public final int getSuperPropertyCount()
		{
			return super.getPropertyCount();
		}

		@Override
		public final AstNodePropertyIterator propertyIterator()
		{
			return new WtInnerNode1PropertyIterator()
			{
				@Override
				protected int getPropertyCount()
				{
					return WtLinkOptionLinkTargetDefault.this.getPropertyCount();
				}

				@Override
				protected String getName(int index)
				{
					switch (index - getSuperPropertyCount())
					{
						case 0:
							return "targetType";

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
							return WtLinkOptionLinkTargetDefault.this.getTargetType();

						default:
							return super.getValue(index);
					}
				}

				@Override
				protected Object setValue(int index, Object value)
				{
					throw new UnsupportedOperationException(genMsg());
				}
			};
		}

		// =====================================================================
		// Children

		public final boolean hasTarget()
		{
			return false;
		}

		public final void setTarget(WtLinkTarget target)
		{
			throw new UnsupportedOperationException(genMsg());
		}

		public final WtLinkTarget getTarget()
		{
			return (WtLinkTarget) get(0);
		}

		@Override
		public String[] getChildNames()
		{
			return WtLinkOptionLinkTargetImpl.CHILD_NAMES;
		}
	}

	// =========================================================================

	public static final class WtLinkOptionLinkTargetImpl
			extends
				WtInnerNode1
			implements
				WtLinkOptionLinkTarget
	{
		private static final long serialVersionUID = 1L;

		// =====================================================================

		/**
		 * Only for use by de-serialization code.
		 */
		public WtLinkOptionLinkTargetImpl()
		{
			super(Uninitialized.X);
		}

		public WtLinkOptionLinkTargetImpl(
				WtLinkTarget target,
				LinkTargetType type)
		{
			super(target);
			setTargetType(type);
		}

		@Override
		public String getNodeName()
		{
			return WtLinkOptionLinkTarget.class.getSimpleName();
		}

		@Override
		public int getNodeType()
		{
			return NT_LINK_OPTION_LINK_TARGET;
		}

		// =====================================================================
		// Properties

		private LinkTargetType targetType;

		public final LinkTargetType getTargetType()
		{
			return this.targetType;
		}

		public final void setTargetType(LinkTargetType targetType)
		{
			if (targetType == null)
				throw new NullPointerException();
			this.targetType = targetType;
		}

		@Override
		public final int getPropertyCount()
		{
			return getSuperPropertyCount() + 1;
		}

		public final int getSuperPropertyCount()
		{
			return super.getPropertyCount();
		}

		@Override
		public final AstNodePropertyIterator propertyIterator()
		{
			return new WtInnerNode1PropertyIterator()
			{
				@Override
				protected int getPropertyCount()
				{
					return WtLinkOptionLinkTargetImpl.this.getPropertyCount();
				}

				@Override
				protected String getName(int index)
				{
					switch (index - getSuperPropertyCount())
					{
						case 0:
							return "targetType";

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
							return WtLinkOptionLinkTargetImpl.this.getTargetType();

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
							LinkTargetType old = WtLinkOptionLinkTargetImpl.this.getTargetType();
							WtLinkOptionLinkTargetImpl.this.setTargetType((LinkTargetType) value);
							return old;
						}

						default:
							return super.setValue(index, value);
					}
				}
			};
		}

		// =====================================================================
		// Children

		public final boolean hasTarget()
		{
			return getTarget() != WtLinkTarget.NO_LINK;
		}

		public final void setTarget(WtLinkTarget target)
		{
			set(0, target);
		}

		public final WtLinkTarget getTarget()
		{
			return (WtLinkTarget) get(0);
		}

		private static final String[] CHILD_NAMES = new String[] { "target" };

		public final String[] getChildNames()
		{
			return CHILD_NAMES;
		}
	}
}
