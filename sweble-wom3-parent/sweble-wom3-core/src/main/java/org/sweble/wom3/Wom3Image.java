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

import java.net.URL;

/**
 * Denotes a Wikitext image.
 * 
 * Corresponds to the WXML 1.0 element "image".
 * 
 * <b>Child elements:</b> imgcaption?
 */
public interface Wom3Image
		extends
			Wom3ElementNode,
			Wom3Link
{
	/**
	 * Get the source of the image.
	 * 
	 * Corresponds to the XWML 1.0 attribute "source".
	 * 
	 * @return The page title of the image.
	 */
	public String getSource();

	/**
	 * Set the source of the image.
	 * 
	 * Corresponds to the XWML 1.0 attribute "source".
	 * 
	 * @param source
	 *            The page title of the new image source.
	 * @return The page title of the old image source.
	 * @throws IllegalArgumentException
	 *             Thrown if an empty source is passed or the source is not a
	 *             valid page title.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is passed as source.
	 */
	public String setSource(String source) throws IllegalArgumentException, NullPointerException;

	/**
	 * Get the image rendering format.
	 * 
	 * @return The image rendering format.
	 */
	public Wom3ImageFormat getFormat();

	/**
	 * Set the image rendering format.
	 * 
	 * @param format
	 *            The new image rendering format or <code>null</code> to remove
	 *            the attribute. Passing <code>unrestrained</code> also removes
	 *            the attribute as this is the default.
	 * @return The old image rendering format.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is given as format.
	 */
	public Wom3ImageFormat setFormat(Wom3ImageFormat format) throws NullPointerException;

	/**
	 * Get whether the image will be rendered with a grey border.
	 * 
	 * Corresponds to the XWML 1.0 attribute "border".
	 * 
	 * @return <code>True</code> if the image is rendered with a grey border,
	 *         <code>false</code> otherwise.
	 */
	public boolean isBorder();

	/**
	 * Set whether the image should be rendered with a grey border
	 * 
	 * Corresponds to the XWML 1.0 attribute "border".
	 * 
	 * @param border
	 *            <code>True</code> if the image should be rendered with a grey
	 *            border, <code>false</code> otherwise.
	 * @return The old setting.
	 */
	public boolean setBorder(boolean border);

	/**
	 * Get the horizontal alignment of the image.
	 * 
	 * Corresponds to the XWML 1.0 attribute "halign".
	 * 
	 * @return The horizontal alignment or <code>null</code> if the attribute is
	 *         not specified.
	 */
	public Wom3ImageHAlign getHAlign();

	/**
	 * Set the horizontal alignment of the image.
	 * 
	 * Corresponds to the XWML 1.0 attribute "halign".
	 * 
	 * @param halign
	 *            The new setting or <code>null</code> to remove the attribute.
	 * @return The old setting.
	 */
	public Wom3ImageHAlign setHAlign(Wom3ImageHAlign halign);

	/**
	 * Get the vertical alignment of the image. Only applies to inline,
	 * non-floating images.
	 * 
	 * Corresponds to the XWML 1.0 attribute "valign".
	 * 
	 * @return The vertical alignment of the image or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public Wom3ImageVAlign getVAlign();

	/**
	 * Set the vertical alignment of the image. Only applies to inline,
	 * non-floating images.
	 * 
	 * Corresponds to the XWML 1.0 attribute "valign".
	 * 
	 * @param valign
	 *            The new setting or <code>null</code> to remove the attribute.
	 * @return The old setting.
	 */
	public Wom3ImageVAlign setVAlign(Wom3ImageVAlign valign);

	/**
	 * Get the width to which the image should be scaled before rendering.
	 * 
	 * Corresponds to the XWML 1.0 attribute "width".
	 * 
	 * @return The width in pixels or <code>null</code> if the attribute is not
	 *         specified.
	 */
	public Integer getWidth();

	/**
	 * Set the width to which the image should be scaled before rendering.
	 * 
	 * Corresponds to the XWML 1.0 attribute "width".
	 * 
	 * @param width
	 *            The new width in pixels or <code>null</code> to remove the
	 *            attribute.
	 * @return The old width in pixels.
	 */
	public Integer setWidth(Integer width);

	/**
	 * Get the height to which the image should be scaled before rendering.
	 * 
	 * Corresponds to the XWML 1.0 attribute "height".
	 * 
	 * @return The height in pixels or <code>null</code> if the attribute is not
	 *         specified.
	 */
	public Integer getHeight();

	/**
	 * Set the height to which the image should be scaled before rendering.
	 * 
	 * Corresponds to the XWML 1.0 attribute "height".
	 * 
	 * @param height
	 *            The new height in pixels or <code>null</code> to remove the
	 *            attribute.
	 * @return The old height in pixels.
	 */
	public Integer setHeight(Integer height);

	/**
	 * Whether the image will be resized according to user preferences.
	 * 
	 * Corresponds to the XWML 1.0 attribute "upright".
	 * 
	 * @return <code>True</code> if the image will be resized according to user
	 *         preferences, <code>false</code> otherwise.
	 */
	public boolean isUpright();

	/**
	 * Set whether the image should will be resized according to user
	 * preferences.
	 * 
	 * Corresponds to the XWML 1.0 attribute "upright".
	 * 
	 * @param upright
	 *            <code>True</code> if the image should be resized according to
	 *            user preferences, <code>false</code> otherwise.
	 * 
	 * @return The old setting.
	 */
	public boolean setUpright(boolean upright);

	/**
	 * Get the optional URL to which the image will link when clicked. The
	 * optional URL and optional page link are mutually exclusive.
	 * 
	 * Corresponds to the XWML 1.0 attribute "urllink".
	 * 
	 * @return The URL to link to or <code>null</code> if the attribute is not
	 *         specified.
	 */
	public URL getExtLink();

	/**
	 * Set the optional URL to which the image will link when clicked. The
	 * optional URL and optional page link are mutually exclusive. If the
	 * attribute "pagelink" is specified and this method is called to set a
	 * "urllink", the "pagelink" attribute will be removed.
	 * 
	 * Corresponds to the XWML 1.0 attribute "urllink".
	 * 
	 * @param url
	 *            The new URL to which the image should link or
	 *            <code>null</code> to remove the attribute.
	 * @return The old URL.
	 */
	public URL setExtLink(URL url);

	/**
	 * Get the optional page to which the image will link when clicked. The
	 * optional page link and optional URL are mutually exclusive.
	 * 
	 * Corresponds to the XWML 1.0 attribute "pagelink".
	 * 
	 * @return The page to link to or <code>null</code> if the attribute is not
	 *         specified.
	 */
	public String getIntLink();

	/**
	 * Set the optional page to which the image will link when clicked. The
	 * optional page and link optional URL are mutually exclusive. If the
	 * attribute "urllink" is specified and this method is called to set a
	 * "pagelink", the "urllink" attribute will be removed.
	 * 
	 * Corresponds to the XWML 1.0 attribute "pagelink".
	 * 
	 * @param page
	 *            The new page to which the image should link.
	 * @return The old page.
	 */
	public String setIntLink(String page);

	/**
	 * Get the alternative text of the image.
	 * 
	 * Corresponds to the XWML 1.0 attribute "alt".
	 * 
	 * @return The alternative text or <code>null</code> if the attribute is not
	 *         specified.
	 */
	public String getAlt();

	/**
	 * Set the alternative text of the image.
	 * 
	 * Corresponds to the XWML 1.0 attribute "alt".
	 * 
	 * @param alt
	 *            The new alternative text of the image or <code>null</code> to
	 *            remove the attribute.
	 * @return The old alternative text.
	 */
	public String setAlt(String alt);

	// ==[ Caption ]============================================================

	/**
	 * Get the caption of the image.
	 * 
	 * @return The caption of the image or <code>null</code> if the link does
	 *         not specify a caption.
	 */
	public Wom3ImageCaption getCaption();

	/**
	 * Set the caption of the image.
	 * 
	 * @param caption
	 *            The new caption of the image or <code>null</code> to remove
	 *            the caption.
	 * @return The old caption.
	 */
	public Wom3ImageCaption setCaption(Wom3ImageCaption caption);

	// ==[ Link interface ]=====================================================

	/**
	 * Returns the alternative text of the image. If no alternative text is
	 * given an empty title will be returned.
	 * 
	 * @return The title of this image.
	 */
	@Override
	public Wom3Title getLinkTitle();

	/**
	 * Return the target this image links to. This is the page of the image
	 * itself or another page or url if the <code>pagelink</code> or
	 * <code>urllink</code> attributes are set.
	 * 
	 * @return The target this image links to.
	 */
	@Override
	public Object getLinkTarget();
}
