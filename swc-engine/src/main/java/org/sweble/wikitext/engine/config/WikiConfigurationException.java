package org.sweble.wikitext.engine.config;

public class WikiConfigurationException
		extends
			RuntimeException
{
	
	private static final long serialVersionUID = 1L;
	
	public WikiConfigurationException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public WikiConfigurationException(String message)
	{
		super(message);
	}
	
	public WikiConfigurationException(Throwable cause)
	{
		super(cause);
	}
	
}
