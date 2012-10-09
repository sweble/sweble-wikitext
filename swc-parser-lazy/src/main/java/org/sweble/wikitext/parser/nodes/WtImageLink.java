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

package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.nodes.WtLinkTarget.LinkTargetType;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.Uninitialized;

public class WtImageLink
		extends
			WtInnerNode3
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtImageLink()
	{
		super(Uninitialized.X);
	}
	
	protected WtImageLink(
			WtPageName target,
			WtLinkOptions options,
			ImageViewFormat format,
			boolean border,
			ImageHorizAlign hAlign,
			ImageVertAlign vAlign,
			int width,
			int height,
			boolean upright,
			ImageLinkTarget link,
			WtLinkOptionAltText alt)
	{
		super(target, options, WtLinkTitle.NO_TITLE);
		setFormat(format);
		setBorder(border);
		setHAlign(hAlign);
		setVAlign(vAlign);
		setWidth(width);
		setHeight(height);
		setUpright(upright);
		setLink(link);
		setAlt(alt);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_IMAGE_LINK;
	}
	
	// =========================================================================
	// Properties
	
	private int width;
	
	public final int getWidth()
	{
		return this.width;
	}
	
	public final int setWidth(int width)
	{
		int old = this.width;
		this.width = width;
		return old;
	}
	
	private int height;
	
	public final int getHeight()
	{
		return this.height;
	}
	
	public final int setHeight(int height)
	{
		int old = this.height;
		this.height = height;
		return old;
	}
	
	private boolean upright;
	
	public final boolean getUpright()
	{
		return this.upright;
	}
	
	public final boolean setUpright(boolean upright)
	{
		boolean old = this.upright;
		this.upright = upright;
		return old;
	}
	
	private ImageHorizAlign hAlign;
	
	public final ImageHorizAlign getHAlign()
	{
		return this.hAlign;
	}
	
	public final ImageHorizAlign setHAlign(ImageHorizAlign hAlign)
	{
		if (hAlign == null)
			throw new NullPointerException();
		ImageHorizAlign old = this.hAlign;
		this.hAlign = hAlign;
		return old;
	}
	
	private ImageVertAlign vAlign;
	
	public final ImageVertAlign getVAlign()
	{
		return this.vAlign;
	}
	
	public final ImageVertAlign setVAlign(ImageVertAlign vAlign)
	{
		if (vAlign == null)
			throw new NullPointerException();
		ImageVertAlign old = this.vAlign;
		this.vAlign = vAlign;
		return old;
	}
	
	private ImageViewFormat format;
	
	public final ImageViewFormat getFormat()
	{
		return this.format;
	}
	
	public final ImageViewFormat setFormat(ImageViewFormat format)
	{
		if (format == null)
			throw new NullPointerException();
		ImageViewFormat old = this.format;
		this.format = format;
		return old;
	}
	
	private boolean border;
	
	public final boolean getBorder()
	{
		return this.border;
	}
	
	public final boolean setBorder(boolean border)
	{
		boolean old = this.border;
		this.border = border;
		return old;
	}
	
	private ImageLinkTarget link;
	
	public final ImageLinkTarget getLink()
	{
		return this.link;
	}
	
	public final ImageLinkTarget setLink(ImageLinkTarget link)
	{
		if (link == null)
			throw new NullPointerException();
		ImageLinkTarget old = this.link;
		this.link = link;
		return old;
	}
	
	private WtLinkOptionAltText alt;
	
	public final WtLinkOptionAltText getAlt()
	{
		return this.alt;
	}
	
	public final WtLinkOptionAltText setAlt(WtLinkOptionAltText alt)
	{
		if (alt == null)
			throw new NullPointerException();
		WtLinkOptionAltText old = this.alt;
		this.alt = alt;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 9 + getSuperPropertyCount();
	}
	
	private final int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new WtInnerNode3PropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtImageLink.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "width";
					case 1:
						return "height";
					case 2:
						return "upright";
					case 3:
						return "hAlign";
					case 4:
						return "vAlign";
					case 5:
						return "format";
					case 6:
						return "border";
					case 7:
						return "link";
					case 8:
						return "alt";
						
					default:
						return super.getName(index);
				}
			}
			
			@Override
			protected Object getValue(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return WtImageLink.this.getWidth();
					case 1:
						return WtImageLink.this.getHeight();
					case 2:
						return WtImageLink.this.getUpright();
					case 3:
						return WtImageLink.this.getHAlign();
					case 4:
						return WtImageLink.this.getVAlign();
					case 5:
						return WtImageLink.this.getFormat();
					case 6:
						return WtImageLink.this.getBorder();
					case 7:
						return WtImageLink.this.getLink();
					case 8:
						return WtImageLink.this.getAlt();
						
					default:
						return super.getValue(index);
				}
			}
			
			@Override
			protected Object setValue(int index, Object value)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return WtImageLink.this.setWidth((Integer) value);
					case 1:
						return WtImageLink.this.setHeight((Integer) value);
					case 2:
						return WtImageLink.this.setUpright((Boolean) value);
					case 3:
						return WtImageLink.this.setHAlign((ImageHorizAlign) value);
					case 4:
						return WtImageLink.this.setVAlign((ImageVertAlign) value);
					case 5:
						return WtImageLink.this.setFormat((ImageViewFormat) value);
					case 6:
						return WtImageLink.this.setBorder((Boolean) value);
					case 7:
						return WtImageLink.this.setLink((ImageLinkTarget) value);
					case 8:
						return WtImageLink.this.setAlt((WtLinkOptionAltText) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
	
	// =========================================================================
	// Children
	
	public final void setTarget(WtPageName target)
	{
		set(0, target);
	}
	
	public final WtPageName getTarget()
	{
		return (WtPageName) get(0);
	}
	
	public final void setOptions(WtLinkOptions options)
	{
		set(1, options);
	}
	
	public final WtLinkOptions getOptions()
	{
		return (WtLinkOptions) get(1);
	}
	
	public boolean hasTitle()
	{
		return getTitle() != WtLinkTitle.NO_TITLE;
	}
	
	public final void setTitle(WtLinkTitle title)
	{
		set(2, title);
	}
	
	public final WtLinkTitle getTitle()
	{
		return (WtLinkTitle) get(2);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "target", "options", "title" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
	
	// =========================================================================
	
	public static enum ImageHorizAlign
	{
		LEFT
		{
			@Override
			public String asKeyword()
			{
				return "left";
			}
		},
		RIGHT
		{
			@Override
			public String asKeyword()
			{
				return "right";
			}
		},
		CENTER
		{
			@Override
			public String asKeyword()
			{
				return "center";
			}
		},
		NONE
		{
			@Override
			public String asKeyword()
			{
				return "";
			}
		};
		
		public abstract String asKeyword();
		
		public static ImageHorizAlign which(String s)
		{
			if (s == null)
				throw new NullPointerException();
			
			s = s.trim().toLowerCase();
			for (ImageHorizAlign h : ImageHorizAlign.values())
			{
				if (s.equals(h.asKeyword()))
					return h;
			}
			
			return null;
		}
	}
	
	// =========================================================================
	
	public static enum ImageVertAlign
	{
		BASELINE
		{
			@Override
			public String asKeyword()
			{
				return "baseline";
			}
		},
		SUB
		{
			@Override
			public String asKeyword()
			{
				return "sub";
			}
		},
		SUPER
		{
			@Override
			public String asKeyword()
			{
				return "super";
			}
		},
		TOP
		{
			@Override
			public String asKeyword()
			{
				return "top";
			}
		},
		TEXT_TOP
		{
			@Override
			public String asKeyword()
			{
				return "text-top";
			}
		},
		MIDDLE
		{
			@Override
			public String asKeyword()
			{
				return "middle";
			}
		},
		BOTTOM
		{
			@Override
			public String asKeyword()
			{
				return "bottom";
			}
		},
		TEXT_BOTTOM
		{
			@Override
			public String asKeyword()
			{
				return "text-bottom";
			}
		};
		
		public abstract String asKeyword();
		
		public static ImageVertAlign which(String s)
		{
			if (s == null)
				throw new NullPointerException();
			
			s = s.trim().toLowerCase();
			for (ImageVertAlign v : ImageVertAlign.values())
			{
				if (v.asKeyword().equals(s))
					return v;
			}
			
			return null;
		}
	}
	
	// =========================================================================
	
	public static enum ImageViewFormat
	{
		UNRESTRAINED
		{
			@Override
			public String asKeyword()
			{
				return "";
			}
			
			@Override
			public int priority()
			{
				return 0;
			}
			
			@Override
			public boolean isFramed()
			{
				return false;
			}
		},
		FRAMELESS
		{
			@Override
			public String asKeyword()
			{
				return "frameless";
			}
			
			@Override
			public int priority()
			{
				return 1;
			}
			
			@Override
			public boolean isFramed()
			{
				return false;
			}
		},
		THUMBNAIL
		{
			@Override
			public String asKeyword()
			{
				return "thumb";
			}
			
			@Override
			public int priority()
			{
				return 2;
			}
			
			@Override
			public boolean isFramed()
			{
				return true;
			}
		},
		FRAME
		{
			@Override
			public String asKeyword()
			{
				return "frame";
			}
			
			@Override
			public int priority()
			{
				return 3;
			}
			
			@Override
			public boolean isFramed()
			{
				return true;
			}
		};
		
		private static final Object[] formatMap = new Object[] {
				"frameless", FRAMELESS,
				"thumb", THUMBNAIL,
				"thumbnail", THUMBNAIL,
				"frame", FRAME,
		};
		
		public abstract String asKeyword();
		
		public abstract int priority();
		
		public abstract boolean isFramed();
		
		public static ImageViewFormat which(String s)
		{
			if (s == null)
				throw new NullPointerException();
			
			s = s.trim().toLowerCase();
			for (int i = 0; i < formatMap.length; i += 2)
			{
				String f = (String) formatMap[i];
				if (f.equals(s))
					return (ImageViewFormat) formatMap[i + 1];
			}
			
			return null;
		}
		
		public ImageViewFormat combine(ImageViewFormat other)
		{
			return priority() > other.priority() ? this : other;
		}
	}
	
	// =========================================================================
	
	public static final class ImageLinkTarget
	{
		private final LinkTargetType targetType;
		
		private final WtLinkTarget target;
		
		public ImageLinkTarget(LinkTargetType targetType)
		{
			this.targetType = targetType;
			this.target = WtLinkTarget.NO_LINK;
		}
		
		public ImageLinkTarget(LinkTargetType targetType, WtLinkTarget target)
		{
			this.targetType = targetType;
			this.target = target;
		}
		
		public LinkTargetType getTargetType()
		{
			return targetType;
		}
		
		public WtLinkTarget getTarget()
		{
			return target;
		}
		
		@Override
		public String toString()
		{
			return "ImageLinkTarget[ " + targetType + ": " + target + " ]";
		}
	}
}
