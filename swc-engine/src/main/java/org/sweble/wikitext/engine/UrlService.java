package org.sweble.wikitext.engine;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.sweble.wikitext.engine.utils.UrlEncoding;

public class UrlService
{
	
	public URL convertUrl(UrlType full, URL titleUrl)
	{
		return titleUrl;
	}
	
	/**
	 * Takes a parameterized URL like
	 * "http://localhost/wiki/index.php?title=$1", replace the parameter "$1"
	 * with the URL-encoded normalized full title and returns the resulting URL.
	 */
	public static URL makeUrlToArticle(String parametrizedUrl, PageTitle title) throws MalformedURLException
	{
		String encodedTitle = UrlEncoding.WIKI.encode(title.getNormalizedFullTitle());
		return new URL(parametrizedUrl.replace("$1", encodedTitle));
	}
	
	/**
	 * Appends (possibly additional) query parameter to a URL.
	 * 
	 * @param urlEncodedQuery
	 *            A URL-encoded query string, e.g.: "key=value&key2=value2&...".
	 */
	public static URL appendQuery(URL url, String urlEncodedQuery) throws MalformedURLException
	{
		String ef = url.toExternalForm();
		return (ef.indexOf('?') != -1) ?
				new URL(ef + "&" + urlEncodedQuery) :
				new URL(ef + "?" + urlEncodedQuery);
	}
	
	/**
	 * Appends (possibly additional) query parameter to a URL.
	 */
	public static URL appendQuery(URL url, Map<String, String> query) throws MalformedURLException
	{
		return appendQuery(url, queryMapToString(query));
	}
	
	/**
	 * Converts a map of key=value pairs to the URL-encoded query part of an
	 * URL.
	 */
	public static String queryMapToString(Map<String, String> query)
	{
		StringBuilder b = new StringBuilder();
		UrlEncoding encoder = UrlEncoding.QUERY;
		
		boolean first = true;
		for (Entry<String, String> e : query.entrySet())
		{
			if (!first)
				b.append('&');
			b.append(encoder.encode(e.getKey()));
			b.append('&');
			b.append(encoder.encode(e.getValue()));
			first = false;
		}
		
		return b.toString();
	}
}
