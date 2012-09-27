package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.RtDataPtk;
import de.fau.cs.osr.ptk.common.ast.StringContentNode;

public abstract class WtStringContentNode
		extends
			StringContentNode
		implements
			WikitextNode
{
	
	private static final long serialVersionUID = -2087712873453224402L;
	
	private RtDataPtk rtd = null;
	
	// =========================================================================
	
	public WtStringContentNode()
	{
		super();
	}
	
	public WtStringContentNode(String content)
	{
		super(content);
	}
	
	// =========================================================================
	
	@Override
	public RtDataPtk setRtd(Object... glue)
	{
		rtd = new RtDataPtk(this, glue);
		return rtd;
	}
	
	@Override
	public RtDataPtk setRtd(String... glue)
	{
		rtd = new RtDataPtk(this, glue);
		return rtd;
	}
	
	@Override
	public RtDataPtk getRtd()
	{
		return rtd;
	}
	
	@Override
	public void clearRtd()
	{
		rtd = null;
	}
	
	// =========================================================================
	
	@Override
	public int getPropertyCount()
	{
		return 1;
	}
	
	@Override
	public AstNodePropertyIterator propertyIterator()
	{
		return new AstNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtStringContentNode.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index)
				{
					case 0:
						return "rtd";
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
			
			@Override
			protected Object getValue(int index)
			{
				switch (index)
				{
					case 0:
						return WtStringContentNode.this.getRtd();
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
			
			@Override
			protected Object setValue(int index, Object value)
			{
				switch (index)
				{
					case 0:
						return WtStringContentNode.this.setRtd((Object[]) value);
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}
	
}
