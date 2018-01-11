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

package org.sweble.wikitext.engine.ext.cite_web;


public enum CiteWebArgument
{
	ARG_URL("url"),
	ARG_TITLE("title"),
	ARG_LAST("last"),
	ARG_FRIST("first"),
	ARG_AUTHOR("author"),
	ARG_AUTHOR_LINK("author-link"),
	ARG_LAST2("last2"),
	ARG_FIRST2("first2"),
	ARG_AUTHOR2("author2"),
	ARG_AUTHOR_LINK2("author-link2"),
	ARG_DATE("date"),
	ARG_YEAR("year"),
	ARG_EDITOR_LAST("editor-last"),
	ARG_EDITOR_FIRST("editor-first"),
	ARG_EDITOR("editor"),
	ARG_EDITOR_LINK("editor-link"),
	ARG_EDITOR2_LAST("editor2-last"),
	ARG_EDITOR2_FIRST("editor2-first"),
	ARG_EDITOR2_LINK("editor2-link"),
	ARG_EDITORS("editors"),
	ARG_DEPARTMENT("department"),
	ARG_WEBSITE("website"),
	ARG_SERIES("series"),
	ARG_PUBLISHER("publisher"),
	ARG_LOCATION("location"),
	ARG_PAGE("page"),
	ARG_PAGES("pages"),
	ARG_AT("at"),
	ARG_LANGUAGE("language"),
	ARG_SCRIPT_TITLE("script-title"),
	ARG_TRANS_TITLE("trans-title"),
	ARG_TYPE("type"),
	ARG_FORMAT("format"),
	ARG_ARXIV("arxiv"),
	ARG_ASIN("asin"),
	ARG_BIBCODE("bibcode"),
	ARG_DOI("doi"),
	ARG_DOI_BROKEN_DATE("doi-broken-date"),
	ARG_ISBN("isbn"),
	ARG_ISSN("issn"),
	ARG_JFM("jfm"),
	ARG_JSTOR("jstor"),
	ARG_LCCN("lccn"),
	ARG_MR("mr"),
	ARG_OCLC("oclc"),
	ARG_OL("ol"),
	ARG_OSTI("osti"),
	ARG_PMC("pmc"),
	ARG_PMID("pmid"),
	ARG_RFC("rfc"),
	ARG_SSRN("ssrn"),
	ARG_ZBL("zbl"),
	ARG_ID("id"),
	ARG_ARCHIVE_URL("archive-url"),
	ARG_ARCHIVE_DATE("archive-date"),
	ARG_DEAD_URL("dead-url"),
	ARG_ACCESS_DATE("access-date"),
	ARG_QUOTE("quote"),
	ARG_REF("ref"),
	ARG_POSTSCRIPT("postscript"),
	ARG_SUBSCRIPTION("subscription"),
	ARG_REGISTRATION("registration"),
	;

	private final String argumentName;

	CiteWebArgument(final String argName)
	{
		this.argumentName = argName;
	}

	/**
	 * Searches for an argument String and returns the corresponding Enum.
	 * 
	 * @param argStr The String which defines the argument.
	 * @return The corresponding on success, otherwise null.
	 */
	static public CiteWebArgument findArgument(final String argStr)
	{
		for(CiteWebArgument argEnum : CiteWebArgument.values())
		{
			if (argStr.equals(argEnum.getArgumentName()))
			{
				return argEnum;
			}
		}
		return null;
	}

	public String getArgumentName()
	{
		return argumentName;
	}
}
