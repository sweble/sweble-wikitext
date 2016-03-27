/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
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
