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
package org.sweble.wikitext.engine.wom;

import java.util.Date;

/**
 * Denotes text or a block that has been added.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "ins".
 * 
 * <b>Child elements:</b> Mixed, [Flow elements]*
 */
public interface WomIns
        extends
            WomInlineElement,
            WomBlockElement,
            WomUniversalAttributes
{
	/**
	 * Get the url of a document that specifies the reasons for the change.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cite".
	 * 
	 * @return The url or <code>null</code> if the attribute is not specified.
	 */
	public String getCite();
	
	/**
	 * Set the url of a document that specifies the reasons for the change.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cite".
	 * 
	 * @param url
	 *            The new url or <code>null</code> to remove the attribute.
	 * @return The The old url.
	 */
	public String setCite(String url);
	
	/**
	 * Get the timestamp when the text or block was deleted.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cite".
	 * 
	 * @return The date and time of the deletion or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public Date getDatetime();
	
	/**
	 * Set the timestamp when the text or block was deleted.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cite".
	 * 
	 * @param timestamp
	 *            The new timestamp or <code>null</code> to remove the
	 *            attribute.
	 * @return The old timestamp.
	 */
	public Date setDatetime(Date timestamp);
}
