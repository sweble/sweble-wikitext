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

import java.util.ArrayList;

import org.sweble.wikitext.parser.AstNodeTypes;
import org.sweble.wikitext.parser.LinkTargetType;
import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.nodes.ImageLink;
import org.sweble.wikitext.parser.nodes.InternalLink;
import org.sweble.wikitext.parser.nodes.WtLinkOptionAltText;
import org.sweble.wikitext.parser.nodes.WtLinkTarget;
import org.sweble.wikitext.parser.nodes.WtLinkTitle;
import org.sweble.wikitext.parser.nodes.Url;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;

import de.fau.cs.osr.ptk.common.Warning;

public class LinkBuilder
{
	private String target;
	
	private String linkPage;
	
	private Url linkUrl;
	
	private WtLinkOptionAltText alt;
	
	private WtLinkTitle title;
	
	// -- format
	
	private int width;
	
	private int height;
	
	private boolean upright;
	
	private ImageHorizAlign hAlign;
	
	private ImageVertAlign vAlign;
	
	private ImageViewFormat format;
	
	private boolean border;
	
	// -- warnings picked up along the way
	
	private ArrayList<Warning> warnings;
	
	// -- internal state
	
	private LinkTargetType targetType;
	
	// =========================================================================
	
	public LinkBuilder(ParserConfig parserConfig, String target)
	{
		this.target = target;
		this.targetType = parserConfig.classifyTarget(target);
		
		this.title = null;
		this.width = -1;
		this.height = -1;
		this.upright = false;
		this.hAlign = null;
		this.vAlign = null;
		this.format = null;
		this.border = false;
		this.linkUrl = null;
		this.linkPage = null;
		this.alt = null;
	}
	
	// =========================================================================
	
	public boolean isImageTarget()
	{
		return targetType == LinkTargetType.IMAGE;
	}
	
	public boolean isValidTarget()
	{
		return targetType != LinkTargetType.INVALID;
	}
	
	// =========================================================================
	
	public boolean addKeyword(String keyword)
	{
		ImageViewFormat f;
		if ((f = ImageViewFormat.which(keyword)) != null)
		{
			format = (format == null) ? f : format.combine(f);
			return true;
		}
		
		ImageHorizAlign h;
		if ((h = ImageHorizAlign.which(keyword)) != null)
		{
			hAlign = h;
			return true;
		}
		
		ImageVertAlign v;
		if ((v = ImageVertAlign.which(keyword)) != null)
		{
			vAlign = v;
			return true;
		}
		
		if (keyword.equals("border"))
		{
			border = true;
			return true;
		}
		
		if (keyword.equals("upright"))
		{
			upright = true;
			return true;
		}
		
		return false;
	}
	
	public void setHeight(int height)
	{
		if (height >= 0)
			this.height = height;
	}
	
	public void setWidth(int width)
	{
		if (width >= 0)
			this.width = width;
	}
	
	// =========================================================================
	
	public void setLink(WtNode target)
	{
		if (target != null)
		{
			if (target.isNodeType(AstNodeTypes.NT_URL))
			{
				// second occurrence wins, url beats page
				this.linkPage = null;
				this.linkUrl = (Url) target;
			}
			else
			{
				// second occurrence wins, url beats page
				if (this.linkUrl != null)
					return;
				this.linkPage = ((WtLinkTarget) target).getContent();
			}
		}
		else
		{
			this.linkPage = "";
		}
	}
	
	// =========================================================================
	
	public void setAlt(WtLinkOptionAltText alt)
	{
		this.alt = alt;
	}
	
	public void setTitle(WtLinkTitle title)
	{
		this.title = title;
	}
	
	// =========================================================================
	
	public WtNode build(WtNodeList options, String postfix)
	{
		if (this.title == null)
			this.title = new WtLinkTitle();
		
		if (this.targetType == LinkTargetType.IMAGE)
		{
			if (hAlign == null)
				hAlign = ImageHorizAlign.NONE;
			
			if (vAlign == null)
				vAlign = ImageVertAlign.MIDDLE;
			
			if (format == null)
				format = ImageViewFormat.UNRESTRAINED;
			
			if (alt == null)
				alt = new WtLinkOptionAltText();
			
			ImageLink result = new ImageLink(
					target,
					options,
					title,
					format,
					border,
					hAlign,
					vAlign,
					width,
					height,
					upright,
					linkPage,
					linkUrl,
					alt);
			
			finish(result);
			return result;
		}
		else
		{
			InternalLink result = new InternalLink(
					"",
					target,
					title,
					postfix);
			
			finish(result);
			return result;
		}
	}
	
	public void addWarning(Warning warning)
	{
		if (warnings == null)
			warnings = new ArrayList<Warning>();
		warnings.add(warning);
	}
	
	public void finish(WtNode n)
	{
		if (warnings != null && !warnings.isEmpty())
			n.setAttribute("warnings", warnings);
	}
}
