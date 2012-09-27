package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.LeafNode;
import de.fau.cs.osr.ptk.common.ast.RtDataPtk;

public abstract class WtLeafNode
		extends
			LeafNode
		implements
			WikitextNode
{
	
	private static final long serialVersionUID = -2024251471331960556L;
	
	private RtDataPtk rtd = null;
	
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
				return WtLeafNode.this.getPropertyCount();
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
						return WtLeafNode.this.getRtd();
						
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
						return WtLeafNode.this.setRtd((Object[]) value);
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}
	
}
