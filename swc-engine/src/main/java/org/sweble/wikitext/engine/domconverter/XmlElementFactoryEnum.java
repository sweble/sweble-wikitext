package org.sweble.wikitext.engine.domconverter;

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
	
	/*
	CENTER
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new CENTERAdapter(e);
		}
	},
	
	DL
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new DLAdapter(e);
		}
	},
	
	DIV
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new DIVAdapter(e);
		}
	},
	
	HR
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new HRAdapter(e);
		}
	},
	
	OL
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new OLAdapter(e);
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
	
	UL
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new ULAdapter(e);
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
	
	LI
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new LIAdapter(e);
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
	ABBR
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new XmlElementAbbrAdapter(e);
		}
	},
	/*
	BIG
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new BIGAdapter(e);
		}
	},
	
	B
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new BAdapter(e);
		}
	},
	
	CITE
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new CITEAdapter(e);
		}
	},
	
	CODE
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new CODEAdapter(e);
		}
	},
	
	DEL
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new DELAdapter(e);
		}
	},
	
	DFN
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new DFNAdapter(e);
		}
	},
	
	EM
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new EMAdapter(e);
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
	
	I
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new IAdapter(e);
		}
	},
	
	KBD
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new KBDAdapter(e);
		}
	},
	
	BR
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new BRAdapter(e);
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
	
	SAMP
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new SAMPAdapter(e);
		}
	},
	
	SMALL
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new SMALLAdapter(e);
		}
	},
	
	SPAN
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new SPANAdapter(e);
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
	
	STRONG
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new STRONGAdapter(e);
		}
	},
	
	SUB
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new SUBAdapter(e);
		}
	},
	
	SUP
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new SUPAdapter(e);
		}
	},
	
	TT
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new TTAdapter(e);
		}
	},
	
	U
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new UAdapter(e);
		}
	},
	
	VAR
	{
		@Override
		public DomNode create(XmlElement e)
		{
			return new UAdapter(e);
		}
	}*/;
	
	public abstract DomNode create(XmlElement e);
}
