package org.sweble.wikitext.engine.dom;

/**
 * A HTML color value.
 */
public interface DomColor
{
	/**
	 * Set the RGB values of the color.
	 * 
	 * @param r
	 *            A value for the red component between 0 and 255.
	 * @param g
	 *            A value for the green component between 0 and 255.
	 * @param b
	 *            A value for the blue component between 0 and 255.
	 */
	public void setRGB(int r, int g, int b);
	
	/**
	 * Get the red color component.
	 * 
	 * @return A value between 0 and 255.
	 */
	public int getRed();
	
	/**
	 * Get the green color component.
	 * 
	 * @return A value between 0 and 255.
	 */
	public int getGreen();
	
	/**
	 * Get the blue color component.
	 * 
	 * @return A value between 0 and 255.
	 */
	public int getBlue();
}
