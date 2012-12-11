package org.sweble.wikitext.engine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

import org.joda.time.DateTime;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.CompleteEngineVisitor;
import org.sweble.wikitext.engine.nodes.EngCompiledPage;
import org.sweble.wikitext.engine.nodes.EngNowiki;
import org.sweble.wikitext.engine.nodes.EngPage;
import org.sweble.wikitext.engine.nodes.EngSoftErrorNode;
import org.sweble.wikitext.parser.nodes.WtBody;
import org.sweble.wikitext.parser.nodes.WtBold;
import org.sweble.wikitext.parser.nodes.WtContentNode;
import org.sweble.wikitext.parser.nodes.WtDefinitionList;
import org.sweble.wikitext.parser.nodes.WtDefinitionListDef;
import org.sweble.wikitext.parser.nodes.WtDefinitionListTerm;
import org.sweble.wikitext.parser.nodes.WtExternalLink;
import org.sweble.wikitext.parser.nodes.WtHeading;
import org.sweble.wikitext.parser.nodes.WtHorizontalRule;
import org.sweble.wikitext.parser.nodes.WtIgnored;
import org.sweble.wikitext.parser.nodes.WtIllegalCodePoint;
import org.sweble.wikitext.parser.nodes.WtImEndTag;
import org.sweble.wikitext.parser.nodes.WtImStartTag;
import org.sweble.wikitext.parser.nodes.WtImageLink;
import org.sweble.wikitext.parser.nodes.WtInternalLink;
import org.sweble.wikitext.parser.nodes.WtItalics;
import org.sweble.wikitext.parser.nodes.WtLinkOptionAltText;
import org.sweble.wikitext.parser.nodes.WtLinkOptionGarbage;
import org.sweble.wikitext.parser.nodes.WtLinkOptionKeyword;
import org.sweble.wikitext.parser.nodes.WtLinkOptionLinkTarget;
import org.sweble.wikitext.parser.nodes.WtLinkOptionResize;
import org.sweble.wikitext.parser.nodes.WtLinkOptions;
import org.sweble.wikitext.parser.nodes.WtLinkTitle;
import org.sweble.wikitext.parser.nodes.WtListItem;
import org.sweble.wikitext.parser.nodes.WtName;
import org.sweble.wikitext.parser.nodes.WtNewline;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtOnlyInclude;
import org.sweble.wikitext.parser.nodes.WtOrderedList;
import org.sweble.wikitext.parser.nodes.WtPageName;
import org.sweble.wikitext.parser.nodes.WtPageSwitch;
import org.sweble.wikitext.parser.nodes.WtParagraph;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtPreproWikitextPage;
import org.sweble.wikitext.parser.nodes.WtRedirect;
import org.sweble.wikitext.parser.nodes.WtSection;
import org.sweble.wikitext.parser.nodes.WtSemiPre;
import org.sweble.wikitext.parser.nodes.WtSemiPreLine;
import org.sweble.wikitext.parser.nodes.WtSignature;
import org.sweble.wikitext.parser.nodes.WtTable;
import org.sweble.wikitext.parser.nodes.WtTableCaption;
import org.sweble.wikitext.parser.nodes.WtTableCell;
import org.sweble.wikitext.parser.nodes.WtTableHeader;
import org.sweble.wikitext.parser.nodes.WtTableImplicitTableBody;
import org.sweble.wikitext.parser.nodes.WtTableRow;
import org.sweble.wikitext.parser.nodes.WtTagExtension;
import org.sweble.wikitext.parser.nodes.WtTagExtensionBody;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateArgument;
import org.sweble.wikitext.parser.nodes.WtTemplateArguments;
import org.sweble.wikitext.parser.nodes.WtTemplateParameter;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtTicks;
import org.sweble.wikitext.parser.nodes.WtUnorderedList;
import org.sweble.wikitext.parser.nodes.WtUrl;
import org.sweble.wikitext.parser.nodes.WtValue;
import org.sweble.wikitext.parser.nodes.WtWhitespace;
import org.sweble.wikitext.parser.nodes.WtXmlAttribute;
import org.sweble.wikitext.parser.nodes.WtXmlAttributeGarbage;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes;
import org.sweble.wikitext.parser.nodes.WtXmlCharRef;
import org.sweble.wikitext.parser.nodes.WtXmlComment;
import org.sweble.wikitext.parser.nodes.WtXmlElement;
import org.sweble.wikitext.parser.nodes.WtXmlEmptyTag;
import org.sweble.wikitext.parser.nodes.WtXmlEndTag;
import org.sweble.wikitext.parser.nodes.WtXmlEntityRef;
import org.sweble.wikitext.parser.nodes.WtXmlStartTag;
import org.sweble.wikitext.parser.utils.StringConversionException;
import org.sweble.wikitext.parser.utils.WtRtDataPrettyPrinter;
import org.sweble.wom.WomArg;
import org.sweble.wom.WomAttribute;
import org.sweble.wom.WomBody;
import org.sweble.wom.WomHeading;
import org.sweble.wom.WomInlineElement;
import org.sweble.wom.WomName;
import org.sweble.wom.WomNode;
import org.sweble.wom.WomPage;
import org.sweble.wom.WomParagraph;
import org.sweble.wom.WomSignatureFormat;
import org.sweble.wom.WomTagExtBody;
import org.sweble.wom.WomText;
import org.sweble.wom.WomTransclusion;
import org.sweble.wom.WomValue;
import org.sweble.wom.impl.types.AbbrImpl;
import org.sweble.wom.impl.types.ArgImpl;
import org.sweble.wom.impl.types.AttributeImpl;
import org.sweble.wom.impl.types.BigImpl;
import org.sweble.wom.impl.types.BlockquoteImpl;
import org.sweble.wom.impl.types.BodyImpl;
import org.sweble.wom.impl.types.BoldImpl;
import org.sweble.wom.impl.types.BreakImpl;
import org.sweble.wom.impl.types.CenterImpl;
import org.sweble.wom.impl.types.CiteImpl;
import org.sweble.wom.impl.types.CodeImpl;
import org.sweble.wom.impl.types.CommentImpl;
import org.sweble.wom.impl.types.DefaultImpl;
import org.sweble.wom.impl.types.DefinitionListDefImpl;
import org.sweble.wom.impl.types.DefinitionListImpl;
import org.sweble.wom.impl.types.DefinitionListTermImpl;
import org.sweble.wom.impl.types.DelImpl;
import org.sweble.wom.impl.types.DfnImpl;
import org.sweble.wom.impl.types.DivImpl;
import org.sweble.wom.impl.types.ElementBodyImpl;
import org.sweble.wom.impl.types.ElementImpl;
import org.sweble.wom.impl.types.EmphasizeImpl;
import org.sweble.wom.impl.types.FontImpl;
import org.sweble.wom.impl.types.HeadingImpl;
import org.sweble.wom.impl.types.HorizontalRuleImpl;
import org.sweble.wom.impl.types.InsImpl;
import org.sweble.wom.impl.types.ItalicsImpl;
import org.sweble.wom.impl.types.KbdImpl;
import org.sweble.wom.impl.types.ListItemImpl;
import org.sweble.wom.impl.types.NameImpl;
import org.sweble.wom.impl.types.NowikiImpl;
import org.sweble.wom.impl.types.OrderedListImpl;
import org.sweble.wom.impl.types.PageImpl;
import org.sweble.wom.impl.types.PageSwitchImpl;
import org.sweble.wom.impl.types.ParagraphImpl;
import org.sweble.wom.impl.types.ParamImpl;
import org.sweble.wom.impl.types.PreImpl;
import org.sweble.wom.impl.types.RedirectImpl;
import org.sweble.wom.impl.types.SampImpl;
import org.sweble.wom.impl.types.SectionImpl;
import org.sweble.wom.impl.types.SemiPreImpl;
import org.sweble.wom.impl.types.SignatureImpl;
import org.sweble.wom.impl.types.SmallImpl;
import org.sweble.wom.impl.types.SpanImpl;
import org.sweble.wom.impl.types.StrikeImpl;
import org.sweble.wom.impl.types.StrongImpl;
import org.sweble.wom.impl.types.SubImpl;
import org.sweble.wom.impl.types.SupImpl;
import org.sweble.wom.impl.types.TagExtBodyImpl;
import org.sweble.wom.impl.types.TagExtensionImpl;
import org.sweble.wom.impl.types.TeletypeImpl;
import org.sweble.wom.impl.types.TextImpl;
import org.sweble.wom.impl.types.TransclusionImpl;
import org.sweble.wom.impl.types.UnderlineImpl;
import org.sweble.wom.impl.types.UnorderedListImpl;
import org.sweble.wom.impl.types.ValueImpl;
import org.sweble.wom.impl.types.VarImpl;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.utils.StringUtils;
import de.fau.cs.osr.utils.WrappedException;
import de.fau.cs.osr.utils.XmlGrammar;

