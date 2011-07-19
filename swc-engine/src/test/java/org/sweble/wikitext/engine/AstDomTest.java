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

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.sweble.wikitext.engine.astdom.AstToDomConverter;
import org.sweble.wikitext.engine.dom.DomNode;
import org.sweble.wikitext.engine.dom.tools.DomPrinter;
import org.sweble.wikitext.lazy.LinkTargetException;
import org.sweble.wikitext.lazy.utils.AstPrinter;

public class AstDomTest
        extends
            CompilerTestBase
{
	private static final boolean QUIET = false;
	
	public AstDomTest() throws FileNotFoundException, IOException
	{
		super();
	}
	
	@Test
	public void test() throws LinkTargetException, IOException, CompilerException
	{
		final String pageName = "AstDomTest";
		
		PageTitle title = PageTitle.make(getConfig(), pageName);
		CompiledPage page = postprocess(pageName, false);
		
		if (!QUIET)
			System.out.println(AstPrinter.print(page.getPage()));
		
		DomNode dom = AstToDomConverter.convert(
		        getConfig(),
		        title,
		        page.getPage());
		
		if (!QUIET)
			System.out.println(DomPrinter.print(dom));
		
		dom.getFirstChild().removeChild(
		        dom.getFirstChild().getFirstChild());
		
		if (!QUIET)
			System.out.println(DomPrinter.print(dom));
	}
}
