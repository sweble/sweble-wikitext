package org.sweble.wikitext.engine.astwom;

public class MustBeOneOfException
		extends
			IllegalArgumentException
{
	private static final long serialVersionUID = 1L;
	
	private final String[] expected;
	
	private final String value;
	
	public MustBeOneOfException(String[] expected, String value)
	{
		this.expected = expected;
		this.value = value;
	}
	
	@Override
	public String getMessage()
	{
		return String.format(
				"Expected one of the following values: %s, but got: %s!",
				this.expected.toString(),
				this.value);
	}
}
