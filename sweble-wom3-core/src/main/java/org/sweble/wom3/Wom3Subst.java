/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

public interface Wom3Subst
		extends
			Wom3ElementNode
{
	public String getDisplacementId();
	
	public String setDisplacementId(String did);
	
	public Wom3Repl getRepl();
	
	public Wom3Repl setRepl(Wom3Repl name);
	
	public Wom3For getFor();
	
	public Wom3For setFor(Wom3For value) throws NullPointerException;
}
