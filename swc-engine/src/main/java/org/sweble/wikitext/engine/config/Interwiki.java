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

public class Interwiki
        implements
            Serializable
{
	private static final long serialVersionUID = 1L;
	
	String prefix;
	
	String url;
	
	boolean local;
	
	boolean trans;
	
	// =========================================================================
	
	public Interwiki(String prefix, String url, boolean local, boolean trans)
	{
		super();
		this.prefix = prefix;
		this.url = url;
		this.local = local;
		this.trans = trans;
	}
	
	// =========================================================================
	
	public String getPrefix()
	{
		return prefix;
	}
	
	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}
	
	public String getUrl()
	{
		return url;
	}
	
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public boolean isLocal()
	{
		return local;
	}
	
	public void setLocal(boolean local)
	{
		this.local = local;
	}
	
	public boolean isTrans()
	{
		return trans;
	}
	
	public void setTrans(boolean trans)
	{
		this.trans = trans;
	}
}
