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
package org.sweble.wikitext.engine.dom;

import java.util.Date;

/**
 * Denotes text or a block that has been added.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "ins".
 */
public interface DomIns
        extends
            DomInlineElement,
            DomBlockElement,
            DomUniversalAttributes
{
	/**
	 * Get the url of a document that specifies the reasons for the change.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cite".
	 * 
	 * @return The url.
	 */
	public String getCite();
	
	/**
	 * Get the timestamp when the text or block was added.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "ins".
	 * 
	 * @return The date and time of the addition.
	 */
	public Date getDatetime();
}
