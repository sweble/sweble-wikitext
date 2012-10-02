package org.sweble.wikitext.engine;

import java.util.List;

import org.sweble.wikitext.engine.log.CompilerLog;
import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.nodes.WtInnerNode1;

import de.fau.cs.osr.ptk.common.Warning;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>CompiledPage Node</h1>
 */
public class CompiledPage
		extends
			WtInnerNode1

{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public CompiledPage()
	{
		super(new Page());
		
	}
	
	public CompiledPage(Page page, List<Warning> warnings, CompilerLog log)
	{
		super(page);
		setWarnings(warnings);
		setLog(log);
		
	}
	
	public CompiledPage(
			Page page,
			List<Warning> warnings,
			WtEntityMap entityMap,
			CompilerLog log)
	{
		super(page);
		setWarnings(warnings);
		setEntityMap(entityMap);
		setLog(log);
		
	}
	
	// =========================================================================
	// Properties
	
	private CompilerLog log;
	
	public final CompilerLog getLog()
	{
		return this.log;
	}
	
	public final CompilerLog setLog(CompilerLog log)
	{
		CompilerLog old = this.log;
		this.log = log;
		return old;
	}
	
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
		return 3;
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new AstNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return 3;
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index)
				{
					case 0:
						return "log";
					case 1:
						return "warnings";
					case 2:
						return "entityMap";
						
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
						return CompiledPage.this.getLog();
					case 1:
						return CompiledPage.this.getWarnings();
					case 2:
						return CompiledPage.this.getEntityMap();
						
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
						return CompiledPage.this.setLog((CompilerLog) value);
					case 1:
						return CompiledPage.this.setWarnings((List<Warning>) value);
					case 2:
						return CompiledPage.this.setEntityMap((WtEntityMap) value);
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}
	
	// =========================================================================
	// Children
	
	public final void setPage(Page page)
	{
		set(0, page);
	}
	
	public final Page getPage()
	{
		return (Page) get(0);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "page" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
	
	// =========================================================================
	
}
