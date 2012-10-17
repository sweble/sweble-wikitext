package org.sweble.wikitext.engine.output;

import java.io.Serializable;

public class MediaInfo
		implements
			Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String title;
	
	private String descUrl;
	
	private String imgUrl;
	
	private int imgWidth = -1;
	
	private int imgHeight = -1;
	
	private String thumbUrl;
	
	private int thumbWidth = -1;
	
	private int thumbHeight = -1;
	
	// =========================================================================
	
	public MediaInfo()
	{
	}
	
	public MediaInfo(
			String title,
			String descUrl,
			String imgUrl,
			int imgWidth,
			int imgHeight,
			String thumbUrl,
			int thumbWidth,
			int thumbHeight)
	{
		this.title = title;
		this.descUrl = descUrl;
		this.imgUrl = imgUrl;
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
		this.thumbUrl = thumbUrl;
		this.thumbWidth = thumbWidth;
		this.thumbHeight = thumbHeight;
	}
	
	// =========================================================================
	
	public String getTitle()
	{
		return title;
	}
	
	public String getDescUrl()
	{
		return descUrl;
	}
	
	public String getImgUrl()
	{
		return imgUrl;
	}
	
	public int getImgWidth()
	{
		return imgWidth;
	}
	
	public int getImgHeight()
	{
		return imgHeight;
	}
	
	public String getThumbUrl()
	{
		return thumbUrl;
	}
	
	public int getThumbWidth()
	{
		return thumbWidth;
	}
	
	public int getThumbHeight()
	{
		return thumbHeight;
	}
}
