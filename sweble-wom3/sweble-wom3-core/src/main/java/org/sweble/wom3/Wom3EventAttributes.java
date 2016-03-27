/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3;

/**
 * The XHTML 1.0 Transitional event attributes.
 */
public interface Wom3EventAttributes
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
