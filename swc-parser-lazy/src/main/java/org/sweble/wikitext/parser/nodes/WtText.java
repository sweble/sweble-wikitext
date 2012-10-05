package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.WtRtData;

import de.fau.cs.osr.ptk.common.ast.GenericText;

/* 
 * @startuml img/WtNode.png
 * 
 * WtNode <|-- WtText 
 * GenericText <|-- WtText : GenericText<WtNode>
 *  
 * class WtText {
 * 	+ WtText()
 * 	+ WtText(String content)
 * }
 * 
 * @enduml
 */
public class WtText
		extends
			GenericText<WtNode>
		implements
			WtNode
{
	
	private static final long serialVersionUID = 7333107598118095040L;
	
	// =========================================================================
	
	public WtText()
	{
	}
	
	public WtText(String content)
	{
		super(content);
	}
	
	// =========================================================================
	
	@Override
	public WtRtData setRtd(WtRtData rtd)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public WtRtData setRtd(Object... glue)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public WtRtData setRtd(String... glue)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public WtRtData getRtd()
	{
		return null;
	}
	
	@Override
	public void clearRtd()
	{
	}
}
