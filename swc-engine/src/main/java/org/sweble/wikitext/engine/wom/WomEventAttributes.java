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
 * The XHTML 1.0 Transitional event attributes.
 */
public interface WomEventAttributes
{
	/**
	 * Return the "onclick" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onclick".
	 * 
	 * @return The "onclick" event handler call or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public String getOnclick();
	
	/**
	 * Set the "onclick" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onclick".
	 * 
	 * @param handler
	 *            The new handler or <code>null</code> to remove the attribute.
	 * @return The old handler.
	 */
	public String setOnclick(String handler);
	
	/**
	 * Return the "ondblclick" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "ondblclick".
	 * 
	 * @return The "ondblclick" event handler call or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public String getOndblclick();
	
	/**
	 * Set the "ondblclick" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "ondblclick".
	 * 
	 * @param handler
	 *            The new handler or <code>null</code> to remove the attribute.
	 * @return The old handler.
	 */
	public String setOndblclick(String handler);
	
	/**
	 * Return the "onmousedown" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onmousedown".
	 * 
	 * @return The "onmousedown" event handler call or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public String getOnmousedown();
	
	/**
	 * Set the "onmousedown" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onmousedown".
	 * 
	 * @param handler
	 *            The new handler or <code>null</code> to remove the attribute.
	 * @return The old handler.
	 */
	public String setOnmousedown(String handler);
	
	/**
	 * Return the "onmouseup" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onmouseup".
	 * 
	 * @return The "onmouseup" event handler call or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public String getOnmouseup();
	
	/**
	 * Set the "onmouseup" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onmouseup".
	 * 
	 * @param handler
	 *            The new handler or <code>null</code> to remove the attribute.
	 * @return The old handler.
	 */
	public String setOnmouseup(String handler);
	
	/**
	 * Return the "onmouseover" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onmouseover".
	 * 
	 * @return The "onmouseover" event handler call or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public String getOnmouseover();
	
	/**
	 * Set the "onmouseover" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onmouseover".
	 * 
	 * @param handler
	 *            The new handler or <code>null</code> to remove the attribute.
	 * @return The old handler.
	 */
	public String setOnmouseover(String handler);
	
	/**
	 * Return the "onmousemove" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onmousemove".
	 * 
	 * @return The "onmousemove" event handler call or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public String getOnmousemove();
	
	/**
	 * Set the "onmousemove" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onmousemove".
	 * 
	 * @param handler
	 *            The new handler or <code>null</code> to remove the attribute.
	 * @return The old handler.
	 */
	public String setOnmousemove(String handler);
	
	/**
	 * Return the "onmouseout" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onmouseout".
	 * 
	 * @return The "onmouseout" event handler call or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public String getOnmouseout();
	
	/**
	 * Set the "onmouseout" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onmouseout".
	 * 
	 * @param handler
	 *            The new handler or <code>null</code> to remove the attribute.
	 * @return The old handler.
	 */
	public String setOnmouseout(String handler);
	
	/**
	 * Return the "onkeypress" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onkeypressw".
	 * 
	 * @return The "onkeypress" event handler call or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public String getOnkeypress();
	
	/**
	 * Set the "onkeypress" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onkeypressw".
	 * 
	 * @param handler
	 *            The new handler or <code>null</code> to remove the attribute.
	 * @return The old handler.
	 */
	public String setOnkeypress(String handler);
	
	/**
	 * Return the "onkeydown" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onkeydown".
	 * 
	 * @return The "onkeydown" event handler call or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public String getOnkeydown();
	
	/**
	 * Set the "onkeydown" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onkeydown".
	 * 
	 * @param handler
	 *            The new handler or <code>null</code> to remove the attribute.
	 * @return The old handler.
	 */
	public String setOnkeydown(String handler);
	
	/**
	 * Return the "onkeyup" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onkeyup".
	 * 
	 * @return The "onkeyup" event handler call or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public String getOnkeyup();
	
	/**
	 * Set the "onkeyup" event handler call.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "onkeyup".
	 * 
	 * @param handler
	 *            The new handler or <code>null</code> to remove the attribute.
	 * @return The old handler.
	 */
	public String setOnkeyup(String handler);
}
