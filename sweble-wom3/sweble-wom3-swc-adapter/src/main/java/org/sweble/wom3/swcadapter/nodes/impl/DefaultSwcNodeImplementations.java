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
		// TODO: Still missing: xml-char-ref, xml-entity-ref, redirect, mww:intlink
		ArrayList<SwcNodeImplInfo> implsTmp = new ArrayList<SwcNodeImplInfo>();
		implsTmp.add(new SwcNodeImplInfo(SwcNode.MWW_NS_URI, "arg", ArgImpl.class));
		implsTmp.add(new SwcNodeImplInfo(SwcNode.MWW_NS_URI, "attr", AttrImpl.class));
		implsTmp.add(new SwcNodeImplInfo(SwcNode.MWW_NS_URI, "body", BodyImpl.class));
		implsTmp.add(new SwcNodeImplInfo(SwcNode.MWW_NS_URI, "name", NameImpl.class));
		implsTmp.add(new SwcNodeImplInfo(SwcNode.MWW_NS_URI, "garbage", GarbageImpl.class));
		implsTmp.add(new SwcNodeImplInfo(SwcNode.MWW_NS_URI, "param", ParamImpl.class));
		implsTmp.add(new SwcNodeImplInfo(SwcNode.MWW_NS_URI, "tagext", TagExtensionImpl.class));
		implsTmp.add(new SwcNodeImplInfo(SwcNode.MWW_NS_URI, "tagext-body", TagExtBodyImpl.class));
		implsTmp.add(new SwcNodeImplInfo(SwcNode.MWW_NS_URI, "transclusion", TransclusionImpl.class));
		implsTmp.add(new SwcNodeImplInfo(SwcNode.MWW_NS_URI, "value", ValueImpl.class));
		implsTmp.add(new SwcNodeImplInfo(SwcNode.MWW_NS_URI, "xmlelement", XmlElementImpl.class));

		impls = Collections.unmodifiableList(implsTmp);
	}

	public static Collection<SwcNodeImplInfo> get()
	{
		return impls;
	}
}
