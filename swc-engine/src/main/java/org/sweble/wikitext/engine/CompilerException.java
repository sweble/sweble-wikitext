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

package org.sweble.wikitext.engine;

import org.sweble.wikitext.engine.log.CompilerLog;

public final class CompilerException
        extends
            Exception
{
	private static final long serialVersionUID = 1L;
	
	private CompilerLog log;
	
	public CompilerException(String message, Throwable cause)
	{
		this(message, cause, null);
	}
	
	public CompilerException(String message, Throwable cause, CompilerLog log)
	{
		super(message, cause);
		this.log = log;
	}
	
	public void attachLog(CompilerLog log)
	{
		if (this.log != null)
			throw new IllegalStateException("Log already attached!");
		
		this.log = log;
	}
	
	public CompilerLog getLog()
	{
		return log;
	}
}
