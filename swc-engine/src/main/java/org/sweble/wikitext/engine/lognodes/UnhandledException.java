package org.sweble.wikitext.engine.lognodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class UnhandledException
		extends
			LogLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public UnhandledException()
	{
	}
	
	public UnhandledException(Throwable exception, String dump)
	{
		setException(exception);
		setDump(dump);
	}
	
	// =========================================================================
	// Properties
	
	private Throwable exception;
	
	public final Throwable getException()
	{
		return this.exception;
	}
	
	public final Throwable setException(Throwable exception)
	{
		Throwable old = this.exception;
		this.exception = exception;
		return old;
	}
	
	private String dump;
	
	public final String getDump()
	{
		return this.dump;
	}
	
	public final String setDump(String dump)
	{
		String old = this.dump;
		this.dump = dump;
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
						return "exception";
					case 1:
						return "dump";
						
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
						return UnhandledException.this.getException();
					case 1:
						return UnhandledException.this.getDump();
						
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
						return UnhandledException.this.setException((Throwable) value);
					case 1:
						return UnhandledException.this.setDump((String) value);
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}
}
