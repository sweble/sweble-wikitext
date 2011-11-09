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

package org.sweble.wikitext.engine.utils;

import java.util.HashMap;
import java.util.Map;

public class EntityReferences
{
	private static final Map<String, String> entities =
			new HashMap<String, String>();
	
	private static final Map<String, String> entityAliases =
			new HashMap<String, String>();
	
	// =========================================================================
	
	static
	{
		// From: http://www.w3.org/TR/html4/sgml/entities.html
		
		// 24 Character entity references in HTML 4
		
		// 24.2 Character entity references for ISO 8859-1 characters
		add("nbsp", "\u00a0");
		add("iexcl", "\u00a1");
		add("cent", "\u00a2");
		add("pound", "\u00a3");
		add("curren", "\u00a4");
		add("yen", "\u00a5");
		add("brvbar", "\u00a6");
		add("sect", "\u00a7");
		add("uml", "\u00a8");
		add("copy", "\u00a9");
		add("ordf", "\u00aa");
		add("laquo", "\u00ab");
		add("not", "\u00ac");
		add("shy", "\u00ad");
		add("reg", "\u00ae");
		add("macr", "\u00af");
		add("deg", "\u00b0");
		add("plusmn", "\u00b1");
		add("sup2", "\u00b2");
		add("sup3", "\u00b3");
		add("acute", "\u00b4");
		add("micro", "\u00b5");
		add("para", "\u00b6");
		add("middot", "\u00b7");
		add("cedil", "\u00b8");
		add("sup1", "\u00b9");
		add("ordm", "\u00ba");
		add("raquo", "\u00bb");
		add("frac14", "\u00bc");
		add("frac12", "\u00bd");
		add("frac34", "\u00be");
		add("iquest", "\u00bf");
		add("Agrave", "\u00c0");
		add("Aacute", "\u00c1");
		add("Acirc", "\u00c2");
		add("Atilde", "\u00c3");
		add("Auml", "\u00c4");
		add("Aring", "\u00c5");
		add("AElig", "\u00c6");
		add("Ccedil", "\u00c7");
		add("Egrave", "\u00c8");
		add("Eacute", "\u00c9");
		add("Ecirc", "\u00ca");
		add("Euml", "\u00cb");
		add("Igrave", "\u00cc");
		add("Iacute", "\u00cd");
		add("Icirc", "\u00ce");
		add("Iuml", "\u00cf");
		add("ETH", "\u00d0");
		add("Ntilde", "\u00d1");
		add("Ograve", "\u00d2");
		add("Oacute", "\u00d3");
		add("Ocirc", "\u00d4");
		add("Otilde", "\u00d5");
		add("Ouml", "\u00d6");
		add("times", "\u00d7");
		add("Oslash", "\u00d8");
		add("Ugrave", "\u00d9");
		add("Uacute", "\u00da");
		add("Ucirc", "\u00db");
		add("Uuml", "\u00dc");
		add("Yacute", "\u00dd");
		add("THORN", "\u00de");
		add("szlig", "\u00df");
		add("agrave", "\u00e0");
		add("aacute", "\u00e1");
		add("acirc", "\u00e2");
		add("atilde", "\u00e3");
		add("auml", "\u00e4");
		add("aring", "\u00e5");
		add("aelig", "\u00e6");
		add("ccedil", "\u00e7");
		add("egrave", "\u00e8");
		add("eacute", "\u00e9");
		add("ecirc", "\u00ea");
		add("euml", "\u00eb");
		add("igrave", "\u00ec");
		add("iacute", "\u00ed");
		add("icirc", "\u00ee");
		add("iuml", "\u00ef");
		add("eth", "\u00f0");
		add("ntilde", "\u00f1");
		add("ograve", "\u00f2");
		add("oacute", "\u00f3");
		add("ocirc", "\u00f4");
		add("otilde", "\u00f5");
		add("ouml", "\u00f6");
		add("divide", "\u00f7");
		add("oslash", "\u00f8");
		add("ugrave", "\u00f9");
		add("uacute", "\u00fa");
		add("ucirc", "\u00fb");
		add("uuml", "\u00fc");
		add("yacute", "\u00fd");
		add("thorn", "\u00fe");
		add("yuml", "\u00ff");
		
		// 24.3 Character entity references for symbols, mathematical symbols, and Greek letters
		add("fnof", "\u0192");
		add("Alpha", "\u0391");
		add("Beta", "\u0392");
		add("Gamma", "\u0393");
		add("Delta", "\u0394");
		add("Epsilon", "\u0395");
		add("Zeta", "\u0396");
		add("Eta", "\u0397");
		add("Theta", "\u0398");
		add("Iota", "\u0399");
		add("Kappa", "\u039a");
		add("Lambda", "\u039b");
		add("Mu", "\u039c");
		add("Nu", "\u039d");
		add("Xi", "\u039e");
		add("Omicron", "\u039f");
		add("Pi", "\u03a0");
		add("Rho", "\u03a1");
		add("Sigma", "\u03a3");
		add("Tau", "\u03a4");
		add("Upsilon", "\u03a5");
		add("Phi", "\u03a6");
		add("Chi", "\u03a7");
		add("Psi", "\u03a8");
		add("Omega", "\u03a9");
		add("alpha", "\u03b1");
		add("beta", "\u03b2");
		add("gamma", "\u03b3");
		add("delta", "\u03b4");
		add("epsilon", "\u03b5");
		add("zeta", "\u03b6");
		add("eta", "\u03b7");
		add("theta", "\u03b8");
		add("iota", "\u03b9");
		add("kappa", "\u03ba");
		add("lambda", "\u03bb");
		add("mu", "\u03bc");
		add("nu", "\u03bd");
		add("xi", "\u03be");
		add("omicron", "\u03bf");
		add("pi", "\u03c0");
		add("rho", "\u03c1");
		add("sigmaf", "\u03c2");
		add("sigma", "\u03c3");
		add("tau", "\u03c4");
		add("upsilon", "\u03c5");
		add("phi", "\u03c6");
		add("chi", "\u03c7");
		add("psi", "\u03c8");
		add("omega", "\u03c9");
		add("thetasym", "\u03d1");
		add("upsih", "\u03d2");
		add("piv", "\u03d6");
		add("bull", "\u2022");
		add("hellip", "\u2026");
		add("prime", "\u2032");
		add("Prime", "\u2033");
		add("oline", "\u203e");
		add("frasl", "\u2044");
		add("weierp", "\u2118");
		add("image", "\u2111");
		add("real", "\u211c");
		add("trade", "\u2122");
		add("alefsym", "\u2135");
		add("larr", "\u2190");
		add("uarr", "\u2191");
		add("rarr", "\u2192");
		add("darr", "\u2193");
		add("harr", "\u2194");
		add("crarr", "\u21b5");
		add("lArr", "\u21d0");
		add("uArr", "\u21d1");
		add("rArr", "\u21d2");
		add("dArr", "\u21d3");
		add("hArr", "\u21d4");
		add("forall", "\u2200");
		add("part", "\u2202");
		add("exist", "\u2203");
		add("empty", "\u2205");
		add("nabla", "\u2207");
		add("isin", "\u2208");
		add("notin", "\u2209");
		add("ni", "\u220b");
		add("prod", "\u220f");
		add("sum", "\u2211");
		add("minus", "\u2212");
		add("lowast", "\u2217");
		add("radic", "\u221a");
		add("prop", "\u221d");
		add("infin", "\u221e");
		add("ang", "\u2220");
		add("and", "\u2227");
		add("or", "\u2228");
		add("cap", "\u2229");
		add("cup", "\u222a");
		add("int", "\u222b");
		add("there4", "\u2234");
		add("sim", "\u223c");
		add("cong", "\u2245");
		add("asymp", "\u2248");
		add("ne", "\u2260");
		add("equiv", "\u2261");
		add("le", "\u2264");
		add("ge", "\u2265");
		add("sub", "\u2282");
		add("sup", "\u2283");
		add("nsub", "\u2284");
		add("sube", "\u2286");
		add("supe", "\u2287");
		add("oplus", "\u2295");
		add("otimes", "\u2297");
		add("perp", "\u22a5");
		add("sdot", "\u22c5");
		add("lceil", "\u2308");
		add("rceil", "\u2309");
		add("lfloor", "\u230a");
		add("rfloor", "\u230b");
		add("lang", "\u2329");
		add("rang", "\u232a");
		add("loz", "\u25ca");
		add("spades", "\u2660");
		add("clubs", "\u2663");
		add("hearts", "\u2665");
		add("diams", "\u2666");
		
		// 24.4 Character entity references for markup-significant and internationalization characters
		add("quot", "" + '\u0022'); // Eclipse gets really confused!
		add("amp", "\u0026");
		add("lt", "\u003c");
		add("gt", "\u003e");
		add("OElig", "\u0152");
		add("oelig", "\u0153");
		add("Scaron", "\u0160");
		add("scaron", "\u0161");
		add("Yuml", "\u0178");
		add("circ", "\u02c6");
		add("tilde", "\u02dc");
		add("ensp", "\u2002");
		add("emsp", "\u2003");
		add("thinsp", "\u2009");
		add("zwnj", "\u200c");
		add("zwj", "\u200d");
		add("lrm", "\u200e");
		add("rlm", "\u200f");
		add("ndash", "\u2013");
		add("mdash", "\u2014");
		add("lsquo", "\u2018");
		add("rsquo", "\u2019");
		add("sbquo", "\u201a");
		add("ldquo", "\u201c");
		add("rdquo", "\u201d");
		add("bdquo", "\u201e");
		add("dagger", "\u2020");
		add("Dagger", "\u2021");
		add("permil", "\u2030");
		add("lsaquo", "\u2039");
		add("rsaquo", "\u203a");
		add("euro", "\u20ac");
	}
	
	// =========================================================================
	
	public static void add(String entityName, String replacement)
	{
		entities.put(entityName, replacement);
	}
	
	public static void addAlias(String alias, String entityName)
	{
		entityAliases.put(alias, entityName);
	}
	
	public static boolean isDeclared(String entityName)
	{
		return entities.containsKey(entityName);
	}
	
	public static boolean isAliasDeclared(String alias)
	{
		return entityAliases.containsKey(alias);
	}
	
	public static Map<String, String> getEntities()
	{
		return entities;
	}
	
	/**
	 * @deprecated Use getEntityAliases() instead!
	 */
	public static Map<String, String> getEntityaliases()
	{
		return EntityReferences.getEntityAliases();
	}
	
	public static Map<String, String> getEntityAliases()
	{
		return entityAliases;
	}
	
	public static String resolve(String entityName)
	{
		String tmp = entityAliases.get(entityName);
		if (tmp != null)
			entityName = tmp;
		
		return entities.get(entityName);
	}
}
