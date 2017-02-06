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

package org.sweble.wikitext.parser.postprocessor;

import static org.sweble.wikitext.parser.postprocessor.IntermediateTags.BOLD;
import static org.sweble.wikitext.parser.postprocessor.IntermediateTags.ITALICS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactory;
import org.sweble.wikitext.parser.nodes.WtContentNode;
import org.sweble.wikitext.parser.nodes.WtDefinitionListDef;
import org.sweble.wikitext.parser.nodes.WtDefinitionListTerm;
import org.sweble.wikitext.parser.nodes.WtLeafNode;
import org.sweble.wikitext.parser.nodes.WtListItem;
import org.sweble.wikitext.parser.nodes.WtNewline;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtSemiPreLine;
import org.sweble.wikitext.parser.nodes.WtStringNode;
import org.sweble.wikitext.parser.nodes.WtTableCell;
import org.sweble.wikitext.parser.nodes.WtTableHeader;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtTicks;
import org.sweble.wikitext.parser.nodes.WtWhitespace;
import org.sweble.wikitext.parser.nodes.WtXmlEndTag;
import org.sweble.wikitext.parser.nodes.WtXmlStartTag;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.utils.FmtInternalLogicError;
import de.fau.cs.osr.utils.StringTools;

public class TicksAnalyzer
{
	public static WtNode process(ParserConfig config, WtNode a)
	{
		return (new TicksAnalyzer(config)).process(a);
	}

	// =========================================================================

	private WikitextNodeFactory nf;

	// =========================================================================

	public TicksAnalyzer(ParserConfig config)
	{
		this.nf = config.getNodeFactory();
	}

	public WtNode process(WtNode a)
	{
		LinkedList<Line> lines = new LinkedList<Line>();

		new LineAnalyzer(lines).go(a);

		if (lines.isEmpty())
			return a;

		analyzeOddTicksCombos(lines);

		return (WtNode) new TicksConverter(lines).go(a);
	}

	// =========================================================================

	private void analyzeOddTicksCombos(LinkedList<Line> lines)
	{
		for (Line line : lines)
		{
			if ((line.numBold % 2 == 1) && (line.numItalics % 2 == 1))
			{
				int firstSpace = -1;
				int firstSlWord = -1;
				int firstMlWord = -1;

				for (int i = 0; i < line.ticks.size(); ++i)
				{
					LineEntry entry = line.ticks.get(i);

					WtNode p = entry.previous;
					if (p == null || entry.tickCount != 3)
						continue;

					if (p instanceof WtContentNode)
					{
						WtContentNode c = (WtContentNode) p;

						p = null;
						if (!c.isEmpty())
							p = c.get(c.size() - 1);
					}

					char tMinus1 = '\0';
					char tMinus2 = '\0';
					if (p instanceof WtStringNode)
					{
						String t = ((WtStringNode) p).getContent();

						if (t.length() >= 1)
							tMinus1 = t.charAt(t.length() - 1);

						if (t.length() >= 2)
							tMinus2 = t.charAt(t.length() - 2);
					}

					if (tMinus1 == ' ')
					{
						if (firstSpace == -1)
							firstSpace = i;
					}
					else if (tMinus2 == ' ')
					{
						if (firstSlWord == -1)
							firstSlWord = i;
					}
					else
					{
						if (firstMlWord == -1)
							firstMlWord = i;
					}
				}

				if (firstSlWord != -1)
				{
					apostrophize(line.ticks.get(firstSlWord));
				}
				else if (firstMlWord != -1)
				{
					apostrophize(line.ticks.get(firstMlWord));
				}
				else if (firstSpace != -1)
				{
					apostrophize(line.ticks.get(firstSpace));
				}
			}
		}
	}

	private void apostrophize(LineEntry entry)
	{
		--entry.tickCount;

		if (entry.prefix != null)
		{
			String t = entry.prefix.getContent() + "'";
			entry.prefix.setContent(t);
		}
		else
		{
			entry.prefix = nf.text("'");
		}
	}

	// =========================================================================

