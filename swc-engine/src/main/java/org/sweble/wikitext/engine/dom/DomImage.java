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

import java.net.URL;

/**
 * A Wikitext image.
 */
public interface DomImage
        extends
            DomInlineElement,
            DomLink
{
	/**
	 * Get the source of the image.
	 * 
	 * @return The page title of the image.
	 */
	public String getSource();
	
	/**
	 * Set the source of the image.
	 * 
	 * @param source
	 *            The page title of the new image source.
	 * @return The page title of the old image source.
	 */
	public String setSource(String source);
	
	/**
	 * Get whether the image will be rendered with a grey border.
	 * 
	 * @return <code>True</code> if the image is rendered with a grey border,
	 *         <code>false</code> otherwise.
	 */
	public boolean isBorder();
	
	/**
	 * Set whether the image should be rendered with a grey border
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
	 * @return The horizontal alignment.
	 */
	public DomImageHAlign getHAlign();
	
	/**
	 * Set the horiizontal alignment of the image.
	 * 
	 * @return The old setting.
	 */
	public DomImageHAlign setHAlign(DomImageHAlign halign);
	
	/**
	 * Get the vertical alignment of the image. Only applies to inline,
	 * non-floating images.
	 * 
	 * @return The vertical alignment of the image.
	 */
	public DomImageVAlign getVAlign();
	
	/**
	 * Set the vertical alignment of the image. Only applies to inline,
	 * non-floating images.
	 * 
	 * @param valign
	 *            The new vertical alignment of the image.
	 * 
	 * @return The old vertical alignment of the image.
	 */
	public DomImageVAlign setVAlign(DomImageVAlign valign);
	
	/**
	 * Get the width to which the image should be scaled before rendering.
	 * 
	 * @return The width in pixels.
	 */
	public int getWidth();
	
	/**
	 * Set the width to which the image should be scaled before rendering.
	 * 
	 * @param width
	 *            The new width in pixels.
	 * @return The old width in pixels.
	 */
	public int setWidth(int width);
	
	/**
	 * Get the height to which the image should be scaled before rendering.
	 * 
	 * @return The height in pixels.
	 */
	public int getHeight();
	
	/**
	 * Set the height to which the image should be scaled before rendering.
	 * 
	 * @param height
	 *            The new heightin pixels.
	 * @return The old height in pixels.
	 */
	public int setHeight(int height);
	
	/**
	 * Whether the image will be resized according to user preferences.
	 * 
	 * @return <code>True</code> if the image will be resized according to user
	 *         preferences, <code>false</code> otherwise.
	 */
	public boolean isUpright();
	
	/**
	 * Set whether the image should will be resized according to user
	 * preferences.
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
	 * @return The optional URL.
	 */
	public URL getUrlLink();
	
	/**
	 * Set the optional URL to which the image will link when clicked. The
	 * optional URL and optional page link are mutually exclusive.
	 * 
	 * @param url
	 *            The new URL to which the image should link.
	 * 
	 * @return The old optional URL.
	 */
	public URL setUrlLink(URL url);
	
	/**
	 * Get the optional page to which the image will link when clicked. The
	 * optional page link and optional URL are mutually exclusive.
	 * 
	 * @return The optional page.
	 */
	public String getPageLink();
	
	/**
	 * Set the optional page to which the image will link when clicked. The
	 * optional page and link optional URL are mutually exclusive.
	 * 
	 * @param page
	 *            The new page to which the image should link.
	 * 
	 * @return The old optional page.
	 */
	public String setPageLink(String page);
	
	/**
	 * Get the alternative text of the image.
	 * 
	 * @return The alternative text.
	 */
	public String getAlt();
	
	/**
	 * Set the alternative text of the image.
	 * 
	 * @param alt
	 *            The new alternative text of the image.
	 * 
	 * @return The old alternative text.
	 */
	public String setAlt(String alt);
	
	/**
	 * Returns an empty title since images do not provide a title.
	 * 
	 * @return An empty title.
	 */
	@Override
	public DomTitle getTitle();
	
	/**
	 * Return the target this image links to. This is the page of the image
	 * itself or another page or url if the <code>pagelink</code> or
	 * <code>urllink</code> attributes are set.
	 * 
	 * @return The target this image links to.
	 */
	@Override
	public Object getTarget();
}
