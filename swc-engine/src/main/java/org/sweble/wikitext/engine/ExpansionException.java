package org.sweble.wikitext.engine;

import de.fau.cs.osr.ptk.common.VisitingException;

/**
 * A wrapper for exceptions that occurred during the expansion process.
 */
public class ExpansionException
		extends
			RuntimeException
{
	
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public ExpansionException(Throwable cause)
	{
		super(unwrap(cause));
	}
	
	// =========================================================================
	
	private static Throwable unwrap(Throwable t)
	{
		while (t instanceof ExpansionException
				|| t instanceof VisitingException)
			t = t.getCause();
		return t;
	}
}
