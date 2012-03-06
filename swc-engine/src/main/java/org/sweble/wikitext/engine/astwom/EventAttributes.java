package org.sweble.wikitext.engine.astwom;

import org.sweble.wikitext.engine.astwom.adapters.NativeOrXmlAttributeAdapter;
import org.sweble.wikitext.engine.wom.WomNode;

public enum EventAttributes implements AttributeDescriptor
{
	ONCLICK
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return AttributeVerifiers.SCRIPT.verify(parent, value);
		}
	},
	
	ONDBLCLICK
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return AttributeVerifiers.SCRIPT.verify(parent, value);
		}
	},
	
	ONMOUSEDOWN
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return AttributeVerifiers.SCRIPT.verify(parent, value);
		}
	},
	
	ONMOUSEUP
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return AttributeVerifiers.SCRIPT.verify(parent, value);
		}
	},
	
	ONMOUSEOVER
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return AttributeVerifiers.SCRIPT.verify(parent, value);
		}
	},
	
	ONMOUSEMOVE
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return AttributeVerifiers.SCRIPT.verify(parent, value);
		}
	},
	
	ONMOUSEOUT
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return AttributeVerifiers.SCRIPT.verify(parent, value);
		}
	},
	
	ONKEYPRESS
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return AttributeVerifiers.SCRIPT.verify(parent, value);
		}
	},
	
	ONKEYDOWN
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return AttributeVerifiers.SCRIPT.verify(parent, value);
		}
	},
	
	ONKEYUP
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return AttributeVerifiers.SCRIPT.verify(parent, value);
		}
	};
	
	// =========================================================================
	
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
