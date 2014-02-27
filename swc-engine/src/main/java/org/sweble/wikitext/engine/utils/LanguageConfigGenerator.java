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






public class LanguageConfigGenerator
{

	/*public static void main(String[] args) throws IOException,
		ParserConfigurationException, SAXException, JAXBException
	{
		
		
		WikiConfigImpl wikiConfig = new WikiConfigImpl();
		
		wikiConfig.setSiteName("English Wikipedia");
		wikiConfig.setWikiUrl("http://en.wikipedia.org");
		wikiConfig.setContentLang("en");
		wikiConfig.setIwPrefix("en");
		
		addNamespaces(wikiConfig, wikiConfig.getInterwikiPrefix());
		addInterwikis(wikiConfig, wikiConfig.getInterwikiPrefix());
		addi18NAliases(wikiConfig, wikiConfig.getInterwikiPrefix());
		
		wikiConfig.save(new File("/home/samy/Workspace/swebleconfigurator/src/main/java/myConfigEN.xml"));
	

	}*/
	
	
	public static WikiConfig generateWikiConfig(String siteName, String siteURL, String languagePrefix) throws IOException, ParserConfigurationException, SAXException
	{
		WikiConfigImpl wikiConfig = new WikiConfigImpl();
		wikiConfig.setSiteName(siteName);
		wikiConfig.setWikiUrl(siteURL);
		wikiConfig.setContentLang(languagePrefix);
		wikiConfig.setIwPrefix(languagePrefix);
		
		addNamespaces(wikiConfig, wikiConfig.getInterwikiPrefix());
		addInterwikis(wikiConfig, wikiConfig.getInterwikiPrefix());
		addi18NAliases(wikiConfig, wikiConfig.getInterwikiPrefix());
		
		return wikiConfig;
	}
	
	public static void addi18NAliases(WikiConfigImpl wikiConfig, String iwPrefix) throws IOException, ParserConfigurationException, SAXException
	{
		String urlString = "http://"
			+ iwPrefix
			+ ".wikipedia.org/w/api.php?action=query&meta=siteinfo&siprop=magicwords&format=xml";
		Document document = getXMLFromUlr(urlString);
		NodeList apiI18NAliases = document.getElementsByTagName("magicword");
		
		for(int i=0; i< apiI18NAliases.getLength(); i++)
		{
			Node apii18NAlias = apiI18NAliases.item(i);
			NamedNodeMap attributes = apii18NAlias.getAttributes();
			
			String name = attributes.getNamedItem("name").getNodeValue();
			System.out.println("Magic word: "+name);
			
			boolean iscaseSensitive = false;
			Node caseSensitive = attributes.getNamedItem("case-sensitive");
			if(caseSensitive != null)
			{
				iscaseSensitive = true;
			}
			System.out.println("cas sensitive: "+ iscaseSensitive);
			
			Node aliasesNode = apii18NAlias.getFirstChild();
			NodeList aliasesList = aliasesNode.getChildNodes();
			ArrayList<String> aliases = new ArrayList<String>();
			for(int j = 0; j< aliasesList.getLength(); j++)
			{
				Node aliasNode = aliasesList.item(j);
				String aliasString = aliasNode.getTextContent();
				System.out.println("alias: "+aliasString);
				aliases.add(aliasString);
			}
			System.out.println("");
			I18nAliasImpl I18Alias = new I18nAliasImpl(name, iscaseSensitive, aliases);
			try{
				wikiConfig.addI18nAlias(I18Alias);
			}catch(Exception e)
			{
				//TODO resolve conflicts problem
				e.printStackTrace();
			}
		}
	}
	
