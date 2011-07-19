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
package org.sweble.wikitext.engine.astdom;

import org.sweble.wikitext.engine.dom.DomNode;
import org.sweble.wikitext.lazy.parser.XmlElement;

public enum XmlElementFactoryEnum
{
	BLOCKQUOTE
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementBlockquoteAdapter(e);
		}
	},
	
	DIV
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementDivAdapter(e);
		}
	},
	
	// =========================================================================
	
	HR
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementHrAdapter(e);
		}
	},
	
	// =========================================================================
	
	OL
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementOrderedListAdapter(e);
		}
	},
	
	UL
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementUnorderedListAdapter(e);
		}
	},
	
	LI
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementListItemAdapter(e);
		}
	},
	
	// =========================================================================
	
	/*
	DL
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new DLAdapter(e);
		}
	},

	DD
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new DDAdapter(e);
		}
	},
	
	DT
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new DTAdapter(e);
		}
	},
	
	P
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new PAdapter(e);
		}
	},
	
	PRE
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new PREAdapter(e);
		}
	},
	
	TABLE
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new TABLEAdapter(e);
		}
	},
	
	TD
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new TDAdapter(e);
		}
	},
	
	TR
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new TRAdapter(e);
		}
	},
	*/

	// =========================================================================
	
	ABBR
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementAbbrAdapter(e);
		}
	},
	
	BIG
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementBigAdapter(e);
		}
	},
	
	B
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementBoldAdapter(e);
		}
	},
	
	BR
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementBrAdapter(e);
		}
	},
	
	CITE
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementCiteAdapter(e);
		}
	},
	
	CODE
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementCodeAdapter(e);
		}
	},
	
	DFN
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementDfnAdapter(e);
		}
	},
	
	EM
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementEmAdapter(e);
		}
	},
	
	I
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementItalicsAdapter(e);
		}
	},
	
	KBD
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementKbdAdapter(e);
		}
	},
	
	SAMP
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementSampAdapter(e);
		}
	},
	
	SMALL
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementSmallAdapter(e);
		}
	},
	
	SPAN
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementSpanAdapter(e);
		}
	},
	
	STRONG
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementStrongAdapter(e);
		}
	},
	
	SUB
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementSubAdapter(e);
		}
	},
	
	SUP
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementSupAdapter(e);
		}
	},
	
	TT
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementTeletypeAdapter(e);
		}
	},
	
	VAR
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementVarAdapter(e);
		}
	},
	
	// =========================================================================
	
	DEL
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementDelAdapter(e);
		}
	},
	
	INS
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementInsAdapter(e);
		}
	},
	
	// =========================================================================
	
	/*
	CENTER
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new CENTERAdapter(e);
		}
	},
	
	FONT
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new FONTAdapter(e);
		}
	},
	
	S
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new SAdapter(e);
		}
	},
	
	STRIKE
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new STRIKEAdapter(e);
		}
	},
	
	U
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new UAdapter(e);
		}
	}*/;
	
	public abstract DomNode create(XmlElement e);
}
