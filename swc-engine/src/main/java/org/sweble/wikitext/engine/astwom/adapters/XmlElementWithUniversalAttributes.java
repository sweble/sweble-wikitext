package org.sweble.wikitext.engine.astwom.adapters;

import lombok.AccessLevel;
import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.astwom.AstToWomNodeFactory;
import org.sweble.wikitext.engine.astwom.AttributeDescriptor;
import org.sweble.wikitext.engine.astwom.AttributeManagerBase;
import org.sweble.wikitext.engine.astwom.GenericAttributeDescriptor;
import org.sweble.wikitext.engine.astwom.UniversalAttributes;
import org.sweble.wikitext.engine.wom.WomUniversalAttributes;
import org.sweble.wikitext.lazy.parser.XmlElement;

import de.fau.cs.osr.utils.Utils;

public abstract class XmlElementWithUniversalAttributes
		extends
			XmlElementWithChildren
{
	private static final long serialVersionUID = 1L;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Delegate(types = { WomUniversalAttributes.class })
	private AttributeManagerBase attribManager = AttributeManagerBase.emptyManager();
	
	// =========================================================================
	
	public XmlElementWithUniversalAttributes(String tagName)
	{
		super(tagName);
	}
	
	public XmlElementWithUniversalAttributes(
			FullElementContentType contentType,
			String tagName,
			AstToWomNodeFactory womNodeFactory,
			XmlElement astNode)
	{
		super(contentType, tagName, womNodeFactory, astNode);
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(String name)
	{
		UniversalAttributes d = Utils.fromString(UniversalAttributes.class, name);
		if (d != null)
			return d;
		return GenericAttributeDescriptor.get();
	}
}
