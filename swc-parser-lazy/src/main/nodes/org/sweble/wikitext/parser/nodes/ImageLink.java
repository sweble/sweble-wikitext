package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.parser.ImageHorizAlign;
import org.sweble.wikitext.parser.parser.ImageVertAlign;
import org.sweble.wikitext.parser.parser.ImageViewFormat;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>Image Link</h1>
 */
public class ImageLink
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public ImageLink()
	{
		super(new WtList(), new LinkTitle());
		
	}
	
	public ImageLink(
			String target,
			WtList options,
			LinkTitle title,
			ImageViewFormat format,
			boolean border,
			ImageHorizAlign hAlign,
			ImageVertAlign vAlign,
			int width,
			int height,
			boolean upright,
			String linkPage,
			Url linkUrl,
			LinkOptionAltText alt)
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
	
	private Url linkUrl;
	
	public final Url getLinkUrl()
	{
		return this.linkUrl;
	}
	
	public final Url setLinkUrl(Url linkUrl)
	{
		Url old = this.linkUrl;
		this.linkUrl = linkUrl;
		return old;
	}
	
	private LinkOptionAltText alt;
	
	public final LinkOptionAltText getAlt()
	{
		return this.alt;
	}
	
	public final LinkOptionAltText setAlt(LinkOptionAltText alt)
	{
		LinkOptionAltText old = this.alt;
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
				return ImageLink.this.getPropertyCount();
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
						return ImageLink.this.getTarget();
					case 1:
						return ImageLink.this.getWidth();
					case 2:
						return ImageLink.this.getHeight();
					case 3:
						return ImageLink.this.getUpright();
					case 4:
						return ImageLink.this.getHAlign();
					case 5:
						return ImageLink.this.getVAlign();
					case 6:
						return ImageLink.this.getFormat();
					case 7:
						return ImageLink.this.getBorder();
					case 8:
						return ImageLink.this.getLinkPage();
					case 9:
						return ImageLink.this.getLinkUrl();
					case 10:
						return ImageLink.this.getAlt();
						
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
						return ImageLink.this.setTarget((String) value);
					case 1:
						return ImageLink.this.setWidth((Integer) value);
					case 2:
						return ImageLink.this.setHeight((Integer) value);
					case 3:
						return ImageLink.this.setUpright((Boolean) value);
					case 4:
						return ImageLink.this.setHAlign((ImageHorizAlign) value);
					case 5:
						return ImageLink.this.setVAlign((ImageVertAlign) value);
					case 6:
						return ImageLink.this.setFormat((ImageViewFormat) value);
					case 7:
						return ImageLink.this.setBorder((Boolean) value);
					case 8:
						return ImageLink.this.setLinkPage((String) value);
					case 9:
						return ImageLink.this.setLinkUrl((Url) value);
					case 10:
						return ImageLink.this.setAlt((LinkOptionAltText) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
	
	// =========================================================================
	// Children
	
	public final void setOptions(WtList options)
	{
		set(0, options);
	}
	
	public final WtList getOptions()
	{
		return (WtList) get(0);
	}
	
	public final void setTitle(LinkTitle title)
	{
		set(1, title);
	}
	
	public final LinkTitle getTitle()
	{
		return (LinkTitle) get(1);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "options", "title" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
	
}
