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

import java.util.ListIterator;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.Uninitialized;

public class WtImageLink
		extends
			WtInnerNode2
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
			boolean upright)
	{
		super(target, options);
		setFormat(format);
		setBorder(border);
		setHAlign(hAlign);
		setVAlign(vAlign);
		setWidth(width);
		setHeight(height);
		setUpright(upright);
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

	public final void setWidth(int width)
	{
		this.width = width;
	}

	private int height;

	public final int getHeight()
	{
		return this.height;
	}

	public final void setHeight(int height)
	{
		this.height = height;
	}

	private boolean upright;

	public final boolean getUpright()
	{
		return this.upright;
	}

	public final void setUpright(boolean upright)
	{
		this.upright = upright;
	}

	private ImageHorizAlign hAlign;

	public final ImageHorizAlign getHAlign()
	{
		return this.hAlign;
	}

	public final void setHAlign(ImageHorizAlign hAlign)
	{
		if (hAlign == null)
			throw new NullPointerException();
		this.hAlign = hAlign;
	}

	private ImageVertAlign vAlign;

	public final ImageVertAlign getVAlign()
	{
		return this.vAlign;
	}

	public final void setVAlign(ImageVertAlign vAlign)
	{
		if (vAlign == null)
			throw new NullPointerException();
		this.vAlign = vAlign;
	}

	private ImageViewFormat format;

	public final ImageViewFormat getFormat()
	{
		return this.format;
	}

	public final void setFormat(ImageViewFormat format)
	{
		if (format == null)
			throw new NullPointerException();
		this.format = format;
	}

	private boolean border;

	public final boolean getBorder()
	{
		return this.border;
	}

	public final void setBorder(boolean border)
	{
		this.border = border;
	}

	@Override
	public final int getPropertyCount()
	{
		return 7 + getSuperPropertyCount();
	}

	private final int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}

	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new WtInnerNode2PropertyIterator()
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
					{
						int old = WtImageLink.this.getWidth();
						WtImageLink.this.setWidth((Integer) value);
						return old;
					}
					case 1:
					{
						int old = WtImageLink.this.getHeight();
						WtImageLink.this.setHeight((Integer) value);
						return old;
					}
					case 2:
					{
						boolean old = WtImageLink.this.getUpright();
						WtImageLink.this.setUpright((Boolean) value);
						return old;
					}
					case 3:
					{
						ImageHorizAlign old = WtImageLink.this.getHAlign();
						WtImageLink.this.setHAlign((ImageHorizAlign) value);
						return old;
					}
					case 4:
					{
						ImageVertAlign old = WtImageLink.this.getVAlign();
						WtImageLink.this.setVAlign((ImageVertAlign) value);
						return old;
					}
					case 5:
					{
						ImageViewFormat old = WtImageLink.this.getFormat();
						WtImageLink.this.setFormat((ImageViewFormat) value);
						return old;
					}
					case 6:
					{
						boolean old = WtImageLink.this.getBorder();
						WtImageLink.this.setBorder((Boolean) value);
						return old;
					}

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

	private static final String[] CHILD_NAMES = new String[] { "target", "options" };

	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}

	// =========================================================================
	// Helper

	public final WtLinkOptionLinkTarget getLink()
	{
		WtNode target = getFromOpts(WtNode.NT_LINK_OPTION_LINK_TARGET);
		if (target == null)
			return WtLinkOptionLinkTarget.DEFAULT;
		return (WtLinkOptionLinkTarget) target;
	}

	public final void setLink(WtLinkOptionLinkTarget link)
	{
		if (link == WtLinkOptionLinkTarget.DEFAULT)
			link = null;
		setInOpts(WtNode.NT_LINK_OPTION_LINK_TARGET, link);
	}

	public final boolean hasAlt()
	{
		return getFromOpts(WtNode.NT_LINK_OPTION_ALT_TEXT) != null;
	}

	public final void setAlt(WtLinkOptionAltText alt)
	{
		if (alt == WtLinkOptionAltText.NO_ALT)
			alt = null;
		setInOpts(WtNode.NT_LINK_OPTION_ALT_TEXT, alt);
	}

	public final WtLinkOptionAltText getAlt()
	{
		WtNode alt = getFromOpts(WtNode.NT_LINK_OPTION_ALT_TEXT);
		if (alt == null)
			return WtLinkOptionAltText.NO_ALT;
		return (WtLinkOptionAltText) alt;
	}

	public boolean hasTitle()
	{
		return getFromOpts(WtNode.NT_LINK_TITLE) != null;
	}

	public final void setTitle(WtLinkTitle title)
	{
		if (title == WtLinkTitle.NO_TITLE)
			title = null;
		setInOpts(WtNode.NT_LINK_TITLE, title);
	}

	public final WtLinkTitle getTitle()
	{
		WtNode title = getFromOpts(WtNode.NT_LINK_TITLE);
		if (title == null)
			return WtLinkTitle.NO_TITLE;
		return (WtLinkTitle) title;
	}

	// =========================================================================

	private void setInOpts(int type, WtNode node)
	{
		WtLinkOptions opts = getOptions();

		ListIterator<WtNode> i = opts.listIterator(opts.size());
		while (i.hasPrevious())
		{
			WtNode o = i.previous();
			if (o.getNodeType() == type)
			{
				if (node != null)
				{
					i.set(node);
					return;
				}
				else
				{
					// Remove ALL matching nodes...
					i.remove();
				}
			}
		}

		// We could not replace an existing link instance, append new instead
		if (node != null)
			opts.add(node);
	}

	private WtNode getFromOpts(int type)
	{
		WtLinkOptions opts = getOptions();
		for (int i = opts.size() - 1; i >= 0; --i)
		{
			WtNode o = opts.get(i);
			if (o.getNodeType() == type)
				return o;
		}
		return null;
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
				return "none";
			}
		},
		UNSPECIFIED
		{
			@Override
			public String asKeyword()
			{
				return null;
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
}
