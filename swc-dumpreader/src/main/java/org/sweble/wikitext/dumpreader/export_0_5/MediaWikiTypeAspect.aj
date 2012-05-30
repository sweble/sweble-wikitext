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

package org.sweble.wikitext.dumpreader.export_0_5;

import java.util.ArrayList;

import org.sweble.wikitext.dumpreader.PageListener;
import org.sweble.wikitext.dumpreader.export_0_5.MediaWikiType;
import org.sweble.wikitext.dumpreader.export_0_5.PageType;

public aspect MediaWikiTypeAspect
{
	public void MediaWikiType.setPageListener(final PageListener listener)
	{
		page = (listener == null) ? null : new ArrayList<PageType>()
		{
			private static final long serialVersionUID = 1L;
			
			public boolean add(PageType page)
			{
				listener.handlePage(MediaWikiType.this, page);
				return false;
			}
		};
	}
	
}
