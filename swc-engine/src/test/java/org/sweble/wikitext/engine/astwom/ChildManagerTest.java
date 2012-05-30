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

package org.sweble.wikitext.engine.astwom;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.InOrder;
import org.sweble.wikitext.engine.astwom.ChildManagerBase.ChildManager;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;

public class ChildManagerTest
{
	@Test
	public void whenAppendingChildAssureBothParentAndChildAreInformed() throws Exception
	{
		ChildManager cm = new ChildManagerBase.ChildManager();
		
		FullElement child = mock(FullElement.class);
		FullElement parent = mock(FullElement.class);
		
		NodeList astContainer = new NodeList();
		when(parent.getAstAttribContainer()).thenReturn(astContainer);
		
		AstNode astChild = new Text("...");
		when(child.getAstNode()).thenReturn(astChild);
		
		cm.appendChild(child, parent, astContainer);
		
		InOrder order = inOrder(child, parent);
		order.verify(parent).acceptsChild(child);
		order.verify(child).acceptsParent(parent);
		
		// child is informed (indirectly)
		order.verify(child).link(parent, null, null);
		
		// parent is informed (indirectly)
		order.verify(parent).appendToAst(astContainer, astChild);
	}
}
