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

package org.sweble.wikitext.parser.parser;

public enum ParserScopes
{
	PAGE
	{
		@Override
		public boolean accepts(ParserAtoms atom)
		{
			return true;
		}
	},

	/* That is not even a thing. I don't know why I've added it...
	INTERNAL_LINK_ALT
	{
		@Override
		public boolean accepts(ParserAtoms atom)
		{
			switch (atom)
			{
			//case SECTIONS: // oh the horror ...
			//case INTERNAL_LINK: // terminates the outer internal link
				case LIST:
				case SEMI_PRE:
				case EXTERNAL_LINK:
				case PLAIN_EXTERNAL_LINK:
					return false;
				default:
					return true;
			}
		}

		@Override
		public boolean isSticky()
		{
			return true;
		}
	},
	*/

	IMAGE_LINK_ALT
	{
		@Override
		public boolean accepts(ParserAtoms atom)
		{
			switch (atom)
			{
			//case SECTIONS: // oh the horror ...
			//case INTERNAL_LINK: // terminates the outer internal link
				case LIST:
				case SEMI_PRE:
					/* It appears that external links are properly parsed in MediaWiki and then stripped of their 
					 * syntactic sugar before being rendered into the alt="" attribute. Therefore we must not exclude
					 * external links from parsing.
				case EXTERNAL_LINK:
					*/
				case PLAIN_EXTERNAL_LINK:
					return false;
				default:
					return true;
			}
		}

		@Override
		public boolean isSticky()
		{
			return true;
		}
	},

	INTERNAL_LINK_TITLE
	{
		@Override
		public boolean accepts(ParserAtoms atom)
		{
			switch (atom)
			{
			//case SECTIONS: // oh the horror ...
			//case INTERNAL_LINK: // terminates the outer internal link
				case LIST:
				case SEMI_PRE:
				case EXTERNAL_LINK:
				case PLAIN_EXTERNAL_LINK:
					return false;
				default:
					return true;
			}
		}

		@Override
		public boolean isSticky()
		{
			return true;
		}
	},

	IMAGE_LINK_TITLE
	{
		@Override
		public boolean accepts(ParserAtoms atom)
		{
			switch (atom)
			{
			//case SECTIONS: // oh the horror ...
			//case INTERNAL_LINK: // terminates the outer internal link
				case LIST:
				case SEMI_PRE:
					/* I don't know why those were forbidden. After all an image's caption can contain all kinds of links...
				case EXTERNAL_LINK:
				case PLAIN_EXTERNAL_LINK:
					*/
					return false;
				default:
					return true;
			}
		}

		@Override
		public boolean isSticky()
		{
			return true;
		}
	},

	EXTERNAL_LINK_TITLE
	{
		@Override
		public boolean accepts(ParserAtoms atom)
		{
			switch (atom)
			{
				case INTERNAL_LINK:
					return true;
				default:
					return false;
			}
		}
	},

	LIST_ITEM
	{
		@Override
		public boolean accepts(ParserAtoms atom)
		{
			switch (atom)
			{
				case EXTERNAL_LINK:
				case INTERNAL_LINK:
				case PLAIN_EXTERNAL_LINK:
					return true;
				default:
					return false;
			}
		}
	},

	SEMI_PRE
	{
		@Override
		public boolean accepts(ParserAtoms atom)
		{
			switch (atom)
			{
				case EXTERNAL_LINK:
				case INTERNAL_LINK:
				case PLAIN_EXTERNAL_LINK:
					return true;
				default:
					return false;
			}
		}
	},

	SECTION_HEADING
	{
		@Override
		public boolean accepts(ParserAtoms atom)
		{
			switch (atom)
			{
				case EXTERNAL_LINK:
				case INTERNAL_LINK:
				case PLAIN_EXTERNAL_LINK:
					return true;
				default:
					return false;
			}
		}

		@Override
		public boolean isNoEolScope()
		{
			return true;
		}

		@Override
		public boolean isSticky()
		{
			return true;
		}
	},

	SECTION_BODY
	{
		@Override
		public boolean accepts(ParserAtoms atom)
		{
			switch (atom)
			{
				case SECTIONS:
					return false;
				default:
					return true;
			}
		}
	},

	TABLE_ELEMENTS
	{
		@Override
		public boolean accepts(ParserAtoms atom)
		{
			return true;
		}

		@Override
		public boolean isSticky()
		{
			return true;
		}
	},

	TABLE_CELL
	{
		@Override
		public boolean accepts(ParserAtoms atom)
		{
			return true;
		}
	},

	TABLE_INLINE_CELL
	{
		@Override
		public boolean accepts(ParserAtoms atom)
		{
			switch (atom)
			{
				case EXTERNAL_LINK:
				case INTERNAL_LINK:
				case PLAIN_EXTERNAL_LINK:
					return true;
				default:
					return false;
			}
		}

		@Override
		public boolean isNoEolScope()
		{
			return true;
		}

		@Override
		public boolean isSticky()
		{
			return true;
		}
	},

	TABLE_INLINE_HEADER
	{
		@Override
		public boolean accepts(ParserAtoms atom)
		{
			switch (atom)
			{
				case EXTERNAL_LINK:
				case INTERNAL_LINK:
				case PLAIN_EXTERNAL_LINK:
					return true;
				default:
					return false;
			}
		}

		@Override
		public boolean isNoEolScope()
		{
			return true;
		}

		@Override
		public boolean isSticky()
		{
			return true;
		}
	},

	LCT_PROTECTED_CONTENT
	{
		@Override
		public boolean accepts(ParserAtoms atom)
		{
			return true;
		}

		@Override
		public boolean isNoEolScope()
		{
			return false;
		}

		@Override
		public boolean isSticky()
		{
			return false;
		}
	},

	LCT_RULE_TEXT
	{
		@Override
		public boolean accepts(ParserAtoms atom)
		{
			return true;
		}

		@Override
		public boolean isNoEolScope()
		{
			return false;
		}

		@Override
		public boolean isSticky()
		{
			return false;
		}
	};

	public abstract boolean accepts(ParserAtoms atom);

	public boolean isSticky()
	{
		return false;
	}

	public boolean isNoEolScope()
	{
		return false;
	}
}