	protected final class LineAnalyzer
			extends
				AstVisitor<WtNode>
	{
		private final LinkedList<Line> lines;

		private ArrayList<LineEntry> ticks;

		private int numItalics = 0;

		private int numBold = 0;

		private WtNode previous = null;

		public LineAnalyzer(LinkedList<Line> lines)
		{
			this.lines = lines;
		}

		@Override
		protected Object after(WtNode node, Object result)
		{
			finishLine();
			return node;
		}

		public void visit(WtNode n)
		{
			iterate(n);
		}

		public void visit(WtNodeList list)
		{
			iterateContent(list);
		}

		private void iterateContent(WtNodeList list)
		{
			previous = null;
			for (WtNode n : list)
			{
				dispatch(n);
				previous = n;
			}
			previous = null;
		}

		public void visit(WtNewline ws)
		{
			finishLine();
		}

		public void visit(WtWhitespace ws)
		{
			if (ws.getHasNewline())
				finishLine();
		}

		public void visit(WtListItem n)
		{
			iterateContent(n);
			finishLine();
		}

		public void visit(WtDefinitionListTerm n)
		{
			iterateContent(n);
			finishLine();
		}

		public void visit(WtDefinitionListDef n)
		{
			iterateContent(n);
			finishLine();
		}

		public void visit(WtSemiPreLine n)
		{
			iterateContent(n);
			finishLine();
		}

		public void visit(WtTableCell n)
		{
			// Inline table cells are on one line but we want to finish after
			// each line to prevent leakage of tick formatting into adjacent 
			// cells
			iterateContent(n.getBody());
			finishLine();
		}

		public void visit(WtTableHeader n)
		{
			// Inline table cells are on one line but we want to finish after
			// each line to prevent leakage of tick formatting into adjacent 
			// cells
			iterateContent(n.getBody());
			finishLine();
		}

		public void visit(WtLeafNode n)
		{
			// Nothing to do here
		}

		public void visit(WtTicks n)
		{
			if (ticks == null)
				ticks = new ArrayList<TicksAnalyzer.LineEntry>();

			int tickCount = n.getTickCount();
			switch (tickCount)
			{
				case 2:
					ticks.add(new LineEntry(null, null, 2));
					++numItalics;
					break;

				case 3:
					ticks.add(new LineEntry(previous, null, 3));
					++numBold;
					break;

				case 4:
					ticks.add(new LineEntry(previous, nf.text("'"), 3));
					++numBold;
					break;

				case 5:
					ticks.add(new LineEntry(null, null, 5));
					++numBold;
					++numItalics;
					break;

				default:
					if (n.getTickCount() <= 5)
						throw new FmtInternalLogicError();

					String excessTicks = StringTools.strrep('\'', tickCount - 5);

					ticks.add(new LineEntry(null, nf.text(excessTicks), 5));
					++numBold;
					++numItalics;
					break;
			}
		}

		private void finishLine()
		{
			if (ticks == null)
				return;

			lines.add(new Line(numItalics, numBold, ticks));

			numItalics = 0;
			numBold = 0;
			ticks = null;
		}
	}

	// =========================================================================

	private static enum State
	{
		None,
		Italics,
		Bold,
		ItalicsBold,
		BoldItalics,
	}

	// =========================================================================

