package org.sweble.wikitext.engine.astwom.adapters;

import lombok.AccessLevel;
import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.astwom.AstToWomNodeFactory;
import org.sweble.wikitext.engine.astwom.AttributeDescriptor;
import org.sweble.wikitext.engine.astwom.AttributeManagerBase;
import org.sweble.wikitext.engine.astwom.ChildManagerBase;
import org.sweble.wikitext.engine.astwom.GenericAttributeDescriptor;
import org.sweble.wikitext.engine.astwom.NativeOrXmlElement;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.astwom.UniversalAttributes;
import org.sweble.wikitext.engine.wom.WomUniversalAttributes;
import org.sweble.wikitext.lazy.parser.XmlElement;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.ContentNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.utils.Utils;

public abstract class NativeOrXmlElementWithUniversalAttributes
		extends
			NativeOrXmlElement
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Delegate(types = { WomUniversalAttributes.class })
	private AttributeManagerBase attribManager = AttributeManagerBase.emptyManager();
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private ChildManagerBase childManager = ChildManagerBase.emptyManager();
	
	// =========================================================================
	
	protected NativeOrXmlElementWithUniversalAttributes(AstNode astNode)
	{
		super(astNode);
	}
	
	protected NativeOrXmlElementWithUniversalAttributes(
			FullElementContentType contentType,
			AstToWomNodeFactory factory,
			ContentNode astNode)
	{
		super(astNode);
		addContent(contentType, factory, astNode.getContent());
	}
	
	protected NativeOrXmlElementWithUniversalAttributes(
			FullElementContentType contentType,
			String tagName,
			AstToWomNodeFactory factory,
			XmlElement astNode)
	{
		super(tagName, astNode);
		addAttributes(astNode.getXmlAttributes());
		addContent(contentType, factory, astNode.getBody());
	}
	
	// =========================================================================
	
	@Override
	public NodeList getAstChildContainer()
	{
		return isXml() ? xml().getBody() : ((ContentNode) getAstNode()).getContent();
	}
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(String name)
	{
		UniversalAttributes d = Utils.fromString(UniversalAttributes.class, name);
		if (d != null)
			return d;
		return GenericAttributeDescriptor.get();
	}
	
	@Override
	protected XmlElement convertToXmlElement()
	{
		return Toolbox.addRtData(new XmlElement(
				getNodeName(),
				false,
				new NodeList(),
				getAstChildContainer()));
	}
}
