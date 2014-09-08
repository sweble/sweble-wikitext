/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

/**
 * The different formats to render and place Wikitext images.
 */
public enum Wom3ImageFormat
{
	/**
	 * <h2>Intended use:</h2>
	 * <p>
	 * Custom image formatting by the author.
	 * </p>
	 * 
	 * <h2>Should render as follows:</h2>
	 * <p>
	 * <ul>
	 * <li>Framed: no.</li>
	 * <li>Caption: no.</li>
	 * <li>Floating block: no.</li>
	 * <li>Inline: yes.</li>
	 * <li>Border option: respected.</li>
	 * <li>Sizing:
	 * <ul>
	 * <li>No size specified: Use original image size.</li>
	 * <li>Size specified: Scale down arbitrarily, scale up arbitrarily.</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * </p>
	 */
	UNRESTRAINED,
	
	/**
	 * <h2>Intended use:</h2>
	 * <p>
	 * A plain, frameless, possibly reduced image that serves as preview to the
	 * full image.
	 * </p>
	 * <p>
	 * FRAMELESS and THUMBNAIL have the same sizing behavior.
	 * </p>
	 * 
	 * <h2>Should render as follows:</h2>
	 * <p>
	 * <ul>
	 * <li>Framed: no.</li>
	 * <li>Caption: no.</li>
	 * <li>Floating block: no.</li>
	 * <li>Inline: yes.</li>
	 * <li>Border option: respected.</li>
	 * <li>Sizing:
	 * <ul>
	 * <li>No size specified: Scale down to wiki or user thumbnail size
	 * preferences, but do not scale up.</li>
	 * <li>Size specified: Scale down arbitrarily, scale up but not beyond
	 * original image size.</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * </p>
	 */
	FRAMELESS,
	
	/**
	 * <h2>Intended use:</h2>
	 * <p>
	 * A framed, possibly reduced image that serves as preview to the full
	 * image.
	 * </p>
	 * <p>
	 * FRAMELESS and THUMBNAIL have the same sizing behavior.
	 * </p>
	 * 
	 * <h2>Should render as follows:</h2>
	 * <p>
	 * <ul>
	 * <li>Framed: yes (appearance according to wiki or user preferences).</li>
	 * <li>Caption: yes (placement according to wiki or user preferences).</li>
	 * <li>Floating block: yes.</li>
	 * <li>Inline: no.</li>
	 * <li>Border option: ignored.</li>
	 * <li>Sizing:
	 * <ul>
	 * <li>No size specified: Scale down to wiki or user thumbnail size
	 * preferences, but do not scale up.</li>
	 * <li>Size specified: Scale down arbitrarily, scale up but not beyond
	 * original image size.</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * </p>
	 */
	THUMBNAIL,
	
	/**
	 * <h2>Intended use:</h2>
	 * <p>
	 * A framed image with the original image size.
	 * </p>
	 * 
	 * <h2>Should render as follows:</h2>
	 * <p>
	 * <ul>
	 * <li>Framed: yes (appearance according to wiki or user preferences).</li>
	 * <li>Caption: yes (placement according to wiki or user preferences).</li>
	 * <li>Floating block: yes.</li>
	 * <li>Inline: no.</li>
	 * <li>Border option: ignored.</li>
	 * <li>Sizing:
	 * <ul>
	 * <li>No size specified: Scale down to wiki maximum image size or user,but
	 * do not scale up.</li>
	 * <li>Size specified: Ignore.</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * </p>
	 */
	FRAME
}
