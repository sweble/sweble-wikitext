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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sweble.wikitext.parser.ParserConfig;

import de.fau.cs.osr.utils.XmlGrammar;

/**
 * Expects the string to contain only valid Unicode characters. It must not
 * contain invalid, non- or private use characters. It further expects the
 * string to not contain the following characters:
 * [\u0000-\u001F\u007F\uFFFD<>{}|[\]].
 * 
 * The parser checks if the link target contains any of the following entites,
 * which are not allowed in link targets:
 * 
 * <ul>
 * <li>Percent encoding of URIs:
 * 
 * <pre>
 * %[0-9A-Fa-f]{2}
 * </pre>
 * 
 * </li>
 * <li>XML entity references:
 * 
 * <pre>
 * &amp;&lt;Name&gt;;
 * </pre>
 * 
 * </li>
 * <li>XML char references:
 * 
 * <pre>
 * (&#[0-9]+;)|(&#x[0-9A-Fa-f]+;)
 * </pre>
 * 
 * </li>
 * <li>Relative path components:
 * 
 * <pre>
 * (^\.\.?($|/))|(/\.\.?/)|(/\.\.?$)
 * </pre>
 * 
 * </li>
 * <li>No magic tilde sequences:
 * 
 * <pre>
 * ~~~
 * </pre>
 * 
 * </li>
 * </ul>
 */
public class LinkTargetParser
{
	private String title;
	
	private String fragment;
	
	private String namespace;
	
	private String interwiki;
	
	private boolean initialColon;
	
	// =========================================================================
	
	private final static Pattern bidiCharPattern = Pattern.compile(
			"[\u200E\u200F\u202A-\u202E]");
	
	private final static Pattern spacePlusPattern = Pattern.compile(
			"[ _\u00A0\u1680\u180E\u2000-\u200A\u2028\u2029\u202F\u205F\u3000]+");
	
	private final static Pattern namespaceSeparatorPattern = Pattern.compile(
			"^(.+?)_*:_*(.*)$");
	
	private final static Pattern invalidTitle = Pattern.compile(
			// Percent encoding for URIs
			"(%[0-9A-Fa-f]{2})" +
					
					// XML entity reference
					"|(&" + XmlGrammar.RE_XML_NAME + ";)" +
					
					// XML char reference
					"|((&#[0-9]+;)|(&#x[0-9A-Fa-f]+;))" +
					
					// Relative path components
					"|(^\\.\\.?($|/))" +
					"|(/\\.\\.?/)" +
					"|(/\\.\\.?$)" +
					
					// No magic tilde sequences
					"|(~~~)");
	
	// =========================================================================
	
	public void parse(ParserConfig config, final String target) throws LinkTargetException
	{
		String result = target;
		String resultNs = null;
		String resultIw = null;
		String resultFragment = null;
		boolean resultInitialColon = false;
		
		// Decode URL encoded characters
		{
			result = urlDecode(result);
		}
		
		// Decode XML entities
		{
			result = xmlDecode(config, result);
		}
		
		// Strip bidi override characters
		{
			Matcher matcher = bidiCharPattern.matcher(result);
			result = matcher.replaceAll("");
		}
		
		// Trim whitespace
		{
			result = trim(result);
		}
		
		// Strip whitespace characters
		{
			Matcher matcher = spacePlusPattern.matcher(result);
			result = matcher.replaceAll("_");
		}
		
		// Remove trailing whitespace characters
		result = trimUnderscore(result);
		
		if (result.isEmpty())
			throw new LinkTargetException(target, "Target has empty title");
		
		if (result.charAt(0) == ':')
		{
			resultInitialColon = true;
			result = result.substring(1);
			result = trimUnderscore(result);
		}
		
		boolean gotIl = false;
		boolean gotNsOrIl = false;
		
		// ---
		
		{
			Matcher matcher = namespaceSeparatorPattern.matcher(result);
			if (matcher.matches())
			{
				String nsName = matcher.group(1);
				
				if (config.isNamespace(nsName))
				{
					result = matcher.group(2);
					resultNs = nsName;
					
					if (config.isTalkNamespace(resultNs))
					{
						matcher = namespaceSeparatorPattern.matcher(result);
						nsName = matcher.group(1);
						if (matcher.matches() && (config.isNamespace(nsName) || config.isInterwikiName(nsName)))
							throw new LinkTargetException(
									target,
									"The Talk namespace in a link target may not be followed by another namespace or interwiki name");
					}
				}
				else if (config.isInterwikiName(nsName))
				{
					result = matcher.group(2);
					
					if (config.isIwPrefixOfThisWiki(nsName))
					{
						if (result.isEmpty())
						{
							throw new LinkTargetException(
									target,
									"Empty article title!");
						}
						else
						{
							matcher = namespaceSeparatorPattern.matcher(result);
							if (matcher.matches())
							{
								nsName = matcher.group(1);
								
								if (config.isNamespace(nsName))
								{
									result = matcher.group(2);
									resultNs = nsName;
									
									if (config.isTalkNamespace(resultNs))
									{
										matcher = namespaceSeparatorPattern.matcher(result);
										nsName = matcher.group(1);
										if (matcher.matches() && (config.isNamespace(nsName) || config.isInterwikiName(nsName)))
											throw new LinkTargetException(
													target,
													"The Talk namespace in a link target may not be followed by another namespace or interwiki name");
									}
								}
								else if (config.isInterwikiName(nsName))
								{
									throw new LinkTargetException(
											target,
											"The namespace in a link target may not be followed by another namespace or interwiki name");
								}
							}
						}
					}
					else
					{
						resultIw = nsName;
						
						if (!result.isEmpty() && result.charAt(0) == ':')
						{
							resultInitialColon = true;
							result = result.substring(1);
							result = trimUnderscore(result);
						}
					}
				}
			}
		}
		
		// ---
		
		/*
		while (true)
		{
			Matcher matcher = namespaceSeparatorPattern.matcher(result);
			if (matcher.matches())
			{
				String nsName = matcher.group(1);
				
				if (config.isNamespace(nsName))
				{
					// if part was already a namespace, this is wrong ...
					if (gotNsOrIl)
					{
						if (config.isTalkNamespace(resultNs))
							throw new LinkTargetException(
									target,
									"The Talk namespace in a link target may not be followed by another namespace or interwiki name");
						
						break;
					}
					
					result = matcher.group(2);
					resultNs = nsName;
					
					gotNsOrIl = true;
				}
				else
				{
					if (config.isInterwikiName(nsName))
					{
						if (gotNsOrIl || gotIl)
							throw new LinkTargetException(
									target,
									"The namespace in a link target may not be followed by another namespace or interwiki name");
						
						result = matcher.group(2);
						
						if (config.isIwPrefixOfThisWiki(nsName))
						{
							if (result.isEmpty())
								throw new LinkTargetException(
										target,
										"Empty article title!");
							
							gotIl = true;
						}
						else
						{
							resultIw = nsName;
							
							if (!result.isEmpty() && result.charAt(0) == ':')
							{
								resultInitialColon = true;
								result = result.substring(1);
								result = trimUnderscore(result);
							}
							
							gotNsOrIl = true;
						}
					}
					else
						break;
				}
			}
			else
				break;
		}
		*/
		
		{
			int i = result.indexOf('#');
			if (i != -1)
			{
				resultFragment = result.substring(i + 1);
				resultFragment = trimUnderscore(resultFragment);
				
				result = result.substring(0, i);
				result = trimUnderscore(result);
			}
		}
		
		{
			Matcher matcher = invalidTitle.matcher(result);
			if (matcher.find())
				throw new LinkTargetException(
						target,
						"The title contains invalid entities");
		}
		
		// Empty links to a namespace alone are not allowed
		if (result.isEmpty() &&
				resultIw == null &&
				resultNs != null)
		{
			throw new LinkTargetException(
					target,
					"A namespace alone is not a valid link target");
		}
		
		this.title = result;
		this.fragment = resultFragment;
		this.namespace = resultNs;
		this.interwiki = resultIw;
		this.initialColon = resultInitialColon;
	}
	
