package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>Url</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * Protocol ::= [A-Za-z] [A-Za-z0-9+\-.]*
 * </p>
 * </li>
 * <li>
 * <p>
 * Path ::= [^\u0000- \u007F\uE000\u2028\u2029\u0085\"\[\]<>|]+
 * </p>
 * </li>
 * <li>
 * <p>
 * Url ::= Scheme ':' Path
 * </p>
 * </li>
 * </ul>
 */
public class Url
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public Url()
	{
		super();
		
	}
	
	public Url(String protocol, String path)
	{
		super();
		setProtocol(protocol);
		setPath(path);
		
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_URL;
	}
	
	// =========================================================================
	// Properties
	
	private String protocol;
	
	public final String getProtocol()
	{
		return this.protocol;
	}
	
	public final String setProtocol(String protocol)
	{
		String old = this.protocol;
		this.protocol = protocol;
		return old;
	}
	
	private String path;
	
	public final String getPath()
	{
		return this.path;
	}
	
	public final String setPath(String path)
	{
		String old = this.path;
		this.path = path;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 2 + getSuperPropertyCount();
	}
	
	public final int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new WtLeafNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return Url.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "protocol";
					case 1:
						return "path";
						
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
						return Url.this.getProtocol();
					case 1:
						return Url.this.getPath();
						
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
						return Url.this.setProtocol((String) value);
					case 1:
						return Url.this.setPath((String) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
