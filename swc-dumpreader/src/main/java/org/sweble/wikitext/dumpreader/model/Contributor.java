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
package org.sweble.wikitext.dumpreader.model;

import java.io.Serializable;
import java.math.BigInteger;

public class Contributor
		implements
			Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final BigInteger id;
	
	private final String ip;
	
	private final boolean deleted;
	
	private final String username;
	
	// =========================================================================
	
	public Contributor(
			BigInteger id,
			String ip,
			boolean deleted,
			String username)
	{
		this.id = id;
		this.ip = ip;
		this.deleted = deleted;
		this.username = username;
	}
	
	// =========================================================================
	
	public BigInteger getId()
	{
		return id;
	}
	
	public String getIp()
	{
		return ip;
	}
	
	public boolean isDeleted()
	{
		return deleted;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	// =========================================================================
	
	@Override
	public String toString()
	{
		return "Contributor [id=" + id + ", ip=" + ip + ", deleted=" + deleted + ", username=" + username + "]";
	}
}
