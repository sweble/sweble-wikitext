package org.sweble.wikitext.engine.astwom;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.astwom.AttributeDescriptor.Normalization;
import org.sweble.wikitext.engine.astwom.adapters.NativeOrXmlAttributeAdapter;
import org.sweble.wikitext.engine.wom.WomAttr;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.lazy.utils.XmlAttribute;

import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.utils.Utils;

public class AttrAdapter
		extends
			AttributeSupportingElement
		implements
			WomAttr
{
	private static final long serialVersionUID = 1L;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private AttributeManagerBase attribManager = AttributeManagerBase.emptyManager();
	
	// =========================================================================
	
	public AttrAdapter(String name)
	{
		super(new XmlAttribute(name, new NodeList(), false));
		setName(name);
		setAttrValue("");
	}
	
	public AttrAdapter(XmlAttribute attrib)
	{
		super(attrib);
		setNameUnchecked(attrib.getName());
		setAttrValueUnchecked(attrib.getValue());
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "attr";
	}
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.ELEMENT;
	}
	
	// =========================================================================
	
	@Override
	public String getName()
	{
		return getAttribute("name");
	}
	
	@Override
	public String setName(String name) throws IllegalArgumentException, NullPointerException
	{
		NativeOrXmlAttributeAdapter old = setAttribute(
				Attributes.name, "name", name);
		
		return old == null ? null : old.getValue();
	}
	
	private void setNameUnchecked(String name)
	{
		setAttributeUnchecked(
				Attributes.name,
				"name",
				name);
	}
	
	@Override
	public String getAttrValue()
	{
		return getAttribute("value");
	}
	
	@Override
	public String setAttrValue(String value) throws NullPointerException
	{
		NativeOrXmlAttributeAdapter old = setAttribute(
				Attributes.value, "value", value);
		
		return old == null ? null : old.getValue();
	}
	
	private void setAttrValueUnchecked(NodeList value)
	{
		String normalized;
		if (value != null)
		{
			normalized = AttributeSupportingElement.normalize(
					Normalization.CDATA,
					value);
		}
		else
		{
			// The name itself should always be normalized
			normalized = getName();
		}
		
		setAttributeUnchecked(
				Attributes.value,
				"value",
				normalized);
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(String name)
	{
		Attributes d = Utils.fromString(Attributes.class, name);
		if (d != null)
			return d;
		// No other attributes are allowed.
		return null;
	}
	
	private static enum Attributes implements AttributeDescriptor
	{
		name
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				Toolbox.checkValidXmlName(value);
				return value;
			}
			
			@Override
			public Normalization getNormalizationMode()
			{
				return Normalization.NON_CDATA;
			}
		},
		
		value
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return value;
			}
			
			@Override
			public Normalization getNormalizationMode()
			{
				return Normalization.CDATA;
			}
		};
		
		@Override
		public boolean isRemovable()
		{
			return false;
		}
		
		@Override
		public boolean syncToAst()
		{
			return true;
		}
		
		@Override
		public void customAction(
				WomNode parent,
				NativeOrXmlAttributeAdapter oldAttr,
				NativeOrXmlAttributeAdapter newAttr)
		{
		}
	}
}
