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

package org.sweble.wikitext.lazy;

/**
 * Also this enumeration contains constants "ERROR" and "FATAL" it is called
 * &lt;Warning>Severity because the lazy parser cannot fail.
 */
public enum WarningSeverity
{
	/** Really not a problem. */
	NONE
	{
		@Override
		public int getLevel()
		{
			return 0;
		}
	},
	
	/** Might be worth looking into. */
	INFORMATIVE
	{
		@Override
		public int getLevel()
		{
			return 5;
		}
	},
	
	/** Should be taken care of. */
	NORMAL
	{
		@Override
		public int getLevel()
		{
			return 10;
		}
	},
	
	/** That's really bad. */
	FATAL
	{
		@Override
		public int getLevel()
		{
			return 100;
		}
	};
	
	public abstract int getLevel();
}
