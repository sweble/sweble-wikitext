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
package org.sweble.wikitext.lazy.postprocessor;

public enum ScopeType
{
	WT_ELEMENT_BODY
	{
		@Override
		public boolean isWikitextScope()
		{
			return true;
		}
	},
	
	PAGE
	{
		@Override
		public boolean isWikitextScope()
		{
			return true;
		}
	},
	
	// =========================================================================
	
	WT_INLINE
	{
		@Override
		public boolean isWikitextScope()
		{
			return true;
		}
	},
	
	WT_BLOCK
	{
		@Override
		public boolean isWikitextScope()
		{
			return true;
		}
		
		@Override
		public boolean isBlockScope()
		{
			return true;
		}
		
		@Override
		public boolean isCloseInline()
		{
			return true; // takes care of isReopenClosedInline
		}
	},
	
	WT_TABLE
	{
		@Override
		public boolean isWikitextScope()
		{
			return true;
		}
		
		@Override
		public boolean isBlockScope()
		{
			return true;
		}
		
		@Override
		public boolean isTableScope()
		{
			return true;
		}
		
		@Override
		public boolean isCloseInline()
		{
			return true;
		}
		
		@Override
		public boolean isReopenClosedInline()
		{
			return false;
		}
		
		@Override
		public boolean isPropagateOut()
		{
			return false;
		}
		
		@Override
		public boolean isContinueClosed()
		{
			return true;
		}
	},
	
	WT_TABLE_ITEM
	{
		@Override
		public boolean isWikitextScope()
		{
			return true;
		}
		
		@Override
		public boolean isCloseInline()
		{
			return true;
		}
		
		@Override
		public boolean isReopenClosedInline()
		{
			return false;
		}
		
		@Override
		public boolean isPropagateOut()
		{
			return false; // isContinueClosed
		}
	},
	
	// =========================================================================
	
	XML_INLINE
	{
		@Override
		public boolean isInlineScope()
		{
			return true;
		}
		
		@Override
		public boolean isClosableBeforeBlock()
		{
			return true; // takes care of isReopen()
		}
	},
	
	XML_BLOCK
	{
		@Override
		public boolean isBlockScope()
		{
			return true;
		}
		
		@Override
		public boolean isCloseInline()
		{
			return true; // isReopenClosedInline
		}
	},
	
	XML_PARAGRAPH
	{
		@Override
		public boolean isCloseInline()
		{
			return true; // isReopenClosedInline
		}
		
		@Override
		public boolean isClosableBeforeBlock()
		{
			return true;
		}
		
		@Override
		public boolean isReopen()
		{
			return false;
		}
		
		@Override
		public boolean isCloseSamePred()
		{
			return true;
		}
		
		@Override
		public boolean dropIfOnlyWhitespace()
		{
			return true;
		}
	},
	
	XML_ITEM
	{
		@Override
		public boolean isCloseInline()
		{
			return true; // isReopenClosedInline
		}
		
		@Override
		public boolean isCloseSamePred()
		{
			return true;
		}
	},
	
	XML_TABLE
	{
		@Override
		public boolean isBlockScope()
		{
			return true;
		}
		
		@Override
		public boolean isTableScope()
		{
			return true;
		}
		
		@Override
		public boolean isCloseInline()
		{
			return true;
		}
		
		@Override
		public boolean isReopenClosedInline()
		{
			return false;
		}
		
		@Override
		public boolean isPropagateOut()
		{
			return false;
		}
		
		@Override
		public boolean isContinueClosed()
		{
			return true;
		}
	},
	
	XML_TABLE_ITEM
	{
		@Override
		public boolean isCloseInline()
		{
			return true;
		}
		
		@Override
		public boolean isReopenClosedInline()
		{
			return false;
		}
		
		@Override
		public boolean isPropagateOut()
		{
			return false; // takes care of isContinueClosed
		}
		
		@Override
		public boolean isCloseSamePred()
		{
			return true;
		}
	};
	
	// =========================================================================
	
	public boolean isWikitextScope()
	{
		return false;
	}
	
	public boolean isInlineScope()
	{
		return false;
	}
	
	public boolean isBlockScope()
	{
		return false;
	}
	
	public boolean isTableScope()
	{
		return false;
	}
	
	// =========================================================================
	
	/**
	 * Whether an opening tag closes preceding scopes that return true for
	 * isClosableBeforeBlock() (table, div, p, ...).
	 */
	public boolean isCloseInline()
	{
		return false;
	}
	
	/**
	 * Whether scopes that have been closed in front of an opening tag
	 * (isCloseInline) will be reopened inside the scope of the opening tag
	 * (div, p, NOT table).
	 */
	public boolean isReopenClosedInline()
	{
		return isCloseInline();
	}
	
	/**
	 * Whether an element can be forcibly closed in front of an opening tag that
	 * returns true for isCloseInline() (inline elements, paragraph).
	 * 
	 * @return
	 */
	public boolean isClosableBeforeBlock()
	{
		return false;
	}
	
	/**
	 * If an element can be re-opened after it was forcibly closed (inline
	 * elements).
	 */
	public boolean isReopen()
	{
		return isClosableBeforeBlock();
	}
	
	/**
	 * If an opening tag automatically closes a preceding tag with the same name
	 * (li, td, ...).
	 */
	public boolean isCloseSamePred()
	{
		return false;
	}
	
	/**
	 * Some elements (tables) close inline elements in front of their opening
	 * tag, DON'T propagate their effects into the table but continue the closed
	 * elements AFTER the closing element of the table.
	 */
	public boolean isContinueClosed()
	{
		return isPropagateOut();
	}
	
	/**
	 * If elements closed in front of the closing tag of scome scope will be
	 * continued after that closing tag (inline, block, NOT table).
	 */
	public boolean isPropagateOut()
	{
		return true;
	}
	
	/**
	 * When closing an element which has no content should we drop it entirely
	 * instead?
	 */
	public boolean dropIfEmpty()
	{
		return true;
	}
	
	/**
	 * When closing an element which contains only whitespace should we drop it
	 * entirely instead?
	 */
	public boolean dropIfOnlyWhitespace()
	{
		return false;
	}
}
