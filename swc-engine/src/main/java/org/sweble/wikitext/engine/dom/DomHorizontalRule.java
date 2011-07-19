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

/**
 * Denotes a horizontal rule.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "hr".
 */
public interface DomHorizontalRule
        extends
            DomBlockElement,
            DomUniversalAttributes
{
	/**
	 * Get the alignment of the horizontal rule.
	 * 
	 * @return The alignment of the horizontal rule.
	 */
	public DomAlign getAlign();
	
	/**
	 * Set the alignment of the horizontal rule.
	 * 
	 * @param align
	 *            The alignment. Only the values <code>left</code>,
	 *            <code>center</code> and <code>right</code> are allowed.
	 * @return The old alignment of the horizontal rule.
	 */
	public DomAlign setAlign(DomAlign align);
	
	/**
	 * Get whether the horizontal rule is display with a 3-D effect (shade) or
	 * without (no-shade).
	 * 
	 * @return <code>True</true> for no 3-D effect, <code>false</code> for a 3-D
	 *         effect.
	 */
	public boolean isNoshade();
	
	/**
	 * Set whether the horizontal rule is display with a 3-D effect (shade) or
	 * without (no-shade).
	 * 
	 * @param noshade
	 *            The new setting.
	 * @return The old setting.
	 */
	public boolean setNoshade(boolean noshade);
	
	/**
	 * Get the thickness of the horizontal rule in pixels.
	 * 
	 * @return The thickness in pixels.
	 */
	public int getSize();
	
	/**
	 * Set the thickness of the horizontal rule in pixels.
	 * 
	 * @param size
	 *            The new thickness in pixels.
	 * @return The old thickness in pixels.
	 */
	public int setSize(int size);
	
	/**
	 * Get the width of the horizontal rule.
	 * 
	 * @return The width in pixels or percent.
	 */
	public DomValueWithUnit getWidth();
	
	/**
	 * Set the width of the horizontal rule.
	 * 
	 * @param size
	 *            The new width in pixels or percent.
	 * @return The old width in pixels or percent.
	 */
	public DomValueWithUnit setWidth(DomValueWithUnit width);
}
