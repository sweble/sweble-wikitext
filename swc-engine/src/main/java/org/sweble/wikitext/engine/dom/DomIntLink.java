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
 * Denotes a Wikitext internal link.
 */
public interface DomIntLink
        extends
        	DomInlineElement,
            DomLink
{
	@Override
	public DomTitle getTitle();
	
	/**
	 * Set a new link title.
	 * 
	 * @param title
	 *            The new title of the internal link.
	 * 
	 * @return The old link title node.
	 */
	public DomTitle setTitle(DomTitle title);
	
	@Override
	public String getTarget();
	
	/**
	 * Set a new target for this internal link.
	 * 
	 * @param target
	 *            The new target of the internal link.
	 * 
	 * @return The old target of the internal link.
	 */
	public URL setTarget(String target);
}
