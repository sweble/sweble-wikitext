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

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InOrder;
import org.sweble.wikitext.engine.astwom.ChildManagerBase.ChildManager;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtText;

public class ChildManagerTest
{
	@Test
	public void whenAppendingChildAssureBothParentAndChildAreInformed() throws Exception
	{
		ChildManager cm = new ChildManagerBase.ChildManager();
		
		FullElement child = mock(FullElement.class);
		FullElement parent = mock(FullElement.class);
		
		WtNodeList astContainer = new WtNodeList();
		when(parent.getAstAttribContainer()).thenReturn(astContainer);
		
		WtNode astChild = new WtText("...");
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
