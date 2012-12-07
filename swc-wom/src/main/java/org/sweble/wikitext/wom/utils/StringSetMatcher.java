package org.sweble.wikitext.wom.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StringSetMatcher
{
	class Node
	{
		String match;
		
		Map<Character, Node> children;
		
		void add(String search)
		{
			add(search, 0);
		}
		
		void add(String search, int i)
		{
			char prefix = search.charAt(i);
			if (children == null)
				children = new HashMap<Character, Node>();
			Node sub = children.get(prefix);
			if (sub == null)
			{
				sub = new Node();
				children.put(prefix, sub);
			}
			++i;
			if (i < search.length())
			{
				sub.add(search, i);
			}
			else
			{
				sub.match = search;
			}
		}
		
		public Node match(char ch)
		{
			if (children == null)
				return null;
			return children.get(ch);
		}
	}
	
	public class Match
	{
		private final int pos;
		
		private final String match;
		
		public Match(int pos, String match)
		{
			super();
			this.pos = pos;
			this.match = match;
		}
		
		public int getPos()
		{
			return pos;
		}
		
		public String getMatch()
		{
			return match;
		}
	}
	
	private final Node root;
	
	public StringSetMatcher(Collection<String> searchStrings)
	{
		root = new Node();
		for (String search : searchStrings)
			root.add(search);
	}
	
	public Match find(String search)
	{
		return find(search, 0);
	}
	
	public Match find(String search, int from)
	{
		int len = search.length();
		for (int i = from; i < len; ++i)
		{
			char ch = search.charAt(i);
			
			String bestMatch = null;
			Node level = root;
			for (int j = i;;)
			{
				Node next = level.match(ch);
				if (next == null)
					break;
				if (next.match != null)
					bestMatch = next.match;
				if (++j >= len)
					break;
				ch = search.charAt(j);
				level = next;
			}
			
			if (bestMatch != null)
				return new Match(i, bestMatch);
		}
		return null;
	}
}
