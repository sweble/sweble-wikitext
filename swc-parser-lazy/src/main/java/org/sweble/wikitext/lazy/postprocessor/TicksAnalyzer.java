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

import static org.sweble.wikitext.lazy.postprocessor.IntermediateTags.BOLD;
import static org.sweble.wikitext.lazy.postprocessor.IntermediateTags.ITALICS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.sweble.wikitext.lazy.parser.DefinitionDefinition;
import org.sweble.wikitext.lazy.parser.DefinitionTerm;
import org.sweble.wikitext.lazy.parser.EnumerationItem;
import org.sweble.wikitext.lazy.parser.ItemizationItem;
import org.sweble.wikitext.lazy.parser.Newline;
import org.sweble.wikitext.lazy.parser.SemiPreLine;
import org.sweble.wikitext.lazy.parser.Ticks;
import org.sweble.wikitext.lazy.parser.Whitespace;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.ContentNode;
import de.fau.cs.osr.ptk.common.ast.LeafNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.StringContentNode;
import de.fau.cs.osr.ptk.common.ast.Text;
import de.fau.cs.osr.utils.FmtInternalLogicError;
import de.fau.cs.osr.utils.StringUtils;

public class TicksAnalyzer
{
	protected final static class LineEntry
	{
		public final AstNode previous;
		
		public Text prefix;
		
		public int tickCount;
		
