package org.sweble.wikitext.engine.astwom.adapters;

import lombok.AccessLevel;
import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.astwom.AstToWomNodeFactory;
import org.sweble.wikitext.engine.astwom.AttributeDescriptor;
import org.sweble.wikitext.engine.astwom.AttributeManagerBase;
import org.sweble.wikitext.engine.astwom.ChildManagerBase;
import org.sweble.wikitext.engine.astwom.FullElement;
import org.sweble.wikitext.engine.astwom.GenericAttributeDescriptor;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.astwom.UniversalAttributes;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.engine.wom.WomUniversalAttributes;
import org.sweble.wikitext.lazy.parser.XmlElement;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.utils.Utils;

public abstract class XmlElementWithUniversalAttributes
		extends
			FullElement
{
	private static final long serialVersionUID = 1L;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Delegate(types = { WomUniversalAttributes.class })
	private AttributeManagerBase attribManager = AttributeManagerBase.emptyManager();
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private ChildManagerBase childManager = ChildManagerBase.emptyManager();
	
	// =========================================================================
	
	public XmlElementWithUniversalAttributes(String tagName)
	{
		super(Toolbox.addRtData(new XmlElement(
				tagName,
				true,
				new NodeList(),
				new NodeList())));
	}
	
	public XmlElementWithUniversalAttributes(
			String tagName,
			AstToWomNodeFactory womNodeFactory,
			XmlElement astNode)
	{
		super(astNode);
		
		if (astNode == null)
			throw new NullPointerException();
		
		if (!astNode.getName().equalsIgnoreCase(tagName))
			throw new IllegalArgumentException("Given XmlElement node is not a `" + tagName + "' element!");
		
		addAttributes(astNode.getXmlAttributes());
		addContent(womNodeFactory, astNode.getBody());
	}
	
	// =========================================================================
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.ELEMENT;
	}
	
	@Override
	public XmlElement getAstNode()
	{
		return (XmlElement) super.getAstNode();
	}
	
	// =========================================================================
	
	@Override
	protected void appendToAst(NodeList container, AstNode child)
	{
		if (container.isEmpty())
		{
			XmlElement e = getAstNode();
			e.setEmpty(false);
			Toolbox.addRtData(e);
		}
		
		super.appendToAst(container, child);
	}
	
	@Override
	protected void removeFromAst(NodeList container, AstNode removeNode)
	{
		super.removeFromAst(container, removeNode);
		
		if (container.isEmpty())
		{
			XmlElement e = getAstNode();
			e.setEmpty(true);
			Toolbox.addRtData(e);
		}
	}
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(String name)
	{
		UniversalAttributes d = Utils.fromString(UniversalAttributes.class, name);
		if (d != null)
			return d;
		return GenericAttributeDescriptor.get();
	}
}
