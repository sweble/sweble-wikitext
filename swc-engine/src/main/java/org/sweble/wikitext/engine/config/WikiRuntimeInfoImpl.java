package org.sweble.wikitext.engine.config;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class WikiRuntimeInfoImpl
		implements
			WikiRuntimeInfo
{
	@SuppressWarnings("unused")
	private final WikiConfigImpl wikiConfigImpl;
	
	public WikiRuntimeInfoImpl(WikiConfigImpl wikiConfigImpl)
	{
		this.wikiConfigImpl = wikiConfigImpl;
	}
	
	@Override
	public Calendar getDateAndTime()
	{
		Calendar timestamp = new GregorianCalendar(wikiConfigImpl.getTimezone());
		timestamp.setLenient(true);
		return timestamp;
	}
	
	@Override
	public Calendar getDateAndTime(Locale locale)
	{
		Calendar timestamp = new GregorianCalendar(locale);
		timestamp.setLenient(true);
		return timestamp;
	}
}
