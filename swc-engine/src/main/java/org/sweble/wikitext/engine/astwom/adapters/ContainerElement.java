package org.sweble.wikitext.engine.astwom.adapters;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.astwom.AstToWomNodeFactory;
import org.sweble.wikitext.engine.astwom.AttributeDescriptor;
import org.sweble.wikitext.engine.astwom.ChildManagerBase;
import org.sweble.wikitext.engine.astwom.FullElement;
import org.sweble.wikitext.engine.wom.WomNodeType;

import de.fau.cs.osr.ptk.common.ast.NodeList;

public abstract class ContainerElement
		extends
			FullElement
{
	private static final long serialVersionUID = 1L;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private ChildManagerBase childManager = ChildManagerBase.emptyManager();
	
	// =========================================================================
	
	public ContainerElement()
	{
		super(new NodeList());
	}
	
	public ContainerElement(AstToWomNodeFactory womNodeFactory, NodeList content)
	{
		super(content);
		
		if (content == null)
			throw new NullPointerException();
		
		if (!content.isEmpty())
			addContent(
					womNodeFactory,
					content,
					getChildManagerForModificationOrFail(),
					false);
	}
	
	// =========================================================================
	
	@Override
	public NodeList getAstNode()
	{
		return (NodeList) super.getAstNode();
	}
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.ELEMENT;
	}
	
	// =========================================================================
	
	@Override
	public NodeList getAstChildContainer()
	{
		return getAstNode();
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(String name)
	{
		return null;
	}
}
