package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.parser.ImageHorizAlign;
import org.sweble.wikitext.parser.parser.ImageVertAlign;
import org.sweble.wikitext.parser.parser.ImageViewFormat;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>Image Link</h1>
 */
public class WtImageLink
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtImageLink()
	{
		super(new WtNodeList(), new WtLinkTitle());
	}
	
	public WtImageLink(
			String target,
			WtNodeList options,
			WtLinkTitle title,
			ImageViewFormat format,
			boolean border,
			ImageHorizAlign hAlign,
			ImageVertAlign vAlign,
			int width,
			int height,
			boolean upright,
			String linkPage,
			WtUrl linkUrl,
			WtLinkOptionAltText alt)
	{
		super(options, title);
		setTarget(target);
		setFormat(format);
		setBorder(border);
		setHAlign(hAlign);
		setVAlign(vAlign);
		setWidth(width);
		setHeight(height);
		setUpright(upright);
		setLinkPage(linkPage);
		setLinkUrl(linkUrl);
		setAlt(alt);
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_IMAGE_LINK;
	}
	
	// =========================================================================
	// Properties
	
	private String target;
	
	public final String getTarget()
	{
		return this.target;
	}
	
	public final String setTarget(String target)
	{
		String old = this.target;
		this.target = target;
		return old;
	}
	
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
	
	private String linkPage;
	
	public final String getLinkPage()
	{
		return this.linkPage;
	}
	
	public final String setLinkPage(String linkPage)
	{
		String old = this.linkPage;
		this.linkPage = linkPage;
		return old;
	}
	
	private WtUrl linkUrl;
	
	public final WtUrl getLinkUrl()
	{
		return this.linkUrl;
	}
	
	public final WtUrl setLinkUrl(WtUrl linkUrl)
	{
		WtUrl old = this.linkUrl;
		this.linkUrl = linkUrl;
		return old;
	}
	
	private WtLinkOptionAltText alt;
	
	public final WtLinkOptionAltText getAlt()
	{
		return this.alt;
	}
	
	public final WtLinkOptionAltText setAlt(WtLinkOptionAltText alt)
	{
		WtLinkOptionAltText old = this.alt;
		this.alt = alt;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 11 + getSuperPropertyCount();
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
						return "target";
					case 1:
						return "width";
					case 2:
						return "height";
					case 3:
						return "upright";
					case 4:
						return "hAlign";
					case 5:
						return "vAlign";
					case 6:
						return "format";
					case 7:
						return "border";
					case 8:
						return "linkPage";
					case 9:
						return "linkUrl";
					case 10:
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
						return WtImageLink.this.getTarget();
					case 1:
						return WtImageLink.this.getWidth();
					case 2:
						return WtImageLink.this.getHeight();
					case 3:
						return WtImageLink.this.getUpright();
					case 4:
						return WtImageLink.this.getHAlign();
					case 5:
						return WtImageLink.this.getVAlign();
					case 6:
						return WtImageLink.this.getFormat();
					case 7:
						return WtImageLink.this.getBorder();
					case 8:
						return WtImageLink.this.getLinkPage();
					case 9:
						return WtImageLink.this.getLinkUrl();
					case 10:
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
						return WtImageLink.this.setTarget((String) value);
					case 1:
						return WtImageLink.this.setWidth((Integer) value);
					case 2:
						return WtImageLink.this.setHeight((Integer) value);
					case 3:
						return WtImageLink.this.setUpright((Boolean) value);
					case 4:
						return WtImageLink.this.setHAlign((ImageHorizAlign) value);
					case 5:
						return WtImageLink.this.setVAlign((ImageVertAlign) value);
					case 6:
						return WtImageLink.this.setFormat((ImageViewFormat) value);
					case 7:
						return WtImageLink.this.setBorder((Boolean) value);
					case 8:
						return WtImageLink.this.setLinkPage((String) value);
					case 9:
						return WtImageLink.this.setLinkUrl((WtUrl) value);
					case 10:
						return WtImageLink.this.setAlt((WtLinkOptionAltText) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
	
	// =========================================================================
	// Children
	
	public final void setOptions(WtNodeList options)
	{
		set(0, options);
	}
	
	public final WtNodeList getOptions()
	{
		return (WtNodeList) get(0);
	}
	
	public final void setTitle(WtLinkTitle title)
	{
		set(1, title);
	}
	
	public final WtLinkTitle getTitle()
	{
		return (WtLinkTitle) get(1);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "options", "title" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
