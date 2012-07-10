/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sweble.wikitext.engine.ext.core;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.config.ParserFunctionGroup;
import org.sweble.wikitext.lazy.preprocessor.Template;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.Text;

public class CorePfnVariablesDateAndTime
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	protected CorePfnVariablesDateAndTime()
	{
		super("Core - Variables - Date and Time");
		addParserFunction(new CurrentYearPfn());
		addParserFunction(new CurrentMonthPfn());
		addParserFunction(new CurrentDayPfn());
	}
	
	public static CorePfnVariablesDateAndTime group()
	{
		return new CorePfnVariablesDateAndTime();
	}
	
	// =========================================================================
	// ==
	// == {{CURRENTYEAR}}
	// ==
	// =========================================================================
	
	public static final class CurrentYearPfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;
		
		public CurrentYearPfn()
		{
			super("currentyear");
		}
		
		@Override
		protected final AstNode invoke(Template var, ExpansionFrame frame)
		{
			Calendar cal = GregorianCalendar.getInstance();
			return new Text(String.valueOf(cal.get(GregorianCalendar.YEAR)));
		}
	}
	
	// =========================================================================
	// == 
	// == TODO: {{CURRENTMONTH}}
	// ==
	// =========================================================================
	
	public static final class CurrentMonthPfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;
		
		public CurrentMonthPfn()
		{
			super("currentmonth");
		}
		
		@Override
		protected final AstNode invoke(Template var, ExpansionFrame frame)
		{
			Calendar cal = GregorianCalendar.getInstance();
			return new Text(String.format("%02d", cal.get(GregorianCalendar.MONTH) + 1));
		}
	}
	
	// =========================================================================
	// == 
	// == TODO: {{CURRENTMONTHNAME}}
	// == TODO: {{CURRENTMONTHNAMEGEN}}
	// == TODO: {{CURRENTMONTHABBREV}}
	// ==
	// =========================================================================
	
	// =========================================================================
	// == 
	// == TODO: {{CURRENTDAY}}
	// ==
	// =========================================================================
	
	public static final class CurrentDayPfn
			extends
				CorePfnVariable
	{
		private static final long serialVersionUID = 1L;
		
		public CurrentDayPfn()
		{
			super("currentday");
		}
		
		@Override
		protected final AstNode invoke(Template var, ExpansionFrame frame)
		{
			Calendar cal = GregorianCalendar.getInstance();
			return new Text(String.valueOf(cal.get(GregorianCalendar.DAY_OF_MONTH)));
		}
	}
	
	// =========================================================================
	// == 
	// == TODO: {{CURRENTDAY2}}
	// == TODO: {{CURRENTDOW}}
	// == TODO: {{CURRENTDAYNAME}}
	// == 
	// == Time
	// == ----
	// == TODO: {{CURRENTTIME}}
	// == TODO: {{CURRENTHOUR}}
	// == 
	// == Other
	// == -----
	// == TODO: {{CURRENTWEEK}}
	// == TODO: {{CURRENTTIMESTAMP}}
	// ==
	// =========================================================================
}
