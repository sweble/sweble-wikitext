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
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3I18nDir;

/**
 * This is a base class for all XHTML elements which can technically have
 * children and attributes. The term "technically" means that you could put
 * children into those elements notwithstanding that they might not be allowed
 * to have children. Almost all XHTML elements fall into this category.
 * 
 * Examples for such elements would be "div" and "br". "div" is allowed to have
 * children while "br" is not allowed to have children but one could put
 * children into "br" none the less. An XHTML comment on the other hand would
 * not fit this category since there is no way to add child elements or
 * attributes to an XHTML comment.
 * 
 * Classes which inherit from this class have to be prepared to take any kind of
 * child node and have arbitrary attributes. Thus they are prepared to deal with
 * erroneous XHTML.
 * 
 * This class already provides a child manager. Inheriting classes do not have
 * to add any child management code.
 * 
 * This class does not provide an attribute manager. Technically it should since
 * classes inheriting from this class have to be prepared to take any attribute.
 * However, since implementing classes might have special, non-universal
 * attributes, they require additional getters and setters. Defining the
 * attribute manager field in this class would conflict with the way we pull in
 * attribute getters and setters from the attribute manager using the lombok
 * library.
 */
public abstract class BackboneWomElemWithCoreAttrs
		extends
			BackboneWomElement
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	public BackboneWomElemWithCoreAttrs(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
		// TODO: Restrict accordingly
	}

	@Override
	protected void allowsRemoval(Backbone child)
	{
	}

	@Override
	protected void allowsReplacement(Backbone oldChild, Backbone newChild)
	{
		// TODO: Restrict accordingly
	}

	// ==[ Core Attributes ]====================================================

	public String getId()
	{
		return getStringAttr("id");
	}

	public String setId(String id) throws IllegalArgumentException
	{
		return setAttributeDirect(CoreAttributes.ATTR_DESC_ID, "id", id);
	}

	public String getClasses()
	{
		return getStringAttr("class");
	}

	public String setClasses(String classes)
	{
		return setAttributeDirect(CoreAttributes.ATTR_DESC_CLASS, "class", classes);
	}

	public String getStyle()
	{
		return getStringAttr("style");
	}

	public String setStyle(String style)
	{
		return setAttributeDirect(CoreAttributes.ATTR_DESC_STYLE, "style", style);
	}

	public String getTitle()
	{
		return getStringAttr("title");
	}

	public String setTitle(String title)
	{
		return setAttributeDirect(CoreAttributes.ATTR_DESC_TITLE, "title", title);
	}

	// ==[ I18n Attributes ]====================================================

	public Wom3I18nDir getDir()
	{
		return (Wom3I18nDir) getAttributeNativeData("dir");
	}

	public Wom3I18nDir setDir(Wom3I18nDir dir)
	{
		return setAttributeDirect(I18nAttributes.ATTR_DESC_DIR, "dir", dir);
	}

	public String getLang()
	{
		return getStringAttr("lang");
	}

	public String setLang(String lang)
	{
		return setAttributeDirect(I18nAttributes.ATTR_DESC_LANG, "lang", lang);
	}

	public String getXmlLang()
	{
		return getStringAttr("xml:lang");
	}

	public String setXmlLang(String lang)
	{
		return setAttributeDirect(I18nAttributes.ATTR_DESC_XML_LANG, "xml:lang", lang);
	}

	// ==[ Event Attributes ]===================================================

	public String getOnclick()
	{
		return getStringAttr("onclick");
	}

	public String setOnclick(String handler)
	{
		return setAttributeDirect(EventAttributes.ATTR_DESC_ONCLICK, "onclick", handler);
	}

	public String getOndblclick()
	{
		return getStringAttr("ondblclick");
	}

	public String setOndblclick(String handler)
	{
		return setAttributeDirect(EventAttributes.ATTR_DESC_ONDBLCLICK, "ondblclick", handler);
	}

	public String getOnmousedown()
	{
		return getStringAttr("onmousedown");
	}

	public String setOnmousedown(String handler)
	{
		return setAttributeDirect(EventAttributes.ATTR_DESC_ONMOUSEDOWN, "onmousedown", handler);
	}

	public String getOnmouseup()
	{
		return getStringAttr("onmouseup");
	}

	public String setOnmouseup(String handler)
	{
		return setAttributeDirect(EventAttributes.ATTR_DESC_ONMOUSEUP, "onmouseup", handler);
	}

	public String getOnmouseover()
	{
		return getStringAttr("onmouseover");
	}

	public String setOnmouseover(String handler)
	{
		return setAttributeDirect(EventAttributes.ATTR_DESC_ONMOUSEOVER, "onmouseover", handler);
	}

	public String getOnmousemove()
	{
		return getStringAttr("onmousemove");
	}

	public String setOnmousemove(String handler)
	{
		return setAttributeDirect(EventAttributes.ATTR_DESC_ONMOUSEMOVE, "onmousemove", handler);
	}

	public String getOnmouseout()
	{
		return getStringAttr("onmouseout");
	}

	public String setOnmouseout(String handler)
	{
		return setAttributeDirect(EventAttributes.ATTR_DESC_ONMOUSEOUT, "onmouseout", handler);
	}

	public String getOnkeypress()
	{
		return getStringAttr("onkeypress");
	}

	public String setOnkeypress(String handler)
	{
		return setAttributeDirect(EventAttributes.ATTR_DESC_ONKEYPRESS, "onkeypress", handler);
	}

	public String getOnkeydown()
	{
		return getStringAttr("onkeydown");
	}

	public String setOnkeydown(String handler)
	{
		return setAttributeDirect(EventAttributes.ATTR_DESC_ONKEYDOWN, "onkeydown", handler);
	}

	public String getOnkeyup()
	{
		return getStringAttr("onkeyup");
	}

	public String setOnkeyup(String handler)
	{
		return setAttributeDirect(EventAttributes.ATTR_DESC_ONKEYUP, "onkeyup", handler);
	}
}