public class AstToWomVisitor
		extends
			AstVisitor<WtNode>
		implements
			CompleteEngineVisitor<WomNode>
{
	private final WikiConfig config;
	
	private final PageTitle pageTitle;
	
	private final LinkedList<WomNode> stack = new LinkedList<WomNode>();
	
	private final String author;
	
	private final DateTime timestamp;
	
	private final Map<String, XhtmlElement> xhtmlElems;
	
	// =========================================================================
	
	public AstToWomVisitor(
			WikiConfig config,
			PageTitle pageTitle,
			String author,
			DateTime timestamp)
	{
		this.config = config;
		
		this.pageTitle = pageTitle;
		
		this.author = author;
		
		this.timestamp = timestamp;
		
		this.xhtmlElems = new HashMap<String, XhtmlElement>();
		for (XhtmlElement e : XhtmlElement.values())
			xhtmlElems.put(e.name().toLowerCase(), e);
	}
	
	// == [ Encoding Validator ] ===============================================
	
	@Override
	public WomNode visit(WtIllegalCodePoint n)
	{
		// The & will be escaped and therefore the whole thing will be rendered 
		// as plain text, indicating that something unrepresentable was 
		// processed here.
		return appendText("&#" + n.getCodePoint() + ";");
	}
	
	// == [ Tag extension ] ====================================================
	
	@Override
	public WomNode visit(WtTagExtension n)
	{
		TagExtensionImpl e = new TagExtensionImpl(n.getName());
		
		for (WtNode attr : n.getXmlAttributes())
		{
			WomAttribute womAttr = (WomAttribute) dispatch(attr);
			if (womAttr != null)
				e.setTagAttribute(womAttr.getName(), womAttr.getValue());
		}
		
		if (n.hasBody())
			e.setBody((WomTagExtBody) dispatch(n.getBody()));
		
		return e;
	}
	
	@Override
	public WomNode visit(WtTagExtensionBody n)
	{
		return new TagExtBodyImpl(n.getContent());
	}
	
	// == [ Template ] =========================================================
	
	@Override
	public WomNode visit(WtTemplate n)
	{
		TransclusionImpl t = new TransclusionImpl();
		stack.push(t);
		t.setName((WomName) dispatch(n.getName()));
		dispatch(n.getArgs());
		stack.pop();
		return t;
	}
	
	@Override
	public WomNode visit(WtTemplateArgument n)
	{
		WomValue value = (WomValue) dispatch(n.getValue());
		return n.hasName() ?
				new ArgImpl((WomName) dispatch(n.getName()), value) :
				new ArgImpl(value);
	}
	
	@Override
	public WomNode visit(WtTemplateParameter n)
	{
		WomName name = (WomName) dispatch(n.getName());
		return n.hasDefaultValue() ?
				new ParamImpl(name, processChildren(n.getDefaultValue(), new DefaultImpl())) :
				new ParamImpl(name);
	}
	
	@Override
	public WomNode visit(WtTemplateArguments n)
	{
		WomTransclusion parent = (WomTransclusion) stack.peek();
		for (WtNode arg : n)
			parent.addArgument((WomArg) dispatch(arg));
		return null;
	}
	
	// == [ XML stuff ] ========================================================
	
	@Override
	public WomNode visit(WtXmlCharRef n)
	{
		int codePoint = n.getCodePoint();
		if (!XmlGrammar.isChar(codePoint))
		{
			// The & will be escaped and therefore the whole thing will be 
			// rendered as plain text, indicating that something unrepresentable 
			// was processed here.
			return appendText("&#" + codePoint + ";");
		}
		else
		{
			return appendText(new String(Character.toChars(codePoint)));
		}
	}
	
	@Override
	public WomNode visit(WtXmlEntityRef n)
	{
		if (n.getResolved() == null)
		{
			// The & will be escaped and therefore the whole thing will be 
			// rendered as plain text, indicating that something unrepresentable 
			// was processed here.
			return appendText("&" + n.getName() + ";");
		}
		else
		{
			return appendText(n.getResolved());
		}
	}
	
	@Override
	public WomNode visit(WtXmlEmptyTag n)
	{
		return convertToText(n);
	}
	
	@Override
	public WomNode visit(WtXmlStartTag n)
	{
		return convertToText(n);
	}
	
	@Override
	public WomNode visit(WtImStartTag n)
	{
		// Drop that ****
		return null;
	}
	
	@Override
	public WomNode visit(WtXmlEndTag n)
	{
		return convertToText(n);
	}
	
	@Override
	public WomNode visit(WtImEndTag n)
	{
		// Drop that ****
		return null;
	}
	
	@Override
	public WomNode visit(WtXmlAttributes n)
	{
		WomNode parent = stack.peek();
		for (WtNode a : n)
		{
			WomAttribute womAttr = (WomAttribute) dispatch(a);
			if (womAttr != null)
				parent.setAttributeNode(womAttr);
		}
		return null;
	}
	
	@Override
	public WomNode visit(WtXmlAttribute n)
	{
		return new AttributeImpl(n.getName(), stringify(n.getValue()));
	}
	
	@Override
	public WomNode visit(WtXmlAttributeGarbage n)
	{
		// Ignore garbage
		return null;
	}
	
	@Override
	public WomNode visit(WtXmlElement n)
	{
		String eName = n.getName().toLowerCase();
		XhtmlElement eType = xhtmlElems.get(eName);
		if (eType != null)
		{
			switch (eType)
			{
			// -- Easy stuff --
			
				case ABBR:
					return completeXmlElement(n, new AbbrImpl());
				case B:
					return completeXmlElement(n, new BoldImpl());
				case BIG:
					return completeXmlElement(n, new BigImpl());
				case BLOCKQUOTE:
					return completeXmlElement(n, new BlockquoteImpl());
				case BR:
					return completeXmlElement(n, new BreakImpl());
				case CENTER:
					return completeXmlElement(n, new CenterImpl());
				case CITE:
					return completeXmlElement(n, new CiteImpl());
				case CODE:
					return completeXmlElement(n, new CodeImpl());
				case DEL:
					return completeXmlElement(n, new DelImpl());
				case DFN:
					return completeXmlElement(n, new DfnImpl());
				case DIV:
					return completeXmlElement(n, new DivImpl());
				case EM:
					return completeXmlElement(n, new EmphasizeImpl());
				case FONT:
					return completeXmlElement(n, new FontImpl());
				case HR:
					return completeXmlElement(n, new HorizontalRuleImpl());
				case I:
					return completeXmlElement(n, new ItalicsImpl());
				case INS:
					return completeXmlElement(n, new InsImpl());
				case KBD:
					return completeXmlElement(n, new KbdImpl());
				case P:
					return completeXmlElement(n, new ParagraphImpl());
				case S:
					return completeXmlElement(n, new StrikeImpl());
				case SAMP:
					return completeXmlElement(n, new SampImpl());
				case SMALL:
					return completeXmlElement(n, new SmallImpl());
				case SPAN:
					return completeXmlElement(n, new SpanImpl());
				case STRIKE:
					return completeXmlElement(n, new StrikeImpl());
				case STRONG:
					return completeXmlElement(n, new StrongImpl());
				case SUB:
					return completeXmlElement(n, new SubImpl());
				case SUP:
					return completeXmlElement(n, new SupImpl());
				case TT:
					return completeXmlElement(n, new TeletypeImpl());
				case U:
					return completeXmlElement(n, new UnderlineImpl());
				case VAR:
					return completeXmlElement(n, new VarImpl());
					
					// -- Lists --
					
				case DD:
					return completeXmlElement(n, new DefinitionListDefImpl());
				case DL:
					return completeXmlElement(n, new DefinitionListImpl());
				case DT:
					return completeXmlElement(n, new DefinitionListTermImpl());
				case LI:
					return completeXmlElement(n, new ListItemImpl());
				case OL:
					return completeXmlElement(n, new OrderedListImpl());
				case UL:
					return completeXmlElement(n, new UnorderedListImpl());
					
					// -- Misc --
					
				case PRE:
					return new PreImpl(stringify(n.getBody()));

					// -- Tables --
					
				case CAPTION:
				case TABLE:
				case TBODY:
				case TD:
				case TH:
				case TR:
					
					// -- Just wrong stuff --
					
				default:
					return nyi();
			}
		}
		else
		{
			ElementImpl e = new ElementImpl(eName);
			
			for (WtNode attr : n.getXmlAttributes())
			{
				WomAttribute womAttr = (WomAttribute) dispatch(attr);
				if (womAttr != null)
					e.setElemAttribute(womAttr.getName(), womAttr.getValue());
			}
			
			if (n.hasBody())
			{
				stack.push(e);
				e.setBody(new ElementBodyImpl());
				processChildren(n.getBody(), e.getBody());
				stack.pop();
			}
			
			return e;
		}
	}
	
	private WomNode completeXmlElement(WtXmlElement n, WomNode e)
	{
		stack.push(e);
		dispatch(n.getXmlAttributes());
		if (n.hasBody())
			onlyProcessChildren(n.getBody(), e);
		stack.pop();
		return e;
	}
	
	// == [ Links ] ============================================================
	
	@Override
	public WomNode visit(WtRedirect n)
	{
		WomPage page = (WomPage) stack.getLast();
		page.setRedirect(new RedirectImpl(n.getTarget().getContent()));
		return null;
	}
	
	@Override
	public WomNode visit(WtUrl n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtExternalLink n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtInternalLink n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtImageLink n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtPageName n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtLinkTitle n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtLinkOptions n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtLinkOptionLinkTarget n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtLinkOptionKeyword n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtLinkOptionResize n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtLinkOptionAltText n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtLinkOptionGarbage n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	// == [ Section ] ==========================================================
	
	@Override
	public WomNode visit(WtSection n)
	{
		SectionImpl s = new SectionImpl(n.getLevel());
		stack.push(s);
		s.setHeading((WomHeading) dispatch(n.getHeading()));
		s.setBody((WomBody) dispatch(n.getBody()));
		stack.pop();
		return s;
	}
	
	@Override
	public WomNode visit(WtHeading n)
	{
		return processChildren(n, new HeadingImpl());
	}
	
	// == [ Lists ] ============================================================
	
	@Override
	public WomNode visit(WtOrderedList n)
	{
		return processChildren(n, new OrderedListImpl());
	}
	
	@Override
	public WomNode visit(WtUnorderedList n)
	{
		return processChildren(n, new UnorderedListImpl());
	}
	
	@Override
	public WomNode visit(WtListItem n)
	{
		return processChildren(n, new ListItemImpl());
	}
	
	@Override
	public WomNode visit(WtDefinitionList n)
	{
		return processChildren(n, new DefinitionListImpl());
	}
	
	@Override
	public WomNode visit(WtDefinitionListDef n)
	{
		return processChildren(n, new DefinitionListDefImpl());
	}
	
	@Override
	public WomNode visit(WtDefinitionListTerm n)
	{
		return processChildren(n, new DefinitionListTermImpl());
	}
	
	// == [ Table ] ============================================================
	
	@Override
	public WomNode visit(WtTable n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTableCaption n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTableImplicitTableBody n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTableRow n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTableCell n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	@Override
	public WomNode visit(WtTableHeader n)
	{
		// TODO Auto-generated method stub
		return nyi();
	}
	
	// == [ Simple HTML equivalents ] ==========================================
	
	@Override
	public WomNode visit(WtHorizontalRule n)
	{
		return processChildren(n, new HorizontalRuleImpl());
	}
	
	@Override
	public WomNode visit(WtItalics n)
	{
		return processChildren(n, new ItalicsImpl());
	}
	
	@Override
	public WomNode visit(WtBold n)
	{
		return processChildren(n, new BoldImpl());
	}
	
	// == [ Signature ] ========================================================
	
	@Override
	public WomNode visit(WtSignature n)
	{
		WomSignatureFormat format;
		switch (n.getTildeCount())
		{
			case 3:
				format = WomSignatureFormat.USER;
				break;
			case 4:
				format = WomSignatureFormat.USER_TIMESTAMP;
				break;
			case 5:
				format = WomSignatureFormat.TIMESTAMP;
				break;
			default:
				throw new IllegalArgumentException("Invalid AST signature node");
		}
		return new SignatureImpl(format, author, timestamp);
	}
	
	// == [ Paragraph ] ========================================================
	
	@Override
	public WomNode visit(WtParagraph n)
	{
		ParagraphImpl p = new ParagraphImpl();
		p.setTopGap(countNewlines(n.listIterator(), false));
		p.setBottomGap(countNewlines(n.listIterator(n.size()), true));
		processChildren(n, p);
		if (!p.hasChildNodes())
			return null;
		return p;
	}
	
	private int countNewlines(ListIterator<?> i, boolean reverse)
	{
		int count = 0;
		outer: while (reverse ? i.hasPrevious() : i.hasNext())
		{
			WtNode n = (WtNode) (reverse ? i.previous() : i.next());
			switch (n.getNodeType())
			{
				case WtNode.NT_NEWLINE:
					++count;
					break;
				case WtNode.NT_TEXT:
				{
					String text = ((WtText) n).getContent();
					int len = text.length();
					for (int j = 0; j < len; ++j)
					{
						int ch = text.charAt(reverse ? len - 1 - j : j);
						if (ch == '\n' || ch == '\r')
						{
							if (j + 1 < len)
							{
								int ch2 = text.charAt(reverse ? len - 1 - j : j);
								if (ch2 == '\n' || ch2 == '\r')
									++j;
							}
							++count;
						}
						else if (!Character.isWhitespace(ch))
						{
							break outer;
						}
					}
					break;
				}
				default:
					break outer;
			}
		}
		return count;
	}
	
	// == [ Semi Pre ] =========================================================
	
	@Override
	public WomNode visit(WtSemiPre n)
	{
		return processChildren(n, new SemiPreImpl());
	}
	
	@Override
	public WomNode visit(WtSemiPreLine n)
	{
		return convertContainerNode(n);
	}
	
	// == [ Containers ] =======================================================
	
	@Override
	public WomNode visit(WtNodeList n)
	{
		return convertContainerNode(n);
	}
	
	@Override
	public WomNode visit(WtBody n)
	{
		return processChildren(n, new BodyImpl());
	}
	
	@Override
	public WomNode visit(WtName n)
	{
		return processChildren(n, new NameImpl());
	}
	
	@Override
	public WomNode visit(WtValue n)
	{
		return processChildren(n, new ValueImpl());
	}
	
	@Override
	public WomNode visit(WtWhitespace n)
	{
		return convertContainerNode(n);
	}
	
	@Override
	public WomNode visit(WtOnlyInclude n)
	{
		return convertContainerNode(n);
	}
	
	private WomNode convertContainerNode(WtNode n)
	{
		WomNode parent = stack.peek();
		onlyProcessChildren(n, parent);
		return null;
	}
	
	// == [ Text ] =============================================================
	
	@Override
	public WomNode visit(WtText n)
	{
		return appendText(n.getContent());
	}
	
	@Override
	public WomNode visit(WtNewline n)
	{
		return appendText(n.getContent());
	}
	
	// == [ Comment ] ==========================================================
	
	@Override
	public WomNode visit(WtXmlComment n)
	{
		return new CommentImpl(n.getContent());
	}
	
	// == [ Nowiki ] ===========================================================
	
	@Override
	public WomNode visit(EngNowiki n)
	{
		return new NowikiImpl(n.getContent());
	}
	
	// == [ Error Node ] =======================================================
	
	@Override
	public WomNode visit(EngSoftErrorNode n)
	{
		// TODO: Other options?
		return visit((WtXmlElement) n);
	}
	
	// == [ Page Switch ] ======================================================
	
	@Override
	public WomNode visit(WtPageSwitch n)
	{
		return new PageSwitchImpl(n.getName());
	}
	
	// == [ Page roots ] =======================================================
	
	@Override
	public WomNode visit(EngCompiledPage n)
	{
		return (WomNode) dispatch(n.getPage());
	}
	
	@Override
	public WomNode visit(EngPage n)
	{
		return createRootNode(n);
	}
	
	@Override
	public WomNode visit(WtParsedWikitextPage n)
	{
		return createRootNode(n);
	}
	
	@Override
	public WomNode visit(WtPreproWikitextPage n)
	{
		return createRootNode(n);
	}
	
	private WomNode createRootNode(WtContentNode body)
	{
		PageImpl page = new PageImpl(
				pageTitle.getNamespaceAlias(),
				null,
				pageTitle.getDenormalizedFullTitle());
		stack.push(page);
		processChildren(body, page.getBody());
		stack.pop();
		return page;
	}
	
	// == [ Misc ] =============================================================
	
	@Override
	public WomNode visit(WtTicks n)
	{
		return appendText(StringUtils.strrep('\'', n.getTickCount()));
	}
	
	@Override
	public WomNode visit(WtIgnored n)
	{
		// Ignore ignored stuff...
		return null;
	}
	
	// =========================================================================
	
	private <T extends WomNode> T processChildren(WtNode astNode, T womNode)
	{
		stack.push(womNode);
		onlyProcessChildren(astNode, womNode);
		stack.pop();
		return womNode;
	}
	
	private WomNode onlyProcessChildren(WtNode astNode, WomNode womNode)
	{
		for (WtNode c : astNode)
		{
			WomNode result = (WomNode) dispatch(c);
			if (result != null)
				womNode.appendChild(result);
		}
		return womNode;
	}
	
	private WomNode convertToText(WtNode n)
	{
		return appendText(WtRtDataPrettyPrinter.print(n));
	}
	
	private WomNode appendText(String text)
	{
		WomNode parent = stack.peek();
		if (parent instanceof WomParagraph || parent instanceof WomInlineElement || !text.trim().isEmpty())
		{
			WomNode last = parent.getLastChild();
			if (last instanceof WomText)
			{
				last.appendText(text);
				return null;
			}
			else
			{
				return new TextImpl(text);
			}
		}
		else
		{
			return null;
		}
	}
	
	private String stringify(WtContentNode value)
	{
		try
		{
			return config.createAstTextUtils().astToText(value);
		}
		catch (StringConversionException e)
		{
			throw new WrappedException(e);
		}
	}
	
	private WomNode nyi()
	{
		throw new UnsupportedOperationException("not yet implemented");
		//return null;
	}
}
