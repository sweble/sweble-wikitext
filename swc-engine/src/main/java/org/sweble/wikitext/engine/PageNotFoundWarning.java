package org.sweble.wikitext.engine;

import org.sweble.wikitext.lazy.WarningSeverity;

import de.fau.cs.osr.ptk.common.ast.AstNode;

public class PageNotFoundWarning
		extends
			OffendingNodeWarning
{
	private static final long serialVersionUID = 1L;
	
	private final PageTitle title;
	
	// =========================================================================
	
	public PageNotFoundWarning(
			WarningSeverity severity,
			String origin,
			AstNode titleNode,
			PageTitle title)
	{
		super(titleNode, severity, origin, makeMessage(title));
		this.title = title;
	}
	
	public PageNotFoundWarning(
			WarningSeverity severity,
			Class<?> origin,
			AstNode titleNode,
			PageTitle title)
	{
		super(titleNode, severity, origin, makeMessage(title));
		this.title = title;
	}
	
	private static String makeMessage(PageTitle title)
	{
		return "The given text `" + title.getDenormalizedFullTitle() + "' " +
				"does not constitute a valid page name";
	}
	
	public PageTitle getTitle()
	{
		return title;
	}
	
	// =========================================================================
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PageNotFoundWarning other = (PageNotFoundWarning) obj;
		if (title == null)
		{
			if (other.title != null)
				return false;
		}
		else if (!title.equals(other.title))
			return false;
		return true;
	}
}
