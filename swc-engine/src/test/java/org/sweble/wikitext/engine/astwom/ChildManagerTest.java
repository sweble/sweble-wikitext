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
