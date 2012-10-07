package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.nodes.WtImageLink.ImageLinkTarget;
import org.sweble.wikitext.parser.nodes.WtLinkTarget.LinkTargetType;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class WtLinkOptionLinkTarget
		extends
			WtInnerNode1
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtLinkOptionLinkTarget()
	{
		super((WtNode) null);
	}
	
	public WtLinkOptionLinkTarget(ImageLinkTarget type)
	{
		super(type.getTarget());
		setTargetType(type.getTargetType());
	}
	
	@Override
	public int getNodeType()
	{
		return NT_LINK_OPTION_LINK_TARGET;
	}
	
	// =========================================================================
	// Properties
	
	private LinkTargetType targetType;
	
	public final LinkTargetType getTargetType()
	{
		return this.targetType;
	}
	
	public final LinkTargetType setTargetType(LinkTargetType targetType)
	{
		if (targetType == null)
			throw new NullPointerException();
		LinkTargetType old = this.targetType;
		this.targetType = targetType;
		return old;
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
				return WtLinkOptionLinkTarget.this.getPropertyCount();
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
						return WtLinkOptionLinkTarget.this.getTargetType();
						
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
						return WtLinkOptionLinkTarget.this.setTargetType((LinkTargetType) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
	
	// =========================================================================
	// Children
	
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
