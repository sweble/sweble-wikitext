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

package org.sweble.wikitext.engine.config;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.utils.UrlService;

@XmlType(
		name = "interwiki",
		propOrder = { "prefix", "local", "trans", "url" })
public class InterwikiImpl
		implements
			Interwiki,
			Serializable,
			Comparable<Interwiki>
{
	private static final long serialVersionUID = 1L;

	private String prefix;

	private String url;

	private boolean local;

	private boolean trans;

	// =========================================================================

	public InterwikiImpl()
	{
	}

	public InterwikiImpl(String prefix, String url, boolean local, boolean trans)
	{
		super();
		this.prefix = prefix;
		this.url = url;
		this.local = local;
		this.trans = trans;
	}

	// =========================================================================

	@Override
	@XmlAttribute
	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	@XmlAttribute
	public String getUrl()
	{
		return url;
	}

	@Override
	public URL getUrl(PageTitle title)
	{
		try
		{
			return UrlService.makeUrlToArticle(this.url, title);
		}
		catch (MalformedURLException e)
		{
			/* This should not happen: If the URL is correctly formatted, 
			 * appending a title must not cause a MalformedURLException. 
			 */
			throw new WikiConfigurationException(e);
		}
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	@Override
	@XmlAttribute
	public boolean isLocal()
	{
		return local;
	}

	public void setLocal(boolean local)
	{
		this.local = local;
	}

	@Override
	@XmlAttribute
	public boolean isTrans()
	{
		return trans;
	}

	public void setTrans(boolean trans)
	{
		this.trans = trans;
	}

	// =========================================================================

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (local ? 1231 : 1237);
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		result = prime * result + (trans ? 1231 : 1237);
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InterwikiImpl other = (InterwikiImpl) obj;
		if (local != other.local)
			return false;
		if (prefix == null)
		{
			if (other.prefix != null)
				return false;
		}
		else if (!prefix.equals(other.prefix))
			return false;
		if (trans != other.trans)
			return false;
		if (url == null)
		{
			if (other.url != null)
				return false;
		}
		else if (!url.equals(other.url))
			return false;
		return true;
	}

	// =========================================================================

	@Override
	public String toString()
	{
		return "InterwikiImpl [prefix=" + prefix + ", url=" + url + ", local=" + local + ", trans=" + trans + "]";
	}

	// =========================================================================

	@Override
	public int compareTo(Interwiki o)
	{
		return this.prefix.compareTo(o.getPrefix());
	}
}
