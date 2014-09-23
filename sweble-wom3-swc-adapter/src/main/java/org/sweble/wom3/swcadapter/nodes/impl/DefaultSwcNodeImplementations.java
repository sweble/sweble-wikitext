package org.sweble.wom3.swcadapter.nodes.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.sweble.wom3.Wom3ElementNode;
import org.sweble.wom3.swcadapter.nodes.SwcNode;

public class DefaultSwcNodeImplementations
{
	private static final List<SwcNodeImplInfo> impls;
	
	public static final class SwcNodeImplInfo
	{
		private final String namespaceUri;
		
		private final String localPart;
		
		private final Class<? extends Wom3ElementNode> clazz;
		
		public SwcNodeImplInfo(
				String namespaceUri,
				String localPart,
				Class<? extends Wom3ElementNode> clazz)
		{
			super();
			this.namespaceUri = namespaceUri;
			this.localPart = localPart;
			this.clazz = clazz;
		}
		
		public Class<? extends Wom3ElementNode> getImpl()
		{
			return clazz;
		}
		
		public String getLocalPart()
		{
			return localPart;
		}
		
		public String getNamespaceUri()
		{
			return namespaceUri;
		}
	}
	
	static
	{
		ArrayList<SwcNodeImplInfo> implsTmp = new ArrayList<SwcNodeImplInfo>();
		implsTmp.add(new SwcNodeImplInfo(SwcNode.MWW_NS_URI, "arg", ArgImpl.class));
		implsTmp.add(new SwcNodeImplInfo(SwcNode.MWW_NS_URI, "name", NameImpl.class));
		implsTmp.add(new SwcNodeImplInfo(SwcNode.MWW_NS_URI, "transclusion", TransclusionImpl.class));
		implsTmp.add(new SwcNodeImplInfo(SwcNode.MWW_NS_URI, "value", ValueImpl.class));
		
		impls = Collections.unmodifiableList(implsTmp);
	}
	
	public static Collection<SwcNodeImplInfo> get()
	{
		return impls;
	}
}