	protected final class TicksConverter
			extends
				AstVisitor<WtNode>
	{
		private final Iterator<Line> lineIter;

		private Iterator<LineEntry> entryIter;

		private State state = State.None;

		private boolean elementStartedItalic = false;
		
		private boolean elementStartedBold = false;
		
		public TicksConverter(LinkedList<Line> lines)
		{
			this.lineIter = lines.iterator();
			if (lineIter.hasNext())
				this.entryIter = lineIter.next().ticks.iterator();
		}

		public WtNode visit(WtNode n)
		{
			mapInPlace(n);
			return n;
		}

		public WtNode visit(WtLeafNode n)
		{
			// Nothing to do here
			return n;
		}

		public WtNode visit(WtTicks n)
		{
			LineEntry entry = nextEntry();

			WtNodeList result = nf.list(entry.prefix);

			toTag(entry, result);

			return result;
		}

		public WtNode visit(WtXmlStartTag n)
		{
			if (n.getName().equalsIgnoreCase("i"))
			{
				switch (state)
				{
					case Italics:
					case BoldItalics:
					case ItalicsBold:
						break;
					case Bold:
						state = State.BoldItalics;
						break;
					case None:
						state = State.Italics;
						break;
				}
				elementStartedItalic = true;
			}
			else if (n.getName().equalsIgnoreCase("b"))
			{
				switch (state)
				{
					case Bold:
					case BoldItalics:
					case ItalicsBold:
						break;
					case Italics:
						state = State.ItalicsBold;
						break;
					case None:
						state = State.Bold;
						break;
				}
				elementStartedBold = true;
			}
			mapInPlace(n);
			return n;
		}

		public WtNode visit(WtXmlEndTag n)
		{
			if (n.getName().equalsIgnoreCase("i"))
			{
				switch (state)
				{
					case Italics:
						state = State.None;
						break;
					case BoldItalics:
					case ItalicsBold:
						state = State.Bold;
						break;
					case Bold:
					case None:
						break;
				}
				elementStartedItalic = false;
			}
			else if (n.getName().equalsIgnoreCase("b"))
			{
				switch (state)
				{
					case Bold:
						state = State.None;
						break;
					case BoldItalics:
					case ItalicsBold:
						state = State.Italics;
						break;
					case Italics:
					case None:
						break;
				}
				elementStartedBold = false;
			}
			mapInPlace(n);
			return n;
		}

		public WtNode visit(WtNewline newline)
		{
			WtNodeList result = closeRemainingTags();
			if (result == null)
				return newline;
			result.add(newline);
			return result;
		}

		public WtNode visit(WtWhitespace ws)
		{
			if (!ws.getHasNewline())
				return ws;
			WtNodeList result = closeRemainingTags();
			if (result == null)
				return ws;
			result.add(ws);
			return result;
		}

		public WtNode visit(WtListItem n)
		{
			return implicitLineScope(n);
		}

		public WtNode visit(WtDefinitionListTerm n)
		{
			return implicitLineScope(n);
		}

		public WtNode visit(WtDefinitionListDef n)
		{
			return implicitLineScope(n);
		}

		public WtNode visit(WtSemiPreLine n)
		{
			return implicitLineScope(n);
		}

		public WtNode visit(WtTableCell n)
		{
			implicitLineScope(n.getBody());
			return n;
		}

		public WtNode visit(WtTableHeader n)
		{
			implicitLineScope(n.getBody());
			return n;
		}

		private WtNode implicitLineScope(WtNodeList content)
		{
			mapInPlace(content);
			finishLine(content);
			return content;
		}

		private void finishLine(WtNodeList body)
		{
			WtNodeList result = closeRemainingTags();
			if (result == null)
				return;
			body.add(result);
		}

		private LineEntry nextEntry()
		{
			if (!entryIter.hasNext())
			{
				Line line = lineIter.next();
				entryIter = line.ticks.iterator();
			}
			return entryIter.next();
		}

		private void toTag(LineEntry entry, WtNodeList result)
		{
			switch (entry.tickCount)
			{
				case 2:
					switch (state)
					{
						case Italics:
							result.add(ITALICS.createClose(nf, false));
							state = State.None;
							break;
						case BoldItalics:
							result.add(ITALICS.createClose(nf, false));
							state = State.Bold;
							break;
						case ItalicsBold:
							result.add(BOLD.createClose(nf, true));
							result.add(ITALICS.createClose(nf, false));
							result.add(BOLD.createOpen(nf, true));
							state = State.Bold;
							break;
						case Bold:
							result.add(ITALICS.createOpen(nf, false));
							state = State.BoldItalics;
							break;
						case None:
							result.add(ITALICS.createOpen(nf, false));
							state = State.Italics;
							break;
					}
					break;

				case 3:
					switch (state)
					{
						case Bold:
							result.add(BOLD.createClose(nf, false));
							state = State.None;
							break;
						case BoldItalics:
							result.add(ITALICS.createClose(nf, true));
							result.add(BOLD.createClose(nf, false));
							result.add(ITALICS.createOpen(nf, true));
							state = State.Italics;
							break;
						case ItalicsBold:
							result.add(BOLD.createClose(nf, false));
							state = State.Italics;
							break;
						case Italics:
							result.add(BOLD.createOpen(nf, false));
							state = State.ItalicsBold;
							break;
						case None:
							result.add(BOLD.createOpen(nf, false));
							state = State.Bold;
							break;
					}
					break;

				case 5:
					switch (state)
					{
						case Italics:
							result.add(ITALICS.createClose(nf, false));
							result.add(BOLD.createOpen(nf, false));
							state = State.Bold;
							break;
						case Bold:
							result.add(BOLD.createClose(nf, false));
							result.add(ITALICS.createOpen(nf, false));
							state = State.Italics;
							break;
						case BoldItalics:
							result.add(ITALICS.createClose(nf, false));
							result.add(BOLD.createClose(nf, false));
							state = State.None;
							break;
						case ItalicsBold:
							result.add(BOLD.createClose(nf, false));
							result.add(ITALICS.createClose(nf, false));
							state = State.None;
							break;
						case None:
							result.add(ITALICS.createOpen(nf, false));
							result.add(BOLD.createOpen(nf, false));
							state = State.ItalicsBold;
							break;
					}
					break;
			}
		}

		private WtNodeList closeRemainingTags()
		{
			WtNodeList result = null;
			switch (state)
			{
				case Italics:
					if (!elementStartedItalic)
					{
					result = nf.list();
					result.add(ITALICS.createClose(nf, true));
						state = State.None;
					}
					break;
				case Bold:
					if (!elementStartedBold)
					{
					result = nf.list();
					result.add(BOLD.createClose(nf, true));
						state = State.None;
					}
					break;
				case BoldItalics:
					if (!elementStartedItalic && !elementStartedBold)
					{
					result = nf.list();
					result.add(ITALICS.createClose(nf, true));
					result.add(BOLD.createClose(nf, true));
						state = State.None;
					}
					else if (elementStartedItalic)
					{
						result = nf.list();
						result.add(BOLD.createClose(nf, true));
						state = State.Italics;
					}
					else if (!elementStartedBold)
					{
						result = nf.list();
						result.add(ITALICS.createClose(nf, true));
						state = State.Bold;
					}
					break;
				case ItalicsBold:
					if (!elementStartedItalic && !elementStartedBold)
					{
					result = nf.list();
					result.add(BOLD.createClose(nf, true));
					result.add(ITALICS.createClose(nf, true));
						state = State.None;
					}
					else if (elementStartedItalic)
					{
						result = nf.list();
						result.add(BOLD.createClose(nf, true));
						state = State.Italics;
					}
					else if (!elementStartedBold)
					{
						result = nf.list();
						result.add(ITALICS.createClose(nf, true));
						state = State.Bold;
					}
					break;
				case None:
					break;
			}
			return result;
		}
	}

