/**
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

package org.sweble.wikitext.engine.utils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.map.MultiValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sweble.wikitext.engine.config.I18nAliasImpl;
import org.sweble.wikitext.engine.config.InterwikiImpl;
import org.sweble.wikitext.engine.config.NamespaceImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Samy Ateia, samyateia@hotmail.de
 */
public class LanguageConfigGenerator
{
	private static final Logger logger = LoggerFactory.getLogger(LanguageConfigGenerator.class);

	public static final String API_ENDPOINT_MAGICWORDS =
			".wikipedia.org/w/api.php?action=query&meta=siteinfo&siprop=magicwords&format=xml";

	public static final String API_ENDPOINT_INTERWIKIMAP =
			".wikipedia.org/w/api.php?action=query&meta=siteinfo&siprop=interwikimap&format=xml";

	public static final String API_ENDPOINT_NAMESPACES =
			".wikipedia.org/w/api.php?action=query&meta=siteinfo&siprop=namespaces&format=xml";

	public static final String API_ENDPOINT_NAMESPACEALIASES =
			".wikipedia.org/w/api.php?action=query&meta=siteinfo&siprop=namespacealiases&format=xml";

	// =========================================================================

	public static WikiConfig generateWikiConfig(String languagePrefix)
		throws IOException,
			ParserConfigurationException,
			SAXException
	{
		return generateWikiConfig(
				languagePrefix + " wiki",
				"https://" + languagePrefix + ".wikipedia.org",
				languagePrefix);
	}

	public static WikiConfig generateWikiConfig(
			String siteName,
			String siteURL,
			String languagePrefix)
		throws IOException,
			ParserConfigurationException,
			SAXException
	{
		String endpointPrefix = "https://" + languagePrefix;
		return generateWikiConfig(
				siteName,
				siteURL,
				languagePrefix,
				endpointPrefix + API_ENDPOINT_NAMESPACEALIASES,
				endpointPrefix + API_ENDPOINT_NAMESPACES,
				endpointPrefix + API_ENDPOINT_INTERWIKIMAP,
				endpointPrefix + API_ENDPOINT_MAGICWORDS);
	}

	public static WikiConfig generateWikiConfig(
			String siteName,
			String siteUrl,
			String languagePrefix,
			String apiUrlNamespacealiases,
			String apiUrlNamespaces,
			String apiUrlInterwikimap,
			String apiUrlMagicwords)
		throws IOException,
			ParserConfigurationException,
			SAXException
	{
		WikiConfigImpl wikiConfig = new WikiConfigImpl();
		wikiConfig.setSiteName(siteName);
		wikiConfig.setWikiUrl(siteUrl);
		wikiConfig.setContentLang(languagePrefix);
		wikiConfig.setIwPrefix(languagePrefix);

		DefaultConfigEnWp config = new DefaultConfigEnWp();
		config.configureEngine(wikiConfig);

		MultiValueMap namespaceAliases = getNamespaceAliases(apiUrlNamespacealiases);
		addNamespaces(wikiConfig, apiUrlNamespaces, namespaceAliases);
		addInterwikis(wikiConfig, apiUrlInterwikimap);
		addi18NAliases(wikiConfig, apiUrlMagicwords);

		config.addParserFunctions(wikiConfig, true);
		config.addTagExtensions(wikiConfig);

		return wikiConfig;
	}

	public static void addi18NAliases(
			WikiConfigImpl wikiConfig,
			String apiUrlMagicWords)
		throws IOException,
			ParserConfigurationException,
			SAXException
	{
		// Template when to add '#' prefix to i18n aliases
		WikiConfigImpl template = new WikiConfigImpl();
		new DefaultConfigEnWp().addI18nAliases(template);

		Document document = getXMLFromUrl(apiUrlMagicWords);
		NodeList apiI18nAliases = document.getElementsByTagName("magicword");

		for (int i = 0; i < apiI18nAliases.getLength(); i++)
		{
			Node apiI18nAlias = apiI18nAliases.item(i);
			NamedNodeMap attributes = apiI18nAlias.getAttributes();

			String name = attributes.getNamedItem("name").getNodeValue();

			boolean isCaseSensitive = attributes.getNamedItem("case-sensitive") != null;

			I18nAliasImpl defaultAlias = template.getI18nAliasById(name);
			String prefix = "";
			String postfix = "";
			boolean postOptional = false;
			if (defaultAlias != null)
			{
				for (String a : defaultAlias.getAliases())
				{
					if (a.startsWith("#"))
					{
						prefix = "#";
					}

					if (a.endsWith(":"))
					{
						postfix = ":";
					}
					else
					{
						postOptional = true;
					}
				}
			}

			Node aliasesNode = apiI18nAlias.getFirstChild();
			NodeList aliasesList = aliasesNode.getChildNodes();
			ArrayList<String> aliases = new ArrayList<String>();
			for (int j = 0; j < aliasesList.getLength(); j++)
			{
				Node aliasNode = aliasesList.item(j);
				String aliasString = aliasNode.getTextContent();
				if (prefix.length() > 0 && !aliasString.startsWith(prefix))
				{
					aliasString = prefix + aliasString;
				}
				if (postOptional || postfix.length() == 0 || aliasString.endsWith(postfix))
				{
					// Add without change to the postfix:
					aliases.add(aliasString);
				}
				if (postfix.length() > 0)
				{
					if (!aliasString.endsWith(postfix))
					{
						// Add missing postfix:
						aliases.add(aliasString + postfix);
					}
					else if (postOptional)
					{
						// Remove existing, optional postfix:
						aliases.add(aliasString.substring(0, aliasString.length() - postfix.length()));
					}
				}
			}
			I18nAliasImpl i18nAlias = new I18nAliasImpl(name, isCaseSensitive, aliases);
			try
			{
				wikiConfig.addI18nAlias(i18nAlias);
			}
			catch (IllegalArgumentException e)
			{
				// TODO resolve conflicts problem
				logger.warn(e.getMessage());
			}
		}
	}

