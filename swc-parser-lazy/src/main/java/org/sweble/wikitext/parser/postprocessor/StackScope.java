package org.sweble.wikitext.parser.postprocessor;

public enum StackScope
{
	GENERAL_SCOPE_WITHOUT_LAZY_PARSED_PAGE
	{
		@Override
		public boolean isInList(ElementType nodeType)
		{
			switch (nodeType)
			{
			// APPLET, MARQUEE, OBJECT
			// MI, MO, MN, MS, MTEXT, ANNOTATION
			// FORAIGNOBJECT, DESC, TITLE
				case SECTION_HEADING:
				case SECTION_BODY:
				case CAPTION:
				case TABLE:
				case TD:
				case TH:
					return true;
				default:
					return false;
			}
		}
	},
	
	GENERAL_SCOPE
	{
		@Override
		public boolean isInList(ElementType nodeType)
		{
			switch (nodeType)
			{
				case PAGE:
					return true;
				default:
					return GENERAL_SCOPE_WITHOUT_LAZY_PARSED_PAGE.isInList(nodeType);
			}
		}
	},
	
	BUTTON_SCOPE
	{
		@Override
		public boolean isInList(ElementType nodeType)
		{
			switch (nodeType)
			{
			/*
			case BUTTON:
				return true;
			*/
				default:
					return GENERAL_SCOPE.isInList(nodeType);
			}
		}
	},
	
	LIST_ITEM_SCOPE
	{
		@Override
		public boolean isInList(ElementType nodeType)
		{
			switch (nodeType)
			{
				case UL:
				case OL:
					return true;
				default:
					return GENERAL_SCOPE.isInList(nodeType);
			}
		}
	},
	
	TABLE_SCOPE
	{
		@Override
		public boolean isInList(ElementType nodeType)
		{
			switch (nodeType)
			{
				case SECTION_HEADING:
				case SECTION_BODY:
				case TABLE:
				case PAGE:
					return true;
				default:
					return false;
			}
		}
	};
	
	public abstract boolean isInList(ElementType nodeType);
}
