package org.sweble.wikitext.engine.ext;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.MagicWordBase;
import org.sweble.wikitext.lazy.parser.MagicWord;

import de.fau.cs.osr.ptk.common.ast.AstNode;

public class UnimplementedMagicWord
		extends
			MagicWordBase
{
	
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public UnimplementedMagicWord(
			org.sweble.wikitext.engine.config.MagicWord magicWord)
	{
		super(magicWord);
	}
	
	// =========================================================================
	
	@Override
	public AstNode invoke(ExpansionFrame expansionFrame, MagicWord magicWord)
	{
		expansionFrame.fileWarning(
				new NotYetImplementedMagicWordWarning(this, magicWord));
		
		return magicWord;
	}
	
}
