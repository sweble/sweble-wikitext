/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
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
		return setAttributeDirect(CoreAttributes.ID, "id", id);
	}
	
	public String getClasses()
	{
		return getStringAttr("class");
	}
	
	public String setClasses(String classes)
	{
		return setAttributeDirect(CoreAttributes.CLASS, "class", classes);
	}
	
	public String getStyle()
	{
		return getStringAttr("style");
	}
	
	public String setStyle(String style)
	{
		return setAttributeDirect(CoreAttributes.STYLE, "style", style);
	}
	
	public String getTitle()
	{
		return getStringAttr("title");
	}
	
	public String setTitle(String title)
	{
		return setAttributeDirect(CoreAttributes.TITLE, "title", title);
	}
	
	// ==[ I18n Attributes ]====================================================
	
	public Wom3I18nDir getDir()
	{
		return (Wom3I18nDir) getAttributeNativeData("dir");
	}
	
	public Wom3I18nDir setDir(Wom3I18nDir dir)
	{
		return setAttributeDirect(I18nAttributes.DIR, "dir", dir);
	}
	
	public String getLang()
	{
		return getStringAttr("lang");
	}
	
	public String setLang(String lang)
	{
		return setAttributeDirect(I18nAttributes.LANG, "lang", lang);
	}
	
	public String getXmlLang()
	{
		return getStringAttr("xml:lang");
	}
	
	public String setXmlLang(String lang)
	{
		return setAttributeDirect(I18nAttributes.XMLLANG, "xml:lang", lang);
	}
	
	// ==[ Event Attributes ]===================================================
	
	public String getOnclick()
	{
		return getStringAttr("onclick");
	}
	
	public String setOnclick(String handler)
	{
		return setAttributeDirect(EventAttributes.ONCLICK, "onclick", handler);
	}
	
	public String getOndblclick()
	{
		return getStringAttr("ondblclick");
	}
	
	public String setOndblclick(String handler)
	{
		return setAttributeDirect(EventAttributes.ONDBLCLICK, "ondblclick", handler);
	}
	
	public String getOnmousedown()
	{
		return getStringAttr("onmousedown");
	}
	
	public String setOnmousedown(String handler)
	{
		return setAttributeDirect(EventAttributes.ONMOUSEDOWN, "onmousedown", handler);
	}
	
	public String getOnmouseup()
	{
		return getStringAttr("onmouseup");
	}
	
	public String setOnmouseup(String handler)
	{
		return setAttributeDirect(EventAttributes.ONMOUSEUP, "onmouseup", handler);
	}
	
	public String getOnmouseover()
	{
		return getStringAttr("onmouseover");
	}
	
	public String setOnmouseover(String handler)
	{
		return setAttributeDirect(EventAttributes.ONMOUSEOVER, "onmouseover", handler);
	}
	
	public String getOnmousemove()
	{
		return getStringAttr("onmousemove");
	}
	
	public String setOnmousemove(String handler)
	{
		return setAttributeDirect(EventAttributes.ONMOUSEMOVE, "onmousemove", handler);
	}
	
	public String getOnmouseout()
	{
		return getStringAttr("onmouseout");
	}
	
	public String setOnmouseout(String handler)
	{
		return setAttributeDirect(EventAttributes.ONMOUSEOUT, "onmouseout", handler);
	}
	
	public String getOnkeypress()
	{
		return getStringAttr("onkeypress");
	}
	
	public String setOnkeypress(String handler)
	{
		return setAttributeDirect(EventAttributes.ONKEYPRESS, "onkeypress", handler);
	}
	
	public String getOnkeydown()
	{
		return getStringAttr("onkeydown");
	}
	
	public String setOnkeydown(String handler)
	{
		return setAttributeDirect(EventAttributes.ONKEYDOWN, "onkeydown", handler);
	}
	
	public String getOnkeyup()
	{
		return getStringAttr("onkeyup");
	}
	
	public String setOnkeyup(String handler)
	{
		return setAttributeDirect(EventAttributes.ONKEYUP, "onkeyup", handler);
	}
}
