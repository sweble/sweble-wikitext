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

public enum PfnArgumentMode
{
	/**
	 * This is the normal mode. The original template arguments (name, value)
	 * will be collapsed into a single value of the form "$name=$value". No
	 * expansion is done.
	 */
	UNEXPANDED_VALUES,

	/**
	 * The original template arguments will be collapsed as is done by
	 * UNEXPANDED_VALUES. Then the collapsed value is expanded and trimmed.
	 */
	EXPANDED_AND_TRIMMED_VALUES,

	/**
	 * The original template arguments are left as is.
	 */
	TEMPLATE_ARGUMENTS,
}
