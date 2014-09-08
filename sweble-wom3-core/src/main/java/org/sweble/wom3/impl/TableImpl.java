/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3Color;
import org.sweble.wom3.Wom3HorizAlign;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3Table;
import org.sweble.wom3.Wom3TableBody;
import org.sweble.wom3.Wom3TableCaption;
import org.sweble.wom3.Wom3TableFrame;
import org.sweble.wom3.Wom3TablePartition;
import org.sweble.wom3.Wom3TableRules;
import org.sweble.wom3.Wom3ValueWithUnit;

public class TableImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3Table
{
	private static final long serialVersionUID = 1L;
	
	/* Doesn't work for tables. They are too complex (the caption can occur anywhere...)
	private static final ChildDescriptor[] BODY_DESCRIPTOR = {
			childDesc("caption"),
			descChild("tbody", ChildDescriptor.MULTIPLE) };
	*/
	
	private Wom3TableCaption caption;
	
	/*
	// TODO: A MediaWiki table may only have one body, but others might have the full set!
	private Wom3TableBody body;
	*/
	
	private ChildrenSubset<Wom3TablePartition> partitions =
			new ChildrenSubset<Wom3TablePartition>(1);
	
	// =========================================================================
	
	public TableImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "table";
	}
	
	// =========================================================================
	
	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
		if (getOwnerDocument().getStrictErrorChecking())
		{
			if (BackboneChildOperationChecker.isIgnoredDefault(child))
				return;
			if ((child instanceof Wom3TableCaption) && (getCaption() == null))
				return;
			if (child instanceof Wom3TableBody)
				return;
			doesNotAllowInsertion(prev, child);
			//checkInsertion(prev, child, BODY_DESCRIPTOR);
		}
	}
	
	@Override
	protected void allowsRemoval(Backbone child)
	{
		//checkRemoval(child, BODY_DESCRIPTOR);
	}
	
	@Override
	protected void allowsReplacement(Backbone oldChild, Backbone newChild)
	{
		if (getOwnerDocument().getStrictErrorChecking())
		{
			/**
			 * Since all children are optional and there can be any number of
			 * ignored children or partitions, any node can always be replaced
			 * by a partition or an ignored node.
			 */
			if (newChild instanceof Wom3TableBody)
				return;
			if (BackboneChildOperationChecker.isIgnoredDefault(newChild))
				return;
			
			/**
			 * Only one caption may be child of this node. So if we replace with
			 * a caption we must make sure that either the existing caption is
			 * replaced or that there is no caption yet.
			 */
			if ((newChild instanceof Wom3TableCaption) &&
					((oldChild == getCaption()) || (getCaption() == null)))
				return;
			
			doesNotAllowReplacement(oldChild, newChild);
			//checkReplacement(oldChild, newChild, BODY_DESCRIPTOR);
		}
	}
	
	@Override
	public void childInserted(Backbone prev, Backbone added)
	{
		if (added instanceof Wom3TableCaption)
		{
			this.caption = (Wom3TableCaption) added;
		}
		else if (added instanceof Wom3TableBody)
		{
			this.partitions.insertAfter(prev, Wom3TablePartition.class, (Wom3TableBody) added);
		}
	}
	
	@Override
	public void childRemoved(Backbone prev, Backbone removed)
	{
		if (removed == this.caption)
		{
			this.caption = null;
		}
		else
		{
			this.partitions.remove(removed);
		}
	}
	
	// =========================================================================
	
	@Override
	public Wom3HorizAlign getAlign()
	{
		return getAlignAttr("align");
	}
	
	@Override
	public Wom3HorizAlign setAlign(Wom3HorizAlign align)
	{
		return setAlignAttr(Attributes.ALIGN, "align", align);
	}
	
	@Override
	public Integer getBorder()
	{
		return getIntAttr("border");
	}
	
	@Override
	public Integer setBorder(Integer thickness)
	{
		return setIntAttr(Attributes.BORDER, "border", thickness);
	}
	
	@Override
	public Wom3Color getBgColor()
	{
		return getColorAttr("bgcolor");
	}
	
	@Override
	public Wom3Color setBgColor(Wom3Color color)
	{
		return setColorAttr(Attributes.BGCOLOR, "bgcolor", color);
	}
	
	@Override
	public Wom3ValueWithUnit getCellPadding()
	{
		return getValueWithUnitAttr("cellpadding");
	}
	
	@Override
	public Wom3ValueWithUnit setCellPadding(Wom3ValueWithUnit padding)
	{
		return setValueWithUnitAttr(Attributes.CELLPADDING, "cellpadding", padding);
	}
	
	@Override
	public Wom3ValueWithUnit getCellSpacing()
	{
		return getValueWithUnitAttr("cellspacing");
	}
	
	@Override
	public Wom3ValueWithUnit setCellSpacing(Wom3ValueWithUnit spacing)
	{
		return setValueWithUnitAttr(Attributes.CELLSPACING, "cellspacing", spacing);
	}
	
	@Override
	public Wom3TableFrame getFrame()
	{
		return (Wom3TableFrame) getAttributeNativeData("frame");
	}
	
	@Override
	public Wom3TableFrame setFrame(Wom3TableFrame frame)
	{
		return setAttributeDirect(Attributes.FRAME, "frame", frame);
	}
	
	@Override
	public Wom3TableRules getRules()
	{
		return (Wom3TableRules) getAttributeNativeData("rules");
	}
	
	@Override
	public Wom3TableRules setRules(Wom3TableRules rules)
	{
		return setAttributeDirect(Attributes.RULES, "rules", rules);
	}
	
	@Override
	public String getSummary()
	{
		return getStringAttr("summary");
	}
	
	@Override
	public String setSummary(String summary)
	{
		return setStringAttr(Attributes.SUMMARY, "summary", summary);
	}
	
	@Override
	public Wom3ValueWithUnit getWidth()
	{
		return getValueWithUnitAttr("width");
	}
	
	@Override
	public Wom3ValueWithUnit setWidth(Wom3ValueWithUnit width)
	{
		return setValueWithUnitAttr(Attributes.WIDTH, "width", width);
	}
	
	// =========================================================================
	
	@Override
	public Wom3TableCaption getCaption()
	{
		return caption;
	}
	
	@Override
	public Wom3TableCaption setCaption(Wom3TableCaption caption)
	{
		return (Wom3TableCaption) replaceOrInsertBeforeOrAppend(
				this.caption, this.partitions.getFirstOrNull(), caption, false);
		/*
		WomTableCaption old = this.caption;
		if (this.caption != null)
		{
			replaceChildNoNotify(this.caption, caption);
		}
		else
		{
			if (this.body != null)
				insertBeforeNoNotify(this.body, caption);
			else
				appendChildNoNotify(caption);
		}
		this.caption = Toolbox.expectType(WomTableCaption.class, caption);
		return old;
		*/
	}
	
	@Override
	public Collection<Wom3TablePartition> getPartitions()
	{
		return Collections.unmodifiableCollection(this.partitions);
	}
	
	//	@Override
	//	public Wom3TableBody getBody()
	//	{
	//		return body;
	//	}
	//	
	//	@Override
	//	public Wom3TableBody setBody(Wom3TableBody body)
	//	{
	//		return (Wom3TableBody) replaceOrAppend(this.body, body, false);
	//		/*
	//		WomTableBody old = this.body;
	//		if (this.body != null)
	//		{
	//			replaceChildNoNotify(this.body, body);
	//		}
	//		else
	//		{
	//			appendChildNoNotify(body);
	//		}
	//		this.body = Toolbox.expectType(WomTableBody.class, body);
	//		return old;
	//		*/
	//	}
	
	// =========================================================================
	
	private static final Map<String, AttributeDescriptor> nameMap = getNameMap();
	
	public static Map<String, AttributeDescriptor> getNameMap()
	{
		Map<String, AttributeDescriptor> nameMap =
				new HashMap<String, AttributeDescriptor>();
		
		nameMap.putAll(UniversalAttributes.getNameMap());
		nameMap.put("align", Attributes.ALIGN);
		nameMap.put("border", Attributes.BORDER);
		nameMap.put("bgcolor", Attributes.BGCOLOR);
		nameMap.put("cellpadding", Attributes.CELLPADDING);
		nameMap.put("cellspacing", Attributes.CELLSPACING);
		nameMap.put("frame", Attributes.FRAME);
		nameMap.put("rules", Attributes.RULES);
		nameMap.put("summary", Attributes.SUMMARY);
		nameMap.put("width", Attributes.WIDTH);
		
		return nameMap;
	}
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDesc(namespaceUri, localName, qualifiedName, nameMap);
	}
	
	public static enum Attributes implements AttributeDescriptor
	{
		ALIGN
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.LCR_ALIGN.verifyAndConvert(parent, verified);
			}
		},
		BORDER
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.PIXELS.verifyAndConvert(parent, verified);
			}
		},
		BGCOLOR
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.COLOR.verifyAndConvert(parent, verified);
			}
		},
		CELLPADDING
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.LENGTH.verifyAndConvert(parent, verified);
			}
		},
		CELLSPACING
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.LENGTH.verifyAndConvert(parent, verified);
			}
		},
		FRAME
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.FRAME.verifyAndConvert(parent, verified);
			}
		},
		RULES
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.RULES.verifyAndConvert(parent, verified);
			}
		},
		SUMMARY
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return true;
			}
		},
		WIDTH
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.LENGTH.verifyAndConvert(parent, verified);
			}
		};
		
		// =====================================================================
		
		@Override
		public boolean isRemovable()
		{
			return true;
		}
		
		@Override
		public Normalization getNormalizationMode()
		{
			return Normalization.CDATA;
		}
		
		@Override
		public void customAction(
				Wom3Node parent,
				AttributeBase oldAttr,
				AttributeBase newAttr)
		{
		}
	}
	
}
