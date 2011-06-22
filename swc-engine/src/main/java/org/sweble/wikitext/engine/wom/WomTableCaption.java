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
 * Denotes a table caption.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "caption".
 * 
 * <b>Child elements:</b> Mixed, [Inline elements]*
 */
public interface WomTableCaption
        extends
            WomNode,
            WomUniversalAttributes
{
	/**
	 * Get the alignment of the caption.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @return The alignment of the caption or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public WomTableCaptionAlign getAlign();
	
	/**
	 * Set the alignment of the caption.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @param align
	 *            The new alignment of the caption or <code>null</code> to
	 *            remove the attribute.
	 * @return The old alignment of the caption.
	 */
	public WomTableCaptionAlign setAlign(WomTableCaptionAlign align);
}
