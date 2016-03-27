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
package org.sweble.wom3.impl;

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3DocumentFragment;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.impl.AttributeDescriptor.Normalization;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public abstract class BackboneWithChildren
		extends
			Backbone
{
	private static final long serialVersionUID = 1L;

	/**
	 * It's vital that this member is only accessed and modified through
	 * get/setFirstChild().
	 */
	private Backbone firstChild;

	/**
	 * It's vital that this member is only accessed and modified through
	 * get/setLastChild().
	 */
	private Backbone lastChild;

	// =========================================================================

	public BackboneWithChildren(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	protected void setFirstChild(Backbone firstChild)
	{
		this.firstChild = firstChild;
	}

	@Override
	public Backbone getFirstChild()
	{
		return firstChild;
	}

	protected void setLastChild(Backbone lastChild)
	{
		this.lastChild = lastChild;
	}

	@Override
	public Backbone getLastChild()
	{
		return lastChild;
	}

	/**
	 * Called when a text node containing content whitespace is inserted into
	 * this element.
	 * 
	 * Override to change node type specific behavior.
	 * 
	 * @return Returns {@code true} if the node should be ignored silently and
	 *         not get inserted.
	 */
	protected boolean ignoresContentWhitespace()
	{
		// True for most WOM Version 3 nodes
		return true;
	}

	/**
	 * Called on the parent node when a new child node is added. The parent can
	 * then check if he accepts this kind of child node or throw an exception if
	 * he doesn't.
	 * 
	 * @param prev
	 *            The node after which the child was inserted.
	 * @param child
	 *            The child node to add.
	 * @throws IllegalArgumentException
	 *             Thrown if the parent does not accept this kind of child node.
	 */
	protected void allowsInsertion(
			Backbone prev,
			Backbone child)
	{
		if (getOwnerDocument().getStrictErrorChecking())
			doesNotAllowInsertion(prev, child);
	}

	protected void allowsReplacement(
			Backbone oldChild,
			Backbone newChild)
	{
		if (getOwnerDocument().getStrictErrorChecking())
			doesNotAllowReplacement(oldChild, newChild);
	}

	protected void allowsRemoval(Backbone child)
	{
		if (getOwnerDocument().getStrictErrorChecking())
			doesNotAllowRemoval(child);
	}

	/**
	 * Called when a node was appended to this node.
	 * 
	 * @param prev
	 *            The node after which the child was inserted.
	 * @param added
	 *            The child node that was added.
	 */
	protected void childInserted(
			Backbone prev,
			Backbone added)
	{
	}

	/**
	 * Called when a node was removed from this node.
	 * 
	 * @param prev
	 *            The node after which the child was previously located.
	 * @param removed
	 *            The child node that was removed.
	 * @return Return the element preceding the removed element (usually the one
	 *         given as the "prev" parameter). This allows this method to
	 *         further alter the sequence of children as long as the correct
	 *         previous element is returned. This is only important when a child
	 *         is replaced.
	 */
	protected void childRemoved(
			Backbone prev,
			Backbone removed)
	{
	}

	/**
	 * Override according to node type.
	 */
	protected void getTextContentRecursive(StringBuilder b)
	{
		for (Backbone n = getFirstChild(); n != null; n = n.getNextSibling())
		{
			switch (n.getNodeType())
			{
				case Node.COMMENT_NODE:
				case Node.PROCESSING_INSTRUCTION_NODE:
					break;
				default:
					n.getTextContentRecursive(b);
					break;
			}
		}
	}

	// =========================================================================
	// 
	// DOM Level 3 Node Interface Operations
	// 
	// =========================================================================

	@Override
	public Wom3Node insertBefore(Node child_, Node before_)
			throws DOMException
	{
		Wom3Node child = Toolbox.expectType(Wom3Node.class, child_);
		if (((Backbone) child).isContentWhitespace() && ignoresContentWhitespace())
			return null;

		Wom3Node before = Toolbox.expectType(Wom3Node.class, before_);
		if (before instanceof Wom3DocumentFragment)
		{
			Wom3Node move = child.getFirstChild();
			while (move != null)
			{
				Wom3Node next = move.getNextSibling();
				child.removeChild(move);

				Backbone prev = insertBeforeIntern(before, move, true);
				this.childInserted(prev, (Backbone) move);

				move = next;
			}
		}
		else
		{
			Backbone prev = insertBeforeIntern(before, child, true);
			this.childInserted(prev, (Backbone) child);
		}

		return child;
	}

	@Override
	public Wom3Node replaceChild(Node newChild_, Node oldChild_) throws DOMException
	{
		Backbone newChild = Toolbox.expectType(Backbone.class, newChild_);
		if (((Backbone) newChild).isContentWhitespace() && ignoresContentWhitespace())
			return removeChild(oldChild_);

		Backbone oldChild = Toolbox.expectType(Backbone.class, oldChild_);

		//Backbone prev = replaceChildIntern(newChild, oldChild, true);

		Backbone prevSibling = oldChild.getPreviousSibling();
		Backbone nextSibling = oldChild.getNextSibling();

		if (newChild instanceof Wom3DocumentFragment)
		{
			removeChildIntern(oldChild, true /* check */);
			childRemoved(prevSibling, oldChild);

			Backbone move = newChild.getFirstChild();
			while (move != null)
			{
				Backbone next = move.getNextSibling();
				newChild.removeChild(move);

				insertOrAppendIntern(move, nextSibling);
				childInserted(prevSibling, move);

				prevSibling = move;
				move = next;
			}
		}
		else
		{
			replaceChildIntern(newChild, oldChild, true /* check */);
			childRemoved(prevSibling, oldChild);
			childInserted(prevSibling, newChild);
		}

		return oldChild;
	}

	private void insertOrAppendIntern(Wom3Node newChild, Wom3Node nextSibling)
	{
		if (nextSibling != null)
		{
			insertBeforeIntern(nextSibling, newChild, true /* check */);
		}
		else
		{
			appendChildIntern(newChild, true /* check */, false /* cloning */);
		}
	}

	@Override
	public Wom3Node removeChild(Node child_) throws DOMException
	{
		Wom3Node child = Toolbox.expectType(Wom3Node.class, child_);
		Backbone prev = removeChildIntern(child, true);

		this.childRemoved(prev, (Backbone) child);

		return child;
	}

	@Override
	public Wom3Node appendChild(Node child_) throws DOMException
	{
		return appendChild(child_, false /* cloning */);
	}

	public Wom3Node appendChild(Node child_, boolean cloning) throws DOMException
	{
		Wom3Node child = Toolbox.expectType(Wom3Node.class, child_);
		if (((Backbone) child).isContentWhitespace() && ignoresContentWhitespace())
			return null;

		if (child instanceof Wom3DocumentFragment)
		{
			Wom3Node move = child.getFirstChild();
			while (move != null)
			{
				Wom3Node next = move.getNextSibling();
				child.removeChild(move);

				Backbone prev = appendChildIntern(move, true /* check */, cloning);
				childInserted(prev, (Backbone) move);

				move = next;
			}
		}
		else
		{
			Backbone prev = appendChildIntern(child, true /* check */, cloning);
			childInserted(prev, (Backbone) child);
		}

		return child;
	}

	@Override
	public void setTextContent(String textContent) throws DOMException
	{
		// Is expected to assertWritable();
		clearChildren();
		appendChild(getOwnerDocument().createTextNode(textContent));
	}

	// =========================================================================

	@Override
	public Wom3Node cloneNode(boolean deep)
	{
		BackboneWithChildren newNode = (BackboneWithChildren) super.cloneNode(deep);

		newNode.setFirstChild(null);
		newNode.setLastChild(null);

		// Do a deep clone
		if (deep)
		{
			Backbone child = this.getFirstChild();
			while (child != null)
			{
				newNode.appendChild(child.cloneNode(deep), true /* cloning */);
				child = child.getNextSibling();
			}
		}

		return newNode;
	}

	// =========================================================================

	private final Backbone insertBeforeIntern(
			Wom3Node before,
			Wom3Node child,
			boolean check)
	{
		assertWritableOnDocument();

		if (before == null || child == null)
			throw new IllegalArgumentException("Argument `before' and/or `child' is null.");

		final Backbone newChild =
				Toolbox.expectType(Backbone.class, child, "child");

		if (newChild.isLinked())
			throw new IllegalStateException(
					"Given argument `child' is still child of another WOM node.");

		if (before.getParentNode() != this)
			throw new IllegalArgumentException("Given node `before' is not a child of this node.");
		Backbone p = (Backbone) before;

		Backbone prev = p.getPreviousSibling();

		if (check)
			this.allowsInsertion(prev, newChild);

		// Indicate change before performing it
		++childrenChanges;

		newChild.link(this, prev, p);
		if (p == getFirstChild())
			setFirstChild(newChild);

		return prev;
	}

	private Backbone replaceChildIntern(
			Wom3Node replace,
			Wom3Node search,
			boolean check)
	{
		assertWritableOnDocument();

		if (search == null || replace == null)
			throw new IllegalArgumentException("Argument `search' and/or `replace' is null.");

		final Backbone newChild =
				Toolbox.expectType(Backbone.class, replace, "replace");

		if (newChild.isLinked())
			throw new IllegalStateException(
					"Given node `replace' is still child of another WOM node.");

		if (search.getParentNode() != this)
			throw new IllegalArgumentException("Given node `search' is not a child of this node.");
		Backbone oldChild = (Backbone) search;

		if (check)
			this.allowsReplacement(oldChild, newChild);

		Backbone prev = oldChild.getPreviousSibling();
		Backbone next = oldChild.getNextSibling();

		// Indicate change before performing it
		++childrenChanges;

		oldChild.unlink();

		newChild.link(this, prev, next);
		if (oldChild == getFirstChild())
			setFirstChild(newChild);
		if (oldChild == getLastChild())
			setLastChild(newChild);

		++childrenChanges;
		return prev;
	}

	private final Backbone removeChildIntern(
			Wom3Node child,
			boolean check)
	{
		assertWritableOnDocument();

		if (child == null)
			throw new IllegalArgumentException("Argument `child' is null.");

		Backbone remove = Toolbox.expectType(Backbone.class, child, "child");

		if (child.getParentNode() != this)
			throw new IllegalArgumentException("Given node `child' is not a child of this node.");

		if (check)
			this.allowsRemoval(remove);

		Backbone prev = remove.getPreviousSibling();
		Backbone next = remove.getNextSibling();

		// Indicate change before performing it
		++childrenChanges;

		if (remove == getFirstChild())
			setFirstChild(next);
		if (remove == getLastChild())
			setLastChild(prev);
		remove.unlink();

		return prev;
	}

	private final Backbone appendChildIntern(
			Wom3Node child,
			boolean check,
			boolean cloning)
	{
		if (!cloning)
			assertWritableOnDocument();

		if (child == null)
			throw new IllegalArgumentException("Argument `child' is null.");

		final Backbone newChild =
				Toolbox.expectType(Backbone.class, child, "child");

		if (newChild.isLinked())
			throw new IllegalStateException(
					"Given node `child' is still child of another WOM node.");

		Backbone lastChild = (Backbone) getLastChild();

		if (check)
			this.allowsInsertion(lastChild, newChild);

		// Indicate change before performing it
		++childrenChanges;

		newChild.link(this, lastChild, null);
		setLastChild(newChild);
		if (getFirstChild() == null)
			setFirstChild(newChild);

		return lastChild;
	}

	// =========================================================================

	/* TODO: All these methods are called *NoNotify, and yes, they do not 
	 * notify. Seeing how they are only used by the DefinitionListImpl that's 
	 * probably the intention. However, they also prevent checks... is that 
	 * intentional too? Should it be reflected in the name?
	 */

	protected final void insertBeforeNoNotify(Wom3Node before, Wom3Node child)
			throws IllegalArgumentException
	{
		// FIXME: Does not treat DocumentFragment correctly.
		insertBeforeIntern(before, child, true);
	}

	protected final void replaceChildNoNotify(Wom3Node replace, Wom3Node search)
	{
		// FIXME: Does not treat DocumentFragment correctly.
		replaceChildIntern(replace, search, true);
	}

	protected final void removeChildNoNotify(Wom3Node child)
	{
		removeChildIntern(child, true);
	}

	protected final void appendChildNoNotify(Wom3Node child)
	{
		// FIXME: Does not treat DocumentFragment correctly.
		appendChildIntern(child, true /* check */, false /* cloning */);
	}

	// =========================================================================

	/**
	 * Remove all children from this node.
	 * 
	 * Checks assertWritable();
	 */
	protected void clearChildren()
	{
		assertWritableOnDocument();

		while (getFirstChild() != null)
			removeChild(getFirstChild());
	}

	/**
	 * For setting the first child of a node with exactly one child.
	 */
	protected final Wom3Node replaceOrAdd(
			Wom3Node replace,
			Wom3Node replacement,
			boolean required)
	{
		if (replacement == null)
		{
			if (required)
			{
				if (getOwnerDocument().getStrictErrorChecking())
					throw new NullPointerException(
							"Argument is null but child cannot be removed");
			}

			if (replace != null)
			{
				removeChild(replace);
			}
			else
			{
				; // nothing to do, still let caller know that even though the 
					// operation had no side effects, what he's trying to do is nasty.
				assertWritableOnDocument();
			}
		}
		else if (replace == replacement)
		{
			; // nothing to do, still let caller know that even though the 
				// operation had no side effects, what he's trying to do is nasty.
			assertWritableOnDocument();
		}
		else if (replace != null)
		{
			replaceChild(replacement, replace);
		}
		else
		{
			appendChild(replacement);
		}
		return replace;
	}

	/**
	 * For setting the first child of a node with exactly two children.
	 */
	protected final Wom3Node replaceOrInsertBeforeOrAppend(
			Wom3Node replace,
			Wom3Node before,
			Wom3Node replacement,
			boolean required)
	{
		assertWritableOnDocument();

		if (replacement == null)
		{
			if (required)
			{
				if (getOwnerDocument().getStrictErrorChecking())
					throw new NullPointerException(
							"Argument is null but child cannot be removed");
			}

			if (replace != null)
			{
				removeChild(replace);
			}
			else
			{
				; // nothing to do, still let caller know that even though the 
					// operation had no side effects, what he's trying to do is nasty.
				assertWritableOnDocument();
			}
		}
		else if (replace == replacement)
		{
			; // nothing to do, still let caller know that even though the 
				// operation had no side effects, what he's trying to do is nasty.
			assertWritableOnDocument();
		}
		else if (replace != null)
		{
			replaceChild(replacement, replace);
		}
		else if (before != null)
		{
			insertBefore(replacement, before);
		}
		else
		{
			appendChild(replacement);
		}
		return replace;
	}

	/**
	 * For setting the second child of a node with exactly two children.
	 */
	protected final Wom3Node replaceOrAppend(
			Wom3Node replace,
			Wom3Node replacement,
			boolean required)
	{
		if (replacement == null)
		{
			if (required)
			{
				if (getOwnerDocument().getStrictErrorChecking())
					throw new NullPointerException(
							"Argument is null but child cannot be removed");
			}

			if (replace != null)
			{
				removeChild(replace);
			}
			else
			{
				; // nothing to do, still let caller know that even though the 
					// operation had no side effects, what he's trying to do is nasty.
				assertWritableOnDocument();
			}
		}
		else if (replace == replacement)
		{
			; // nothing to do, still let caller know that even though the 
				// operation had no side effects, what he's trying to do is nasty.
			assertWritableOnDocument();
		}
		else if (replace != null)
		{
			replaceChild(replacement, replace);
		}
		else
		{
			appendChild(replacement);
		}
		return replace;
	}

	// =========================================================================

	protected void checkInsertion(
			Backbone prev,
			Backbone child,
			ChildDescriptor[] desc)
	{
		if (getOwnerDocument().getStrictErrorChecking())
			new BackboneChildOperationChecker(this).checkInsertion(prev, child, desc);
	}

	protected void checkRemoval(
			Backbone child,
			ChildDescriptor[] desc)
	{
		if (getOwnerDocument().getStrictErrorChecking())
			new BackboneChildOperationChecker(this).checkRemoval(child, desc);
	}

	protected void checkReplacement(
			Backbone oldChild,
			Backbone newChild,
			ChildDescriptor[] desc)
	{
		if (getOwnerDocument().getStrictErrorChecking())
			new BackboneChildOperationChecker(this).checkReplacement(oldChild, newChild, desc);
	}

	// =========================================================================

	public static ChildDescriptor childDesc(String tag)
	{
		return childDesc(WOM_NS_URI, tag, 0);
	}

	public static ChildDescriptor childDesc(String namespaceUri, String tag)
	{
		return new ChildDescriptor(namespaceUri, tag, 0);
	}

	public static ChildDescriptor childDesc(String tag, int flags)
	{
		return childDesc(WOM_NS_URI, tag, flags);
	}

	public static ChildDescriptor childDesc(
			String namespaceUri,
			String tag,
			int flags)
	{
		return new ChildDescriptor(namespaceUri, tag, flags);
	}

	public static Map<String, AttributeDescriptor> attrDescMap(
			RuntimeAttributeDescriptor... descriptors)
	{
		HashMap<String, AttributeDescriptor> map = new HashMap<String, AttributeDescriptor>();
		for (RuntimeAttributeDescriptor desc : descriptors)
			map.put(desc.getName(), desc);
		return map;
	}

	public static RuntimeAttributeDescriptor attrDesc(String name)
	{
		return attrDesc(
				name,
				Normalization.NONE);
	}

	public static RuntimeAttributeDescriptor attrDesc(
			String name,
			Normalization normalization)
	{
		return attrDesc(
				name,
				true,
				false,
				null,
				null,
				normalization);
	}

	public static RuntimeAttributeDescriptor attrDesc(
			String name,
			boolean removable,
			boolean readOnly,
			AttributeVerificationAndConverion verifyAndConvert,
			AttributeCustomAction customAction,
			Normalization normalization)
	{
		return new RuntimeAttributeDescriptor(
				name,
				removable,
				readOnly,
				verifyAndConvert,
				customAction,
				normalization);
	}

	// =========================================================================

	protected void doesNotAllowInsertion(
			Backbone prev,
			Backbone child)
	{
		if (prev == null)
		{
			if (getFirstChild() != null)
			{
				throw new IllegalArgumentException(
						getNodeName() + " doesn't accept child " +
								child.getNodeName() + " in front of child " +
								getFirstChild().getNodeName());
			}
			else
			{
				throw new IllegalArgumentException(
						getNodeName() + " doesn't accept child " +
								child.getNodeName() + " as first child");
			}
		}
		else
		{
			throw new IllegalArgumentException(
					getNodeName() + " doesn't accept child " +
							child.getNodeName() + " after child " +
							prev.getNodeName());
		}
	}

	protected void doesNotAllowRemoval(Backbone child)
	{
		throw new IllegalArgumentException("Child node " + child.getNodeName() +
				" cannot be removed from node " + getNodeName());
	}

	protected void doesNotAllowReplacement(
			Backbone oldChild,
			Backbone newChild)
	{
		throw new IllegalArgumentException("Child node " +
				oldChild.getNodeName() + " in node " + getNodeName() +
				" cannot be replaced by node " + newChild.getNodeName());
	}
}