	public static void addInterwikis(
			WikiConfigImpl wikiConfig,
			String apiUrlInterwikiMap)
		throws IOException,
			ParserConfigurationException,
			SAXException
	{
		Document document = getXMLFromUrl(apiUrlInterwikiMap);
		NodeList apiInterwikis = document.getElementsByTagName("iw");

		for (int i = 0; i < apiInterwikis.getLength(); i++)
		{
			Node apiInterWiki = apiInterwikis.item(i);
			NamedNodeMap attributes = apiInterWiki.getAttributes();

			String prefixString = attributes.getNamedItem("prefix").getNodeValue();

			boolean isLocal = false; // if present set true else false
			Node localNode = attributes.getNamedItem("local");
			if (localNode != null)
			{
				isLocal = true;
			}
			boolean isTrans = false; // TODO check dokumentation if really always false?
			String urlStringApi = attributes.getNamedItem("url").getNodeValue();

			InterwikiImpl interwiki = new InterwikiImpl(prefixString, urlStringApi, isLocal, isTrans);
			wikiConfig.addInterwiki(interwiki);
		}
	}

	public static void addNamespaces(
			WikiConfigImpl wikiConfig,
			String apiUrlNamespaces,
			MultiValueMap nameSpaceAliases)
		throws IOException,
			ParserConfigurationException,
			SAXException
	{
		Document document = getXMLFromUrl(apiUrlNamespaces);
		NodeList apiNamespaces = document.getElementsByTagName("ns");

		for (int i = 0; i < apiNamespaces.getLength(); i++)
		{
			Node apiNamespace = apiNamespaces.item(i);
			String name = apiNamespace.getTextContent();
			NamedNodeMap attributes = apiNamespace.getAttributes();
			Integer id = new Integer(attributes.getNamedItem("id").getNodeValue());
			String canonical = "";
			if (attributes.getNamedItem("canonical") != null)
			{
				canonical = attributes.getNamedItem("canonical").getNodeValue();
			}

			boolean fileNs = false;
			if (canonical.equals("File"))
			{
				fileNs = true;
			}

			Node subpages = attributes.getNamedItem("subpages");
			boolean canHaveSubpages = false;
			if (subpages != null)
			{
				canHaveSubpages = true;
			}

			Collection<String> aliases = new ArrayList<String>();
			if (nameSpaceAliases.containsKey(id))
			{
				@SuppressWarnings("unchecked")
				Collection<String> tmp = nameSpaceAliases.getCollection(id);
				aliases = tmp;
			}

			NamespaceImpl namespace = new NamespaceImpl(
					id.intValue(),
					name,
					canonical,
					canHaveSubpages,
					fileNs,
					aliases);
			wikiConfig.addNamespace(namespace);

			if (canonical.equals("Template"))
			{
				wikiConfig.setTemplateNamespace(namespace);
			}
			else if (id.intValue() == 0)
			{
				wikiConfig.setDefaultNamespace(namespace);
			}

		}
	}

	public static MultiValueMap getNamespaceAliases(
			String apiUrlNamespaceAliases)
		throws IOException,
			ParserConfigurationException,
			SAXException
	{
		Document document = getXMLFromUrl(apiUrlNamespaceAliases);
		NodeList namespaceAliasess = document.getElementsByTagName("ns");
		MultiValueMap namespaces = new MultiValueMap();

		for (int i = 0; i < namespaceAliasess.getLength(); i++)
		{
			Node aliasNode = namespaceAliasess.item(i);
			NamedNodeMap attributes = aliasNode.getAttributes();

			Integer id = new Integer(attributes.getNamedItem("id").getNodeValue());
			String aliasString = aliasNode.getTextContent();
			namespaces.put(id, aliasString);
		}
		return namespaces;
	}

	public static Document getXMLFromUrl(String urlString)
		throws IOException,
			ParserConfigurationException,
			SAXException
	{
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = docBuilder.parse(connection.getInputStream());
		return document;
	}
}
