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

package org.sweble.wikitext.engine;

public interface ExpansionCallback
{
	/**
	 * Retrieves the latest revision of the article.
	 * 
	 * @return The requested article or null if no such article could be found.
	 */
	FullPage retrieveWikitext(ExpansionFrame expansionFrame, PageTitle pageTitle);

	/**
	 * Retrieve the full URL of a file.
	 * 
	 * Example:
	 * 
	 * <pre>
	 * File:En-us-Barack-Hussein-Obama.ogg
	 * </pre>
	 * 
	 * Output:
	 * 
	 * <pre>
	 * http://upload.wikimedia.org/wikipedia/commons/8/82/En-us-Barack-Hussein-Obama.ogg
	 * </pre>
	 * 
	 * @param pageTitle
	 *            The title of the file.
	 * @param width
	 *            Return a URL to the image scaled to this width. This parameter
	 *            is ignored if it is less than 1px.
	 * @param height
	 *            Return a URL to the image scaled to this height. This
	 *            parameter is ignored if it is less than 1px.
	 * @return The full URL.
	 */
	String fileUrl(PageTitle pageTitle, int width, int height);

	/*
	 * Retrieves the specified revision of the article.
	 *
	String retrieveWikitext(ExpansionFrame expansionFrame, PageId pageId);
	void cachePreprocessedPage(ExpansionFrame expansionFrame, FullPreprocessedPage page);
	FullPreprocessedPage retrievePreprocessedPage(ExpansionFrame expansionFrame, PageTitle pageTitle, boolean forInclusion);
	 */
}
