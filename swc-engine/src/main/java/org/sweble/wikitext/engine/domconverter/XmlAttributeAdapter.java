package org.sweble.wikitext.engine.domconverter;

import java.util.Collection;
import java.util.Collections;

import org.sweble.wikitext.engine.dom.DomAttribute;
import org.sweble.wikitext.engine.dom.DomNodeType;
import org.sweble.wikitext.lazy.ParserConfigInterface;
import org.sweble.wikitext.lazy.utils.StringConversionException;
import org.sweble.wikitext.lazy.utils.StringConverter;
import org.sweble.wikitext.lazy.utils.XmlAttribute;

import de.fau.cs.osr.utils.FmtNotYetImplementedError;

public class XmlAttributeAdapter
        extends
            DomBackboneLeaf
        implements
            DomAttribute
{
	private static final long serialVersionUID = 1L;
	
	private final XmlAttribute astNode;
	
	private final String value;
	
	// =========================================================================
	
	public XmlAttributeAdapter(ParserConfigInterface resolver, XmlAttribute astNode)
	{
		if (astNode == null)
			throw new NullPointerException();
		this.astNode = astNode;
		
		try
		{
			int opt = StringConverter.RESOLVE_CHAR_REF |
			        StringConverter.RESOLVE_ENTITY_REF |
			        StringConverter.FAIL_ON_IGNORED |
			        StringConverter.FAIL_ON_XML_COMMENTS;
			
			value = StringConverter.convert(astNode.getValue(), resolver, opt);
		}
		catch (StringConversionException e)
		{
			throw new AssertionError();
		}
	}
	
	protected XmlAttribute getAstNode()
	{
		return astNode;
	}
	
	// =========================================================================
	
	@Override
	public String getName()
	{
		return astNode.getName();
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "Attribute";
	}
	
	@Override
	public DomNodeType getNodeType()
	{
		return DomNodeType.ATTRIBUTE;
	}
	
	// =========================================================================
	
	@Override
	public String getText()
	{
		return null;
	}
	
	@Override
	public String getValue()
	{
		return value;
	}
	
	// =========================================================================
	
	@Override
	public void appendText(String text) throws UnsupportedOperationException
	{
		throw new FmtNotYetImplementedError();
	}
	
	@Override
	public String deleteText(int from, int length) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		throw new FmtNotYetImplementedError();
	}
	
	@Override
	public void insertText(int at, String text) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		throw new FmtNotYetImplementedError();
	}
	
	@Override
	public String replaceText(String text) throws UnsupportedOperationException
	{
		throw new FmtNotYetImplementedError();
	}
	
	@Override
	public String replaceText(int from, int length, String text) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		throw new FmtNotYetImplementedError();
	}
	
	// =========================================================================
	
	@Override
	public boolean supportsAttributes()
	{
		return false;
	}
	
	@Override
	public Collection<DomAttribute> getAttributes()
	{
		return Collections.emptyList();
	}
	
	@Override
	public String getAttribute(String name)
	{
		return null;
	}
	
	@Override
	public DomAttribute getAttributeNode(String name)
	{
		return null;
	}
	
	@Override
	public DomAttribute removeAttribute(String name) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void removeAttributeNode(DomAttribute attr) throws UnsupportedOperationException, IllegalArgumentException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String setAttribute(String name, String value) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public DomAttribute setAttributeNode(DomAttribute attr) throws UnsupportedOperationException, IllegalArgumentException
	{
		throw new UnsupportedOperationException();
	}
}
