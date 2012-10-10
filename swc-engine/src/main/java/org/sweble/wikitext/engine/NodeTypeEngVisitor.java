package org.sweble.wikitext.engine;

import org.sweble.wikitext.engine.nodes.EngNode;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtParserEntity;
import org.sweble.wikitext.parser.nodes.WtText;

import de.fau.cs.osr.ptk.common.NodeTypeAstVisitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.AstNodeList;
import de.fau.cs.osr.ptk.common.ast.AstParserEntity;
import de.fau.cs.osr.ptk.common.ast.AstText;

public class NodeTypeEngVisitor
		extends
			NodeTypeAstVisitor<WtNode>
{
	@Override
	protected Object resolveAndVisit(WtNode node, int type) throws Exception
	{
		switch (type)
		{
			case EngNode.NT_TEXT:
				return visit((WtText) node);
				
			case EngNode.NT_NODE_LIST:
				return visit((WtNodeList) node);
				
			case EngNode.NT_PARSER_ENTITY:
				return visit((WtParserEntity) node);
				
			default:
				return visitUnspecific(node);
		}
	}
	
	// =========================================================================
	
	protected Object visitUnspecific(WtNode node) throws Exception
	{
		return node;
	}
	
	protected Object visit(WtText node) throws Exception
	{
		return visitUnspecific(node);
	}
	
	protected Object visit(WtNodeList node) throws Exception
	{
		return visitUnspecific(node);
	}
	
	protected Object visit(WtParserEntity node) throws Exception
	{
		return visitUnspecific(node);
	}
	
	// =========================================================================
	// Make the original methods unusable
	
	@Override
	protected final Object visitUnspecific(AstNode<WtNode> node) throws Exception
	{
		throw new InternalError();
	}
	
	@Override
	protected final Object visit(AstText<WtNode> node) throws Exception
	{
		throw new InternalError();
	}
	
	@Override
	protected final Object visit(AstNodeList<WtNode> node) throws Exception
	{
		throw new InternalError();
	}
	
	@Override
	protected final Object visit(AstParserEntity<WtNode> node) throws Exception
	{
		throw new InternalError();
	}
}
