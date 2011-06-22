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

/**
 * Denotes an XML-style comment in the Wikitext.
 * 
 * Corresponds to the WXML 1.0 element "comment".
 * 
 * <b>Child elements:</b> Text
 */
public interface WomComment
        extends
            WomProcessingInstruction
{
	/**
	 * Return the text of the comment.
	 * 
	 * @return The text of the comment.
	 */
	@Override
	public String getValue();
	
	/**
	 * Set the text of the comment.
	 * 
	 * @param text
	 *            The new text of the comment.
	 * @return The old text of the comment.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is passed as text.
	 * @throws IllegalArgumentException
	 *             Thrown if the given text violates the syntax of XML comments
	 *             (e.g.: " -" would violate the XML comment syntax since it
	 *             would become "&lt;-- ---&gt;").
	 */
	public String setValue(String text) throws IllegalArgumentException, NullPointerException;
}
