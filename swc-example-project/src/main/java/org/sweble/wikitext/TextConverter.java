package org.sweble.wikitext;

import java.util.regex.Pattern;

import org.sweble.wikitext.engine.Page;
import org.sweble.wikitext.engine.utils.EntityReferences;
import org.sweble.wikitext.lazy.encval.IllegalCodePoint;
import org.sweble.wikitext.lazy.parser.Bold;
import org.sweble.wikitext.lazy.parser.ExternalLink;
import org.sweble.wikitext.lazy.parser.HorizontalRule;
import org.sweble.wikitext.lazy.parser.ImageLink;
import org.sweble.wikitext.lazy.parser.InternalLink;
import org.sweble.wikitext.lazy.parser.Italics;
import org.sweble.wikitext.lazy.parser.MagicWord;
import org.sweble.wikitext.lazy.parser.Paragraph;
import org.sweble.wikitext.lazy.parser.Section;
import org.sweble.wikitext.lazy.parser.Url;
import org.sweble.wikitext.lazy.parser.Whitespace;
import org.sweble.wikitext.lazy.parser.XmlElement;
import org.sweble.wikitext.lazy.preprocessor.TagExtension;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.preprocessor.TemplateArgument;
import org.sweble.wikitext.lazy.preprocessor.TemplateParameter;
import org.sweble.wikitext.lazy.preprocessor.XmlComment;
import org.sweble.wikitext.lazy.utils.XmlCharRef;
import org.sweble.wikitext.lazy.utils.XmlEntityRef;

import de.fau.cs.osr.ptk.common.Visitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;
import de.fau.cs.osr.utils.StringUtils;

/**
 * A visitor to convert an article AST into a pure text representation. To
 * better understand the visitor pattern as implemented by the Visitor class,
 * please take a look at the following resources:
 * <ul>
 * <li>{@link http://en.wikipedia.org/wiki/Visitor_pattern} (classic pattern)</li>
 * <li>{@link http://www.javaworld.com/javaworld/javatips/jw-javatip98.html}
 * (the version we use here)</li>
 * </ul>
 * 
 * The methods needed to descend into an AST and visit the children of a given
 * node <code>n</code> are
 * <ul>
 * <li><code>dispatch(n)</code> - visit node <code>n</code>,</li>
 * <li><code>iterate(n)</code> - visit the <b>children</b> of node
 * <code>n</code>,</li>
 * <li><code>map(n)</code> - visit the <b>children</b> of node <code>n</code>
 * and gather the return values of the <code>visit()</code> calls in a list,</li>
 * <li><code>mapInPlace(n)</code> - visit the <b>children</b> of node
 * <code>n</code> and replace each child node <code>c</code> with the return
 * value of the call to <code>visit(c)</code>.</li>
 * </ul>
 */
public class TextConverter extends Visitor {

	private static final Pattern ws = Pattern.compile("\\s+");

	private static final int wrapCol = 80;

	private StringBuilder sb;

	private StringBuilder line;

	private int extLinkNum;

	private boolean wrap;

	// =========================================================================

	@Override
	protected boolean before(AstNode node) {
		// This method is called by go() before visitation starts
		sb = new StringBuilder();
		line = new StringBuilder();
		extLinkNum = 1;
		wrap = true;
		return super.before(node);
	}

	@Override
	protected Object after(AstNode node, Object result) {
		// This method is called by go() after visitation has finished
		// The return value will be passed to go() which passes it to the caller
		return sb.toString();
	}

	// =========================================================================

	public void visit(AstNode n) {
		// Fallback for all nodes that are not explicitly handled below
		write("<unsupported: ");
		write(n.getNodeName());
		write(" />");
	}

	public void visit(NodeList n) {
		iterate(n);
	}

	public void visit(Page p) {
		iterate(p.getContent());
	}

	public void visit(Text text) {
		write(text.getContent());
	}

	public void visit(Whitespace w) {
		write(" ");
	}

	public void visit(Bold b) {
		write("**");
		iterate(b.getContent());
		write("**");
	}

	public void visit(Italics i) {
		write("//");
		iterate(i.getContent());
		write("//");
	}

	public void visit(XmlCharRef cr) {
		write(Character.toChars(cr.getCodePoint()));
	}

	public void visit(XmlEntityRef er) {
		String ch = EntityReferences.resolve(er.getName());
		if (ch == null) {
			write('&');
			write(er.getName());
			write(';');
		} else {
			write(ch);
		}
	}

	public void visit(Url url) {
		write(url.getProtocol());
		write(':');
		write(url.getPath());
	}

	public void visit(ExternalLink link) {
		write('[');
		write(extLinkNum++);
		write(']');
	}

	public void visit(InternalLink link) {
		write(link.getPrefix());
		if (link.getTitle().getContent() == null
				|| link.getTitle().getContent().isEmpty()) {
			write(link.getTarget());
		} else {
			iterate(link.getTitle());
		}
		write(link.getPostfix());
	}

	public void visit(ImageLink n) {
		write("<img/>");
	}

	public void visit(Section s) {
		StringBuilder save = sb;
		sb = new StringBuilder();
		iterate(s.getTitle());
		String title = sb.toString().trim();
		sb = save;

		newline(2);
		sb.append(title);
		newline(1);
		sb.append(StringUtils.strrep('-', title.length()));
		newline(2);
		iterate(s.getBody());
	}

	public void visit(Paragraph p) {
		iterate(p.getContent());
		newline(2);
	}

	public void visit(HorizontalRule hr) {
		newline(1);
		write(StringUtils.strrep('-', wrapCol));
		newline(2);
	}

	public void visit(XmlElement e) {
		if (e.getName().equalsIgnoreCase("br")) {
			newline(1);
		} else {
			iterate(e.getBody());
		}
	}

	// =========================================================================
	// Stuff we want to hide

	public void visit(IllegalCodePoint n) {
	}

	public void visit(XmlComment n) {
	}

	public void visit(Template n) {
	}

	public void visit(TemplateArgument n) {
	}

	public void visit(TemplateParameter n) {
	}

	public void visit(TagExtension n) {
	}

	public void visit(MagicWord n) {
	}

	// =========================================================================

	/*
	 * private String normalizeSpaces(String content) { return
	 * ws.matcher(content).replaceAll(" "); }
	 */

	private void newline(int num) {
		if (sb.length() > 0) {
			for (int i = 0; i < num; ++i)
				write('\n');
		}
	}

	/*
	 * private boolean hadSpace = false;
	 */

	private void write(String s) {
		sb.append(s);
	}

	private void write(char ch) {
		sb.append(ch);
	}

	private void write(char[] cs) {
		sb.append(cs);
	}

	private void write(int num) {
		sb.append(num);
	}
}
