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
 * A Wikitext signature.
 * 
 * Corresponds to the XWML 1.0 element "signature".
 * 
 * <b>Child elements:</b> -
 */
public interface WomSignature
		extends
			WomInlineElement
{
	/**
	 * Get the signature format that describes how the signature should be
	 * rendered.
	 * 
	 * Corresponds to the XWML 1.0 attribute "format".
	 * 
	 * @return The signature format.
	 */
	public WomSignatureFormat getSignatureFormat();
	
	/**
	 * Set the signature format that describes how the signature should be
	 * rendered.
	 * 
	 * Corresponds to the XWML 1.0 attribute "format".
	 * 
	 * @param format
	 *            The new signature format.
	 * @return The old signature format.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is given as format.
	 */
	public WomSignatureFormat setSignatureFormat(WomSignatureFormat format) throws NullPointerException;
	
	/**
	 * Get the name of the author.
	 * 
	 * Corresponds to the XWML 1.0 attribute "author".
	 * 
	 * @return The author name.
	 */
	public String getAuthor();
	
	/**
	 * Set the author name.
	 * 
	 * Corresponds to the XWML 1.0 attribute "author".
	 * 
	 * @param author
	 *            The new name of the author.
	 * @return The old author name.
	 * @throws IllegalArgumentException
	 *             Thrown if the given author name is not a valid MediaWiki user
	 *             name.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is given as format.
	 */
	public String setAuthor(String author) throws IllegalArgumentException, NullPointerException;
	
	/**
	 * Get the date and time of the signature.
	 * 
	 * Corresponds to the XWML 1.0 attribute "timestamp".
	 * 
	 * @return The date and time of the signature.
	 */
	public Date getTimestamp();
	
	/**
	 * Set the date and time of the signature.
	 * 
	 * Corresponds to the XWML 1.0 attribute "timestamp".
	 * 
	 * @param timestamp
	 *            The new date and time of the signature.
	 * @return The old date and time of the signature.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is given as format.
	 */
	public Date setTimestamp(Date timestamp) throws NullPointerException;
}
