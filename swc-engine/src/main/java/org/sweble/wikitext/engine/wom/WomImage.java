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

import java.net.URL;

/**
 * Denotes a Wikitext image.
 * 
 * Corresponds to the WXML 1.0 element "image".
 * 
 * <b>Child elements:</b> imgcaption?
 */
public interface WomImage
		extends
			WomInlineElement,
			WomLink
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
	public WomImageFormat getFormat();
	
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
	public WomImageFormat setFormat(WomImageFormat format) throws NullPointerException;
	
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
	public WomImageHAlign getHAlign();
	
	/**
	 * Set the horizontal alignment of the image.
	 * 
	 * Corresponds to the XWML 1.0 attribute "halign".
	 * 
	 * @param halign
	 *            The new setting or <code>null</code> to remove the attribute.
	 * @return The old setting.
	 */
	public WomImageHAlign setHAlign(WomImageHAlign halign);
	
	/**
	 * Get the vertical alignment of the image. Only applies to inline,
	 * non-floating images.
	 * 
	 * Corresponds to the XWML 1.0 attribute "valign".
	 * 
	 * @return The vertical alignment of the image or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public WomImageVAlign getVAlign();
	
	/**
	 * Set the vertical alignment of the image. Only applies to inline,
	 * non-floating images.
	 * 
	 * Corresponds to the XWML 1.0 attribute "valign".
	 * 
	 * @param halign
	 *            The new setting or <code>null</code> to remove the attribute.
	 * @return The old setting.
	 */
	public WomImageVAlign setVAlign(WomImageVAlign valign);
	
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
	public URL getUrlLink();
	
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
	public URL setUrlLink(URL url);
	
	/**
	 * Get the optional page to which the image will link when clicked. The
	 * optional page link and optional URL are mutually exclusive.
	 * 
	 * Corresponds to the XWML 1.0 attribute "pagelink".
	 * 
	 * @return The page to link to or <code>null</code> if the attribute is not
	 *         specified.
	 */
	public String getPageLink();
	
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
	public String setPageLink(String page);
	
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
	
	// ==[ Link interface ]=====================================================
	
	/**
	 * Returns the alternative text of the image. If no alternative text is
	 * given an empty title will be returned.
	 * 
	 * @return The title of this image.
	 */
	@Override
	public WomTitle getLinkTitle();
	
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
