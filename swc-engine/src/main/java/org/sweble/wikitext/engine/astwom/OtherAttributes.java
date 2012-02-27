package org.sweble.wikitext.engine.astwom;

import java.util.regex.Pattern;

import org.sweble.wikitext.engine.astwom.adapters.NativeOrXmlAttributeAdapter;
import org.sweble.wikitext.engine.wom.WomNode;

import de.fau.cs.osr.utils.Utils;

public enum OtherAttributes implements AttributeDescriptor
{
	TEXT_ALIGN
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			if (!Utils.isOneOf(TEXT_ALIGN_EXPECTED, value))
				throw new MustBeOneOfException(TEXT_ALIGN_EXPECTED, value);
			
			return value;
		}
	},
	
	PIXELS
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			if (!PIXELS_EXPECTED.matcher(value).matches())
				throw new IllegalArgumentException("Expected value of type 'Pixels' but got: " + value);
			
			return value;
		}
	},
	
	LENGTH
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			if (!LENGTH_EXPECTED.matcher(value).matches())
				throw new IllegalArgumentException("Expected value of type 'Length' but got: " + value);
			
			return value;
		}
	},
	
	LCR_ALIGN
	{
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			if (!Utils.isOneOf(HR_ALIGN_EXPECTED, value))
				throw new MustBeOneOfException(HR_ALIGN_EXPECTED, value);
			
			return value;
		}
	};
	
	// =========================================================================
	
	static final Pattern LENGTH_EXPECTED =
			Pattern.compile("[-+]?(\\d+|\\d+(\\.\\d+)?%)");
	
	static final Pattern PIXELS_EXPECTED =
			Pattern.compile("-0+|\\+?\\d+");
	
	static final String[] HR_ALIGN_EXPECTED =
			new String[] { "left", "center", "right" };
	
	static final String[] TEXT_ALIGN_EXPECTED =
			new String[] { "left", "center", "right", "justify" };
	
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
	public void customAction(WomNode parent, NativeOrXmlAttributeAdapter oldAttr, NativeOrXmlAttributeAdapter newAttr)
	{
	}
}
