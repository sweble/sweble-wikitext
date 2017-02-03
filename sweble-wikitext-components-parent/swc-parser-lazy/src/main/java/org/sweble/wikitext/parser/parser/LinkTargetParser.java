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
import org.sweble.wikitext.parser.parser.LinkTargetException.Reason;

import de.fau.cs.osr.utils.StringTools;
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
					"|(~~~)" +

					// No invalid characters
					"|[\\u0000-\\u001F\\u007F\\uFFFD<>{}\\|\\[\\]]");

	// =========================================================================

	public void parse(ParserConfig config, final String target) throws LinkTargetException
	{
		String result = target;

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

		// Trim whitespace (*)
		{
			result = StringTools.trim(result);
		}

		/*
		// Remove trailing whitespace characters
		result = StringTools.trimUnderscores(result);
		*/

		if (result.isEmpty())
			throw new LinkTargetException(Reason.EMPTY_TARGET, target);

		// Has the link an initial colon? Can be reset by identifyNamespaces!
		if (result.charAt(0) == ':')
		{
			this.initialColon = true;
			result = result.substring(1);
			result = StringTools.trimUnderscores(result);
		}

		// Identify namespaces and interwiki names
		result = identifyNamespaces(config, target, result);

		// Get the part after the '#'
		result = extractFragment(result);

		// Perform sanity checks on remaining title
		{
			// Fixes issue #45:
			// "&_foo_;" become "& foo ;" and will not be recognized as illegal entity.
			// Related to (**)
			result = result.replace('_', ' ');

			Matcher matcher = invalidTitle.matcher(result);
			if (matcher.find())
				throw new LinkTargetException(
						Reason.INVALID_ENTITIES,
						target,
						matcher.group());
		}

		// Fixes issue #45:
		// (**) Strip whitespace characters
		// IMPORTANT: Was done after (*). Led to problems for titles like
		// '& foo ;' which became '&_foo_;' and were treated as illegal XML
		// entities by the sanity check. Also when done here it will not
		// affect the fragment which seems to be a good thing...
		{
			Matcher matcher = spacePlusPattern.matcher(result);
			result = matcher.replaceAll("_");
		}

		// Empty links to a namespace alone are not allowed
		if (result.isEmpty() &&
				this.interwiki == null &&
				this.namespace != null)
		{
			throw new LinkTargetException(Reason.ONLY_NAMESPACE, target);
		}

		this.title = result;
	}

	private String identifyNamespaces(
			ParserConfig config,
			final String target,
			String result) throws LinkTargetException
	{
		Matcher matcher = namespaceSeparatorPattern.matcher(result);
		if (matcher.matches())
		{
			// We have at least ONE namespace
			String nsName = matcher.group(1);

			if (config.isNamespace(nsName))
			{
				// It is a KNOWN namespace
				result = matcher.group(2);
				this.namespace = nsName;

				checkNoNsAfterTalkNs(config, target, result, nsName);
			}
			else if (config.isInterwikiName(nsName))
			{
				// It is a KNOWN interwiki name
				result = matcher.group(2);

				if (config.isIwPrefixOfThisWiki(nsName))
				{
					// It points to THIS wiki
					if (result.isEmpty())
					{
						throw new LinkTargetException(Reason.NO_ARTICLE_TITLE, target);
					}
					else
					{
						matcher = namespaceSeparatorPattern.matcher(result);
						if (matcher.matches())
						{
							// There are more namespace parts
							nsName = matcher.group(1);

							if (config.isNamespace(nsName))
							{
								result = matcher.group(2);
								this.namespace = nsName;

								checkNoNsAfterTalkNs(config, target, result, nsName);
							}
							else if (config.isInterwikiName(nsName))
							{
								throw new LinkTargetException(Reason.IW_IW_LINK, target, nsName);
							}
						}
					}
				}
				else
				{
					this.interwiki = nsName;

					if (!result.isEmpty() && result.charAt(0) == ':')
					{
						this.initialColon = true;
						result = result.substring(1);
						result = StringTools.trimUnderscores(result);
					}
				}
			}
		}
		return result;
	}

	private void checkNoNsAfterTalkNs(
			ParserConfig config,
			final String target,
			String result,
			String nsName) throws LinkTargetException
	{
		Matcher matcher;
		if (config.isTalkNamespace(nsName))
		{
			matcher = namespaceSeparatorPattern.matcher(result);
			if (matcher.matches())
			{
				nsName = matcher.group(1);
				if ((config.isNamespace(nsName) || config.isInterwikiName(nsName)))
					throw new LinkTargetException(Reason.TALK_NS_IW_LINK, target, nsName);
			}
		}
	}

	private String extractFragment(String result)
	{
		int i = result.indexOf('#');
		if (i != -1)
		{
			String fragment = result.substring(i + 1);
			this.fragment = StringTools.trimUnderscores(fragment);

			result = result.substring(0, i);
			result = StringTools.trimUnderscores(result);
		}
		return result;
	}

	private static String urlDecode(String text)
	{
		// It's intentional that only '%' characters trigger the decoding.
		// MediaWiki does not decode '+' characters if there's not at least
		// one '%' character :D
		if (text.indexOf('%') >= 0)
			return StringTools.urlDecode(text);
		return text;
	}

	private static String xmlDecode(ParserConfig config, String text)
	{
		if (text.indexOf('&') >= 0)
			return StringTools.xmlDecode(text, config);
		return text;
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
