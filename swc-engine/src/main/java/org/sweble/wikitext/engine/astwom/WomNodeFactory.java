package org.sweble.wikitext.engine.astwom;

import org.sweble.wikitext.engine.wom.WomBold;
import org.sweble.wikitext.engine.wom.WomComment;
import org.sweble.wikitext.engine.wom.WomHorizontalRule;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomParagraph;

public interface WomNodeFactory
{
	
	public abstract WomNode createText(String string);
	
	public abstract WomComment createComment(String text);
	
	public abstract WomBold createBold(WomNode... children);
	
	public abstract WomHorizontalRule createHorizontalRule();
	
	public abstract WomParagraph createParagraph(WomNode... children);
	
}
