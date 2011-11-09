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
 * A node containing plain text.
 * 
 * Corresponds to the XHTML 1.0 Transitional type "Text".
 * 
 * <b>Child elements:</b> -
 */
public interface WomText
		extends
			WomNode
{
	/**
	 * Return the text content of this node.
	 * 
	 * @return The text stored in this node.
	 */
	@Override
	public String getText();
	
	/**
	 * Return the text content of this node.
	 * 
	 * @return The text stored in this node.
	 */
	@Override
	public String getValue();
	
	@Override
	public void appendText(String text) throws UnsupportedOperationException;
	
	@Override
	public void deleteText(int from, int length)
		throws UnsupportedOperationException,
			IndexOutOfBoundsException;
	
	@Override
	public void insertText(int at, String text)
		throws UnsupportedOperationException,
			IndexOutOfBoundsException;
	
	@Override
	public String replaceText(String text) throws UnsupportedOperationException;
	
	@Override
	public String replaceText(int from, int length, String text)
		throws UnsupportedOperationException,
			IndexOutOfBoundsException;
}
