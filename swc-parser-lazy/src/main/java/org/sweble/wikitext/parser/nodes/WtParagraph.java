package org.sweble.wikitext.parser.nodes;

/**
 * <h1>WtParagraph</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * ( '\n' Space* &'\n' )+
 * </p>
 * </li>
 * </ul>
 */
public class WtParagraph
		extends
			WtContentNodeMarkTwo
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtParagraph()
	{
	}
	
	public WtParagraph(WtNodeList content)
	{
		super(content);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_PARAGRAPH;
	}
}
