/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.serialization;

import org.w3c.dom.Node;

/**
 * The Gson type adapter encountered a DOM node that is not supported by the
 * type adapter.
 */
public class UnsupportedNodeException
		extends
			RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	private final Node node;
	
	public UnsupportedNodeException(Node node)
	{
		super(String.format("Node name: %s; Node type: %d", node.getNodeName(), node.getNodeType()));
		this.node = node;
	}
	
	public UnsupportedNodeException(Node node, String message, Throwable cause)
	{
		super(message, cause);
		this.node = node;
	}
	
	public UnsupportedNodeException(Node node, String message)
	{
		super(message);
		this.node = node;
	}
	
	public UnsupportedNodeException(Node node, Throwable cause)
	{
		super(cause);
		this.node = node;
	}
	
	public Node getNode()
	{
		return node;
	}
}