		public LineEntry(AstNode previous, Text prefix, int tickCount)
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
				if (previous.isNodeType(AstNode.NT_TEXT))
				{
					pv = ((Text) previous).getContent();
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
	
	public static AstNode process(AstNode a)
	{
		LinkedList<Line> lines = new LinkedList<Line>();
		
		new LineAnalyzer(lines).go(a);
		
		if (lines.isEmpty())
			return a;
		
		analyzeOddTicksCombos(lines);
		
		return (AstNode) new TicksConverter(lines).go(a);
	}
	
	// =========================================================================
	
	protected final static class LineAnalyzer
			extends
				AstVisitor
	{
		private final LinkedList<Line> lines;
		
		private ArrayList<LineEntry> ticks;
		
		private int numItalics = 0;
		
		private int numBold = 0;
		
		private AstNode previous = null;
		
		public LineAnalyzer(LinkedList<Line> lines)
		{
			this.lines = lines;
		}
		
		@Override
		protected Object after(AstNode node, Object result)
		{
			finishLine();
			return node;
		}
		
		public void visit(AstNode n)
		{
			iterate(n);
		}
		
		public void visit(NodeList list)
		{
			previous = null;
			for (AstNode n : list)
			{
				dispatch(n);
				previous = n;
			}
			previous = null;
		}
		
		public void visit(Newline ws)
		{
			finishLine();
		}
		
		public void visit(Whitespace ws)
		{
			if (ws.getHasNewline())
				finishLine();
		}
		
		public void visit(ItemizationItem n)
		{
			visit(n.getContent());
			finishLine();
		}
		
		public void visit(EnumerationItem n)
		{
			visit(n.getContent());
			finishLine();
		}
		
		public void visit(DefinitionTerm n)
		{
			visit(n.getContent());
			finishLine();
		}
		
		public void visit(DefinitionDefinition n)
		{
			visit(n.getContent());
			finishLine();
		}
		
		public void visit(SemiPreLine n)
		{
			visit(n.getContent());
			finishLine();
		}
		
		public void visit(LeafNode n)
		{
			// Nothing to do here
		}
		
		public void visit(Ticks n)
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
					ticks.add(new LineEntry(previous, new Text("'"), 3));
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
					
					String excessTicks = StringUtils.strrep('\'', tickCount - 5);
					
					ticks.add(new LineEntry(null, new Text(excessTicks), 5));
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
	
	private static void analyzeOddTicksCombos(LinkedList<Line> lines)
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
					
					AstNode p = entry.previous;
					if (p == null || entry.tickCount != 3)
						continue;
					
					if (p instanceof ContentNode)
					{
						ContentNode c = (ContentNode) p;
						
						p = null;
						if (!c.isEmpty())
							p = c.get(c.size() - 1);
					}
					
					char tMinus1 = '\0';
					char tMinus2 = '\0';
					if (p instanceof StringContentNode)
					{
						String t = ((StringContentNode) p).getContent();
						
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
	
	private static void apostrophize(LineEntry entry)
	{
		--entry.tickCount;
		
		if (entry.prefix != null)
		{
			String t = entry.prefix.getContent() + "'";
			entry.prefix.setContent(t);
		}
		else
		{
			entry.prefix = new Text("'");
		}
	}
	
	// =========================================================================
	
	protected final static class TicksConverter
			extends
				AstVisitor
	{
		private static enum State
		{
			None,
			Italics,
			Bold,
			ItalicsBold,
			BoldItalics,
		}
		
		private Iterator<Line> lineIter;
		
		private Iterator<LineEntry> entryIter;
		
		private State state = State.None;
		
		public TicksConverter(LinkedList<Line> lines)
		{
			this.lineIter = lines.iterator();
			if (lineIter.hasNext())
				this.entryIter = lineIter.next().ticks.iterator();
		}
		
		public AstNode visit(AstNode n)
		{
			mapInPlace(n);
			return n;
		}
		
		public AstNode visit(LeafNode n)
		{
			// Nothing to do here
			return n;
		}
		
		public AstNode visit(Ticks n)
		{
			LineEntry entry = nextEntry();
			
			NodeList result = new NodeList(entry.prefix);
			
			toTag(entry, result);
			
			return result;
		}
		
		public AstNode visit(Newline newline)
		{
			NodeList result = closeRemainingTags();
			if (result == null)
				return newline;
			
			state = State.None;
			result.add(newline);
			return result;
		}
		
		public AstNode visit(Whitespace ws)
		{
			if (!ws.getHasNewline())
				return ws;
			
			NodeList result = closeRemainingTags();
			if (result == null)
				return ws;
			
			state = State.None;
			result.add(ws);
			return result;
		}
		
		public AstNode visit(ItemizationItem n)
		{
			return implicitLineScope(n);
		}
		
		public AstNode visit(EnumerationItem n)
		{
			return implicitLineScope(n);
		}
		
		public AstNode visit(DefinitionTerm n)
		{
			return implicitLineScope(n);
		}
		
		public AstNode visit(DefinitionDefinition n)
		{
			return implicitLineScope(n);
		}
		
		public AstNode visit(SemiPreLine n)
		{
			return implicitLineScope(n);
		}
		
		private AstNode implicitLineScope(ContentNode n)
		{
			NodeList content = n.getContent();
			mapInPlace(content);
			finishLine(content);
			return n;
		}
		
		private void finishLine(NodeList body)
		{
			NodeList result = closeRemainingTags();
			if (result == null)
				return;
			
			state = State.None;
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
		
		private void toTag(LineEntry entry, NodeList result)
		{
			switch (entry.tickCount)
			{
				case 2:
					switch (state)
					{
						case Italics:
							result.add(ITALICS.createClose(false));
							state = State.None;
							break;
						case BoldItalics:
							result.add(ITALICS.createClose(false));
							state = State.Bold;
							break;
						case ItalicsBold:
							result.add(BOLD.createClose(true));
							result.add(ITALICS.createClose(false));
							result.add(BOLD.createOpen(true));
							state = State.Bold;
							break;
						case Bold:
							result.add(ITALICS.createOpen(false));
							state = State.BoldItalics;
							break;
						case None:
							result.add(ITALICS.createOpen(false));
							state = State.Italics;
							break;
					}
					break;
				
				case 3:
					switch (state)
					{
						case Bold:
							result.add(BOLD.createClose(false));
							state = State.None;
							break;
						case BoldItalics:
							result.add(ITALICS.createClose(true));
							result.add(BOLD.createClose(false));
							result.add(ITALICS.createOpen(true));
							state = State.Italics;
							break;
						case ItalicsBold:
							result.add(BOLD.createClose(false));
							state = State.Italics;
							break;
						case Italics:
							result.add(BOLD.createOpen(false));
							state = State.ItalicsBold;
							break;
						case None:
							result.add(BOLD.createOpen(false));
							state = State.Bold;
							break;
					}
					break;
				
				case 5:
					switch (state)
					{
						case Italics:
							result.add(ITALICS.createClose(false));
							result.add(BOLD.createOpen(false));
							state = State.Bold;
							break;
						case Bold:
							result.add(BOLD.createClose(false));
							result.add(ITALICS.createOpen(false));
							state = State.Italics;
							break;
						case BoldItalics:
							result.add(ITALICS.createClose(false));
							result.add(BOLD.createClose(false));
							state = State.None;
							break;
						case ItalicsBold:
							result.add(BOLD.createClose(false));
							result.add(ITALICS.createClose(false));
							state = State.None;
							break;
						case None:
							result.add(ITALICS.createOpen(false));
							result.add(BOLD.createOpen(false));
							state = State.ItalicsBold;
							break;
					}
					break;
			}
		}
		
		private NodeList closeRemainingTags()
		{
			NodeList result = null;
			switch (state)
			{
				case Italics:
					result = new NodeList();
					result.add(ITALICS.createClose(true));
					break;
				case Bold:
					result = new NodeList();
					result.add(BOLD.createClose(true));
					break;
				case BoldItalics:
					result = new NodeList();
					result.add(ITALICS.createClose(true));
					result.add(BOLD.createClose(true));
					break;
				case ItalicsBold:
					result = new NodeList();
					result.add(BOLD.createClose(true));
					result.add(ITALICS.createClose(true));
					break;
			}
			return result;
		}
	}
}
