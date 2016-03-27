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
			/*
			case SECTION_HEADING:
			case SECTION_BODY:
			*/
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
