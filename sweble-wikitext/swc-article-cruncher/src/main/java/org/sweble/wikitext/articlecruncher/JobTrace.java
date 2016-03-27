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

package org.sweble.wikitext.articlecruncher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class JobTrace
{
	public static final class Signer
	{
		private Class<?> signer;

		private String message;

		public Signer(Class<?> signer, String message)
		{
			super();
			this.signer = signer;
			this.message = message;
		}

		public Class<?> getSigner()
		{
			return signer;
		}

		public String getMessage()
		{
			return message;
		}

		@Override
		public String toString()
		{
			String str = signer.getSimpleName();
			if (message != null)
				str += " <" + message + ">";
			return str;
		}
	}

	// =========================================================================

	private static AtomicLong jobIdCounter = new AtomicLong(0);

	private final long jobId = jobIdCounter.incrementAndGet();

	private List<Signer> signers;

	// =========================================================================

	public void signOff(Class<?> signer, String signature)
	{
		if (signers == null)
			signers = new ArrayList<Signer>();
		signers.add(new Signer(signer, signature));
	}

	public long getJobId()
	{
		return jobId;
	}

	public List<Signer> getSigners()
	{
		return signers;
	}

	// =========================================================================

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (jobId ^ (jobId >>> 32));
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
		JobTrace other = (JobTrace) obj;
		if (jobId != other.jobId)
			return false;
		return true;
	}

	// =========================================================================

	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();

		b.append(String.format("[ Job Trace: Job Id = %9d", jobId));

		boolean first = true;
		b.append("; Signed by = ");
		for (Signer signer : signers)
		{
			if (!first)
				b.append(", ");
			b.append(signer.toString());
			first = false;
		}

		b.append(" ]");
		return b.toString();
	}
}
