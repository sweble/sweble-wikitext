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
 * A color value.
 */
public interface WomColor
{
	/**
	 * Set the RGB values of the color.
	 * 
	 * Values that are out of range will be clipped into the interval [0, 255].
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
