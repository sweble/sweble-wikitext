package org.sweble.wikitext.engine.lognodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class PreprocessorLog
		extends
			LogContainer
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public PreprocessorLog()
	{
		super();
		
	}
	
	// =========================================================================
	// Properties
	
	private boolean forInclusion;
	
	public final boolean getForInclusion()
	{
		return this.forInclusion;
	}
	
	public final boolean setForInclusion(boolean forInclusion)
	{
		boolean old = this.forInclusion;
		this.forInclusion = forInclusion;
		return old;
	}
	
	private Long timeNeeded;
	
	public final Long getTimeNeeded()
	{
		return this.timeNeeded;
	}
	
	public final Long setTimeNeeded(Long timeNeeded)
	{
		Long old = this.timeNeeded;
		this.timeNeeded = timeNeeded;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 2;
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new AstNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return 2;
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index)
				{
					case 0:
						return "forInclusion";
					case 1:
						return "timeNeeded";
						
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
						return PreprocessorLog.this.getForInclusion();
					case 1:
						return PreprocessorLog.this.getTimeNeeded();
						
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
						return PreprocessorLog.this.setForInclusion((Boolean) value);
					case 1:
						return PreprocessorLog.this.setTimeNeeded((Long) value);
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}
}
