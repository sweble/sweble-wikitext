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

public class JobHistory
{
	private final JobHistory previous;

	private final JobProcessingState state;

	private final Object result;

	private final Exception exception;

	// =========================================================================

	/**
	 * Construct a history element for a successful processing run.
	 */
	public JobHistory(JobHistory previous, Object result)
	{
		this.previous = previous;
		this.state = JobProcessingState.HAS_RESULT;
		this.result = result;
		this.exception = null;
	}

	/**
	 * Construct a history element for a failed processing run.
	 */
	public JobHistory(JobHistory previous, Exception exception)
	{
		this.previous = previous;
		this.state = JobProcessingState.FAILED;
		this.result = null;
		this.exception = exception;
	}

	// =========================================================================

	/**
	 * Return the history of the processing run preceding this processing run.
	 * 
	 * @return Returns the previous processing run or <code>null</code> if there
	 *         was no previous processing run.
	 */
	public JobHistory getPrevious()
	{
		return previous;
	}

	/**
	 * Get the processing state of the processing run represented by this
	 * history element.
	 */
	public JobProcessingState getState()
	{
		return state;
	}

	/**
	 * Get the result of the processing run represented by this history element.
	 * 
	 * @return Returns the result if the processing run was successful.
	 *         Otherwise, if processing failed, <code>null</code> is returned.
	 */
	public Object getResult()
	{
		return result;
	}

	/**
	 * Get the exception of the processing run represented by this history
	 * element.
	 * 
	 * @return Returns the exception if the processing run failed. Otherwise, if
	 *         processing was successful, <code>null</code> is returned.
	 */
	public Exception getException()
	{
		return exception;
	}
}
