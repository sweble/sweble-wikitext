package org.sweble.wikitext.parser.nodes;

import java.util.List;

import org.sweble.wikitext.parser.WtEntityMap;

import de.fau.cs.osr.ptk.common.Warning;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>Grammar for MediaWiki Wikitext</h1> <h2>Grammar</h2>
 */
public class WtPage
		extends
			WtContentNodeMarkTwo
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtPage()
	{
	}
	
	public WtPage(WtNodeList content, List<Warning> warnings)
	{
		super(content);
		setWarnings(warnings);
	}
	
	public WtPage(
			WtNodeList content,
			List<Warning> warnings,
			WtEntityMap entityMap)
	{
		super(content);
		setWarnings(warnings);
		setEntityMap(entityMap);
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_PARSED_WIKITEXT_PAGE;
	}
	
	// =========================================================================
	// Properties
	
	private List<Warning> warnings;
	
	public final List<Warning> getWarnings()
	{
		return this.warnings;
	}
	
	public final List<Warning> setWarnings(List<Warning> warnings)
	{
		List<Warning> old = this.warnings;
		this.warnings = warnings;
		return old;
	}
	
	private WtEntityMap entityMap;
	
	public final WtEntityMap getEntityMap()
	{
		return this.entityMap;
	}
	
	public final WtEntityMap setEntityMap(WtEntityMap entityMap)
	{
		WtEntityMap old = this.entityMap;
		this.entityMap = entityMap;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 2 + getSuperPropertyCount();
	}
	
	public int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new WtContentNodeMarkTwoPropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtPage.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "warnings";
					case 1:
						return "entityMap";
						
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
						return WtPage.this.getWarnings();
					case 1:
						return WtPage.this.getEntityMap();
						
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
					{
						@SuppressWarnings("unchecked")
						List<Warning> list = (List<Warning>) value;
						return WtPage.this.setWarnings(list);
					}
					case 1:
						return WtPage.this.setEntityMap((WtEntityMap) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
