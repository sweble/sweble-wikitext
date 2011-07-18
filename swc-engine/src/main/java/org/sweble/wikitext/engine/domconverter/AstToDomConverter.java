package org.sweble.wikitext.engine.domconverter;

import java.util.HashMap;
import java.util.LinkedList;

import org.sweble.wikitext.engine.dom.DomAttribute;
import org.sweble.wikitext.engine.dom.DomNode;
import org.sweble.wikitext.lazy.ParserConfigInterface;
import org.sweble.wikitext.lazy.parser.XmlElement;
import org.sweble.wikitext.lazy.utils.XmlAttribute;

import de.fau.cs.osr.ptk.common.Visitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.utils.FmtNotYetImplementedError;

public class AstToDomConverter
        extends
            Visitor
{
	private static final HashMap<String, XmlElementFactoryEnum> xmlElements;
	
	private final ParserConfigInterface config;
	
	static
	{
		xmlElements = new HashMap<String, XmlElementFactoryEnum>();
		for (XmlElementFactoryEnum e : XmlElementFactoryEnum.values())
			xmlElements.put(e.toString().toLowerCase(), e);
	}
	
	// =========================================================================
	
	public AstToDomConverter(ParserConfigInterface config)
	{
		super();
		this.config = config;
	}
	
	// =========================================================================
	
	public DomNode visit(XmlElement e)
	{
		XmlElementFactoryEnum f = xmlElements.get(e.getName().toLowerCase());
		if (f == null)
			throw new FmtNotYetImplementedError(
			        "Unmapable xml elements are not yet supported");
		
		DomNode result = f.create(e);
		
		for (DomAttribute attr : mapDropNull(e.getXmlAttributes(), DomAttribute.class))
			result.setAttributeNode(attr);
		
		for (DomNode child : mapDropNull(e.getBody()))
			result.appendChild(child);
		
		return result;
	}
	
	public DomAttribute visit(XmlAttribute a)
	{
		DomAttribute attr = new XmlAttributeAdapter(config, a);
		return attr;
	}
	
	// =========================================================================
	
	private LinkedList<DomNode> mapDropNull(NodeList l)
	{
		if (l == null)
			return null;
		
		LinkedList<DomNode> result = new LinkedList<DomNode>();
		for (AstNode n : l)
		{
			DomNode o = (DomNode) dispatch(n);
			if (o != null)
				result.add(o);
		}
		
		if (result.isEmpty())
			return null;
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private <T> LinkedList<T> mapDropNull(NodeList l, Class<T> clazz)
	{
		return (LinkedList<T>) mapDropNull(l);
	}
}