	// =========================================================================

	protected final static class Line
	{
		public final int numItalics;

		public final int numBold;

		public final ArrayList<LineEntry> ticks;

		public Line(int numItalics, int numBold, ArrayList<LineEntry> ticks)
		{
			this.numItalics = numItalics;
			this.numBold = numBold;
			this.ticks = ticks;
		}

		@Override
		public String toString()
		{
			return String.format(
					"Line(#i = %d, #b = %d): %s",
					numItalics,
					numBold,
					(ticks != null ? ticks.toString() : "-"));
		}
	}

	// =========================================================================

	protected final static class LineEntry
	{
		public final WtNode previous;

		public WtText prefix;

		public int tickCount;

		public LineEntry(WtNode previous, WtText prefix, int tickCount)
		{
			this.previous = previous;
			this.prefix = prefix;
			this.tickCount = tickCount;
		}

		@Override
		public String toString()
		{
			String pv = "null";
			if (previous != null)
			{
				pv = previous.getNodeName();
				if (previous.isNodeType(WtNode.NT_TEXT))
				{
					pv = ((WtText) previous).getContent();
					if (pv.length() > 16)
					{
						pv = pv.substring(pv.length() - (16 - 4));
						pv = "... " + pv;
					}
				}
				pv = '"' + pv + '"';
			}

			String pf = "-";
			if (prefix != null)
				pf = '"' + prefix.getContent() + '"';

			return String.format(
					"LineEntry(%s, %s, %d)",
					pv,
					pf,
					tickCount);
		}
	}
}
