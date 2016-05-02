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
package org.sweble.wikitext.parser.utils;

public class NonExpandingParserConfig
		extends
			SimpleParserConfig
{
	private boolean fosterParenting;

	private boolean fosterParentingForTransclusions;

	public NonExpandingParserConfig()
	{
		super();
		initialize();
	}

	public NonExpandingParserConfig(
			boolean warningsEnabled,
			boolean gatherRtd,
			boolean autoCorrect)
	{
		super(warningsEnabled, gatherRtd, autoCorrect);
		initialize();
	}

	private void initialize()
	{
		fosterParenting = super.isFosterParenting();
		fosterParentingForTransclusions = super.isFosterParentingForTransclusions();
	}

	@Override
	public boolean isFosterParenting()
	{
		return fosterParenting;
	}

	public void setFosterParenting(boolean fosterParenting)
	{
		this.fosterParenting = fosterParenting;
	}

	@Override
	public boolean isFosterParentingForTransclusions()
	{
		return fosterParentingForTransclusions;
	}

	public void setFosterParentingForTransclusions(
			boolean fosterParentingForTransclusions)
	{
		this.fosterParentingForTransclusions = fosterParentingForTransclusions;
	}
}