	public static void addInterwikis(WikiConfigImpl wikiConfig, String iwPrefix) throws IOException, ParserConfigurationException, SAXException
	{
		String urlString = "http://"
			+ iwPrefix
			+ ".wikipedia.org/w/api.php?action=query&meta=siteinfo&siprop=interwikimap&format=xml";
		Document document = getXMLFromUlr(urlString);
		NodeList apiInterwikis = document.getElementsByTagName("iw");
		
		
		for(int i=0; i< apiInterwikis.getLength(); i++)
		{
			Node apiInterWiki = apiInterwikis.item(i);
			NamedNodeMap attributes = apiInterWiki.getAttributes();
			
			String prefixString = attributes.getNamedItem("prefix").getNodeValue(); //same as prefix in api
			boolean isLocal = false; //if present set true else false
			Node localNode = attributes.getNamedItem("local");
			if(localNode != null)
			{
				isLocal = true;
			}
			boolean isTrans = false; //TODO always false?
			String urlStringApi = attributes.getNamedItem("url").getNodeValue();//same as url in api
			
			InterwikiImpl interwiki = new InterwikiImpl(prefixString,urlStringApi,isLocal,isTrans);
			wikiConfig.addInterwiki(interwiki);
		}
	}

	public static void addNamespaces(WikiConfigImpl wikiConfig, String iwPrefix) throws IOException,
		ParserConfigurationException, SAXException
	{
		String urlString = "http://"
			+ iwPrefix
			+ ".wikipedia.org/w/api.php?action=query&meta=siteinfo&siprop=namespaces&format=xml";
		Document document = getXMLFromUlr(urlString);
		NodeList apiNamespaces = document.getElementsByTagName("ns");
		MultiValueMap namespaceAliases = getNamespaceAliases(iwPrefix);
				
		for (int i = 0; i < apiNamespaces.getLength(); i++)
		{
			Node apiNamespace = apiNamespaces.item(i);
			String name = apiNamespace.getTextContent();
			System.out.println(name);
			NamedNodeMap attributes = apiNamespace.getAttributes();
			Integer id = new Integer(attributes.getNamedItem("id").getNodeValue());
			System.out.println(id);
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

			System.out.println(canonical);
			Node subpages = attributes.getNamedItem("subpages");
			boolean canHaveSubpages = false;
			if (subpages != null)
			{
				canHaveSubpages = true;
			}
			System.out.println(canHaveSubpages);
			
			Collection<String> aliases = new ArrayList<String>();
			if(namespaceAliases.containsKey(id))
			{
				aliases = (Collection<String>) namespaceAliases.getCollection(id);	
			}
			
			NamespaceImpl namespace = new NamespaceImpl(id.intValue(), name, canonical, canHaveSubpages, fileNs, aliases);
			wikiConfig.addNamespace(namespace);

			
			if (canonical.equals("Template"))
			{
				wikiConfig.setTemplateNamespace(namespace);
			}else if(id.intValue() == 0)
			{
				wikiConfig.setDefaultNamespace(namespace);
			}

		}
	}
	
	public static MultiValueMap getNamespaceAliases(String iwPrefix) throws IOException, ParserConfigurationException, SAXException
	{
		String urlString = "http://"
			+ iwPrefix
			+ ".wikipedia.org/w/api.php?action=query&meta=siteinfo&siprop=namespacealiases&format=xml";
		
		Document document = getXMLFromUlr(urlString);
		NodeList namespaceAliasess = document.getElementsByTagName("ns");
		MultiValueMap namespaces = new MultiValueMap();
		
		for(int i = 0; i< namespaceAliasess.getLength(); i++)
		{
			Node aliasNode = namespaceAliasess.item(i);
			NamedNodeMap attributes = aliasNode.getAttributes();
			
			Integer id = new Integer(attributes.getNamedItem("id").getNodeValue());
			String aliasString = aliasNode.getTextContent();
			System.out.println("aliasId: "+id);
			namespaces.put(id, aliasString);
		}
		return namespaces;
	}
	
	public static Document getXMLFromUlr(String urlString) throws IOException, ParserConfigurationException, SAXException
	{
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
			.newInstance();
		DocumentBuilder docBuilder = documentBuilderFactory
			.newDocumentBuilder();
		Document document = docBuilder.parse(connection.getInputStream());
		return document;
	}

}
