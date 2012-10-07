package org.sweble.wikitext.engine.log;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class CompilerLog
		extends
			LogContainer
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public CompilerLog()
	{
		super();
		
	}
	
	// =========================================================================
	// Properties
	
	private String title;
	
	public final String getTitle()
	{
		return this.title;
	}
	
	public final String setTitle(String title)
	{
		String old = this.title;
		this.title = title;
		return old;
	}
	
	private Long revision;
	
	public final Long getRevision()
	{
		return this.revision;
	}
	
	public final Long setRevision(Long revision)
	{
		Long old = this.revision;
		this.revision = revision;
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
						return "title";
					case 1:
						return "revision";
						
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
						return CompilerLog.this.getTitle();
					case 1:
						return CompilerLog.this.getRevision();
						
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
						return CompilerLog.this.setTitle((String) value);
					case 1:
						return CompilerLog.this.setRevision((Long) value);
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}
}
