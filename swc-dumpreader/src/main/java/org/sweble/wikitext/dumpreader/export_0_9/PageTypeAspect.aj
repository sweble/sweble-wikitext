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

package org.sweble.wikitext.dumpreader.export_0_9;

import org.sweble.wikitext.dumpreader.DumpReaderListener;

import java.util.ArrayList;

public aspect PageTypeAspect
{
	public void PageType.setRevisionListener(final DumpReaderListener listener)
	{
		DumpReaderListener oldListener = null;
		if ((this.revisionOrUpload != null) && (this.revisionOrUpload instanceof MyArrayList))
			oldListener = ((MyArrayList) this.revisionOrUpload).getListener();
		
		if (listener != null)
		{
			if (oldListener != listener)
			{
				this.revisionOrUpload = new MyArrayList(PageType.this, listener);
			}
		}
		else
		{
			this.revisionOrUpload = null;
		}
	}
	
	public static final class MyArrayList
			extends
				ArrayList<Object>
	{
		private static final long serialVersionUID = 1L;
		
		private final DumpReaderListener listener;
		
		private PageType page;
		
		public MyArrayList(
				final PageType page,
				final DumpReaderListener listener)
		{
			this.page = page;
			this.listener = listener;
		}
		
		public DumpReaderListener getListener()
		{
			return listener;
		}
		
		public boolean add(Object revisionOrUpload)
		{
			if (listener.handleRevisionOrUploadOrLogitem(page, revisionOrUpload))
				return super.add(revisionOrUpload);
			return false;
		}
	}
}
