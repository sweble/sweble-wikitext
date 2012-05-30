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

package org.sweble.wikitext.engine.astwom;

import org.sweble.wikitext.engine.astwom.adapters.NativeOrXmlAttributeAdapter;
import org.sweble.wikitext.engine.wom.WomNode;

public enum UniversalAttributes implements AttributeDescriptor
{
	// ==[ Core ]===============================================================
	
	ID
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return CoreAttributes.ID.verify(parent, value);
		}
	},
	CLASS
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return CoreAttributes.CLASS.verify(parent, value);
		}
	},
	STYLE
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return CoreAttributes.STYLE.verify(parent, value);
		}
	},
	
	TITLE
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return CoreAttributes.TITLE.verify(parent, value);
		}
	},
	
	// ==[ I18N ]===============================================================
	
	DIR
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return I18nAttributes.DIR.verify(parent, value);
		}
	},
	LANG
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return I18nAttributes.LANG.verify(parent, value);
		}
	},
	XMLLANG
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return I18nAttributes.XMLLANG.verify(parent, value);
		}
	},
	
	// ==[ Event ]==============================================================
	
	ONCLICK
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return EventAttributes.ONCLICK.verify(parent, value);
		}
	},
	ONDBLCLICK
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return EventAttributes.ONDBLCLICK.verify(parent, value);
		}
	},
	ONMOUSEDOWN
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return EventAttributes.ONMOUSEDOWN.verify(parent, value);
		}
	},
	ONMOUSEUP
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return EventAttributes.ONMOUSEUP.verify(parent, value);
		}
	},
	ONMOUSEOVER
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return EventAttributes.ONMOUSEOVER.verify(parent, value);
		}
	},
	ONMOUSEMOVE
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return EventAttributes.ONMOUSEMOVE.verify(parent, value);
		}
	},
	ONMOUSEOUT
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return EventAttributes.ONMOUSEOUT.verify(parent, value);
		}
	},
	ONKEYPRESS
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return EventAttributes.ONKEYPRESS.verify(parent, value);
		}
	},
	ONKEYDOWN
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return EventAttributes.ONKEYDOWN.verify(parent, value);
		}
	},
	ONKEYUP
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return EventAttributes.ONKEYUP.verify(parent, value);
		}
	};
	
	// =========================================================================
	
	@Override
	public String verify(WomNode parent, String value) throws IllegalArgumentException
	{
		return value;
	}
	
	@Override
	public boolean isRemovable()
	{
		return true;
	}
	
	@Override
	public boolean syncToAst()
	{
		return true;
	}
	
	@Override
	public Normalization getNormalizationMode()
	{
		return Normalization.NON_CDATA;
	}
	
	@Override
	public void customAction(
			WomNode parent,
			NativeOrXmlAttributeAdapter oldAttr,
			NativeOrXmlAttributeAdapter newAttr)
	{
	}
}
