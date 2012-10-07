package org.sweble.wikitext.engine.log;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class ParseException
		extends
			LogLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public ParseException()
	{
	}
	
	public ParseException(String message)
	{
		setMessage(message);
	}
	
	// =========================================================================
	// Properties
	
	private String message;
	
	public final String getMessage()
	{
		return this.message;
	}
	
	public final String setMessage(String message)
	{
		String old = this.message;
		this.message = message;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 1;
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new AstNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return 1;
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index)
				{
					case 0:
						return "message";
						
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
						return ParseException.this.getMessage();
						
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
						return ParseException.this.setMessage((String) value);
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}
}