	/* FIXME: Move to utility class! */
	public static String urlDecode(String result)
	{
		// It's intentional that only '%' characters trigger the decoding.
		// MediaWiki does not decode '+' characters if there's not at least
		// one '%' character :D
		if (result.indexOf('%') >= 0)
		{
			StringBuilder b = new StringBuilder();
			for (int i = 0; i < result.length(); ++i)
			{
				char ch = result.charAt(i);
				if (ch == '%' && i + 2 < result.length())
				{
					String num = result.substring(i + 1, i + 3);
					try
					{
						int val = Integer.valueOf(num, 16);
						if (val >= 0x20 && val < 0x7F)
						{
							ch = (char) val;
							i += 2;
						}
					}
					catch (NumberFormatException e)
					{
					}
				}
				else if (ch == '+')
				{
					ch = ' ';
				}
				
				b.append(ch);
			}
			result = b.toString();
		}
		
		return result;
	}
	
	/* FIXME: Move to utility class! */
	public static String xmlDecode(ParserConfig config, String result)
	{
		if (result.indexOf('&') >= 0)
		{
			Pattern rx = XmlGrammar.xmlReference();
			
			int start = 0;
			StringBuilder b = new StringBuilder();
			while (true)
			{
				Matcher m = rx.matcher(result);
				if (m.find(start))
				{
					b.append(result.substring(start, m.start()));
					
					String resolved = null;
					if (m.group(1) != null)
					{
						resolved = config.resolveXmlEntity(m.group(1));
					}
					else
					{
						try
						{
							boolean decimal = m.group(2) != null;
							
							String num = decimal ? m.group(2) : m.group(3);
							
							int val = Integer.valueOf(num, decimal ? 10 : 16);
							if (val >= 0x20 && val != 0x7F)
								resolved = String.valueOf((char) val);
						}
						catch (NumberFormatException e)
						{
						}
					}
					
					if (resolved != null)
						b.append(resolved);
					else
						b.append(result.substring(m.start(), m.end()));
					
					start = m.end();
				}
				else
				{
					if (start < result.length())
						b.append(result.substring(start));
					
					break;
				}
			}
			
			result = b.toString();
		}
		
		return result;
	}
	
	/* FIXME: Move to utility class! */
	static String trimUnderscore(String result)
	{
		int i;
		for (i = 0; i < result.length() && result.charAt(i) == '_'; ++i)
			;
		
		int j;
		for (j = result.length() - 1; j >= 0 && result.charAt(j) == '_'; --j)
			;
		
		return result.substring(i, j + 1);
	}
	
	/* FIXME: Move to utility class! */
	static String trim(String input)
	{
		int i = 0;
		int j = input.length();
		
		while ((i < j) && Character.isWhitespace(input.charAt(i)))
			++i;
		
		while ((i < j) && Character.isWhitespace(input.charAt(j - 1)))
			--j;
		
		return ((i > 0) || (j < input.length())) ? input.substring(i, j) : input;
	}
	
	// =========================================================================
	
	public String getTitle()
	{
		return title;
	}
	
	public String getFragment()
	{
		return fragment;
	}
	
	public String getNamespace()
	{
		return namespace;
	}
	
	public String getInterwiki()
	{
		return interwiki;
	}
	
	public boolean isInitialColon()
	{
		return initialColon;
	}
}
