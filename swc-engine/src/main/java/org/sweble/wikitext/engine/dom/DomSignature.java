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
 * A Wikitext signature.
 */
public interface DomSignature
        extends
            DomInlineElement
{
	/**
	 * Get the signature format that describes how the signature should be
	 * rendered.
	 * 
	 * @return The signature format.
	 */
	public DomSignatureFormat getSignatureFormat();
	
	/**
	 * Set the signature format that describes how the signature should be
	 * rendered.
	 * 
	 * @param format
	 *            The new signature format.
	 * @return The old signature format.
	 */
	public DomSignatureFormat setSignatureFormat(DomSignatureFormat format);
	
	/**
	 * Get the name of the author.
	 * 
	 * @return The author name.
	 */
	public String getAuthor();
	
	/**
	 * Set the author name.
	 * 
	 * @param author
	 *            The new name of the author.
	 * @return The old author name.
	 */
	public String setAuthor(String author);
	
	/**
	 * Get the date and time of the signature.
	 * 
	 * @return The date and time of the signature.
	 */
	public Date getTimestamp();
	
	/**
	 * Set the date and time of the signature.
	 * 
	 * @param timestamp
	 *            The new date and time of the signature.
	 * @return The old date and time of the signature.
	 */
	public Date setTimestamp(Date timestamp);
}
