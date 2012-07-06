package org.sweble.wikitext.engine;

import static org.sweble.wikitext.lazy.utils.AstBuilder.*;

import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.lazy.AstNodeTypes;
import org.sweble.wikitext.lazy.parser.XmlElement;
import org.sweble.wikitext.lazy.utils.XmlAttribute;

import de.fau.cs.osr.ptk.common.ast.AstNode;

public class SoftErrorNode
		extends
			XmlElement
{
	private static final long serialVersionUID = 1L;
	
	public SoftErrorNode(String message)
	{
		this(astText(message));
	}
	
	public SoftErrorNode(AstNode content)
	{
		// We don't store the content as AST node of the error node. This way
		// we prevent visitors from trying to do something to the error node.
		//
		// this(RtWikitextPrinter.print(content));
		// setAttribute("node", content);
		
		super("strong",
				false,
				astList(astXmlAttrib()
						.withName("class")
						.withValue("error")
						.build()),
				astList(content));
		
		Toolbox.addRtData(this);
	}
	
	public SoftErrorNode(AstNode content, Exception e)
	{
		this(content);
		setAttribute("exception", e);
	}
	
	public void addCssClass(String cssClass)
	{
		for (AstNode attrib : this.getXmlAttributes())
		{
			if (attrib == null || !(attrib instanceof XmlAttribute))
				continue;
			XmlAttribute a = (XmlAttribute) attrib;
			if (!a.getName().equals("class"))
				continue;
			a.setValue(astList(a.getValue(), astText(" " + cssClass)));
		}
	}
	
	@Override
	public int getNodeType()
	{
		return AstNodeTypes.NT_ERROR;
	}
}
