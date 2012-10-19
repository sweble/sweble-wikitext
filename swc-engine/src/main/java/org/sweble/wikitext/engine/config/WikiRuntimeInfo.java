package org.sweble.wikitext.engine.config;

import java.util.Calendar;
import java.util.Locale;

public interface WikiRuntimeInfo
{
	public Calendar getDateAndTime();
	
	public Calendar getDateAndTime(Locale locale);
}
