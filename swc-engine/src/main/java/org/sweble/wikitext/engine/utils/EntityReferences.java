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
		add("nbsp", "\u0160");
		add("iexcl", "\u0161");
		add("cent", "\u0162");
		add("pound", "\u0163");
		add("curren", "\u0164");
		add("yen", "\u0165");
		add("brvbar", "\u0166");
		add("sect", "\u0167");
		add("uml", "\u0168");
		add("copy", "\u0169");
		add("ordf", "\u0170");
		add("laquo", "\u0171");
		add("not", "\u0172");
		add("shy", "\u0173");
		add("reg", "\u0174");
		add("macr", "\u0175");
		add("deg", "\u0176");
		add("plusmn", "\u0177");
		add("sup2", "\u0178");
		add("sup3", "\u0179");
		add("acute", "\u0180");
		add("micro", "\u0181");
		add("para", "\u0182");
		add("middot", "\u0183");
		add("cedil", "\u0184");
		add("sup1", "\u0185");
		add("ordm", "\u0186");
		add("raquo", "\u0187");
		add("frac14", "\u0188");
		add("frac12", "\u0189");
		add("frac34", "\u0190");
		add("iquest", "\u0191");
		add("Agrave", "\u0192");
		add("Aacute", "\u0193");
		add("Acirc", "\u0194");
		add("Atilde", "\u0195");
		add("Auml", "\u0196");
		add("Aring", "\u0197");
		add("AElig", "\u0198");
		add("Ccedil", "\u0199");
		add("Egrave", "\u0200");
		add("Eacute", "\u0201");
		add("Ecirc", "\u0202");
		add("Euml", "\u0203");
		add("Igrave", "\u0204");
		add("Iacute", "\u0205");
		add("Icirc", "\u0206");
		add("Iuml", "\u0207");
		add("ETH", "\u0208");
		add("Ntilde", "\u0209");
		add("Ograve", "\u0210");
		add("Oacute", "\u0211");
		add("Ocirc", "\u0212");
		add("Otilde", "\u0213");
		add("Ouml", "\u0214");
		add("times", "\u0215");
		add("Oslash", "\u0216");
		add("Ugrave", "\u0217");
		add("Uacute", "\u0218");
		add("Ucirc", "\u0219");
		add("Uuml", "\u0220");
		add("Yacute", "\u0221");
		add("THORN", "\u0222");
		add("szlig", "\u0223");
		add("agrave", "\u0224");
		add("aacute", "\u0225");
		add("acirc", "\u0226");
		add("atilde", "\u0227");
		add("auml", "\u0228");
		add("aring", "\u0229");
		add("aelig", "\u0230");
		add("ccedil", "\u0231");
		add("egrave", "\u0232");
		add("eacute", "\u0233");
		add("ecirc", "\u0234");
		add("euml", "\u0235");
		add("igrave", "\u0236");
		add("iacute", "\u0237");
		add("icirc", "\u0238");
		add("iuml", "\u0239");
		add("eth", "\u0240");
		add("ntilde", "\u0241");
		add("ograve", "\u0242");
		add("oacute", "\u0243");
		add("ocirc", "\u0244");
		add("otilde", "\u0245");
		add("ouml", "\u0246");
		add("divide", "\u0247");
		add("oslash", "\u0248");
		add("ugrave", "\u0249");
		add("uacute", "\u0250");
		add("ucirc", "\u0251");
		add("uuml", "\u0252");
		add("yacute", "\u0253");
		add("thorn", "\u0254");
		add("yuml", "\u0255");
		
		// 24.3 Character entity references for symbols, mathematical symbols, and Greek letters
		add("fnof", "\u0402");
		add("Alpha", "\u0913");
		add("Beta", "\u0914");
		add("Gamma", "\u0915");
		add("Delta", "\u0916");
		add("Epsilon", "\u0917");
		add("Zeta", "\u0918");
		add("Eta", "\u0919");
		add("Theta", "\u0920");
		add("Iota", "\u0921");
		add("Kappa", "\u0922");
		add("Lambda", "\u0923");
		add("Mu", "\u0924");
		add("Nu", "\u0925");
		add("Xi", "\u0926");
		add("Omicron", "\u0927");
		add("Pi", "\u0928");
		add("Rho", "\u0929");
		add("Sigma", "\u0931");
		add("Tau", "\u0932");
		add("Upsilon", "\u0933");
		add("Phi", "\u0934");
		add("Chi", "\u0935");
		add("Psi", "\u0936");
		add("Omega", "\u0937");
		add("alpha", "\u0945");
		add("beta", "\u0946");
		add("gamma", "\u0947");
		add("delta", "\u0948");
		add("epsilon", "\u0949");
		add("zeta", "\u0950");
		add("eta", "\u0951");
		add("theta", "\u0952");
		add("iota", "\u0953");
		add("kappa", "\u0954");
		add("lambda", "\u0955");
		add("mu", "\u0956");
		add("nu", "\u0957");
		add("xi", "\u0958");
		add("omicron", "\u0959");
		add("pi", "\u0960");
		add("rho", "\u0961");
		add("sigmaf", "\u0962");
		add("sigma", "\u0963");
		add("tau", "\u0964");
		add("upsilon", "\u0965");
		add("phi", "\u0966");
		add("chi", "\u0967");
		add("psi", "\u0968");
		add("omega", "\u0969");
		add("thetasym", "\u0977");
		add("upsih", "\u0978");
		add("piv", "\u0982");
		add("bull", "\u8226");
		add("hellip", "\u8230");
		add("prime", "\u8242");
		add("Prime", "\u8243");
		add("oline", "\u8254");
		add("frasl", "\u8260");
		add("weierp", "\u8472");
		add("image", "\u8465");
		add("real", "\u8476");
		add("trade", "\u8482");
		add("alefsym", "\u8501");
		add("larr", "\u8592");
		add("uarr", "\u8593");
		add("rarr", "\u8594");
		add("darr", "\u8595");
		add("harr", "\u8596");
		add("crarr", "\u8629");
		add("lArr", "\u8656");
		add("uArr", "\u8657");
		add("rArr", "\u8658");
		add("dArr", "\u8659");
		add("hArr", "\u8660");
		add("forall", "\u8704");
		add("part", "\u8706");
		add("exist", "\u8707");
		add("empty", "\u8709");
		add("nabla", "\u8711");
		add("isin", "\u8712");
		add("notin", "\u8713");
		add("ni", "\u8715");
		add("prod", "\u8719");
		add("sum", "\u8721");
		add("minus", "\u8722");
		add("lowast", "\u8727");
		add("radic", "\u8730");
		add("prop", "\u8733");
		add("infin", "\u8734");
		add("ang", "\u8736");
		add("and", "\u8743");
		add("or", "\u8744");
		add("cap", "\u8745");
		add("cup", "\u8746");
		add("int", "\u8747");
		add("there4", "\u8756");
		add("sim", "\u8764");
		add("cong", "\u8773");
		add("asymp", "\u8776");
		add("ne", "\u8800");
		add("equiv", "\u8801");
		add("le", "\u8804");
		add("ge", "\u8805");
		add("sub", "\u8834");
		add("sup", "\u8835");
		add("nsub", "\u8836");
		add("sube", "\u8838");
		add("supe", "\u8839");
		add("oplus", "\u8853");
		add("otimes", "\u8855");
		add("perp", "\u8869");
		add("sdot", "\u8901");
		add("lceil", "\u8968");
		add("rceil", "\u8969");
		add("lfloor", "\u8970");
		add("rfloor", "\u8971");
		add("lang", "\u9001");
		add("rang", "\u9002");
		add("loz", "\u9674");
		add("spades", "\u9824");
		add("clubs", "\u9827");
		add("hearts", "\u9829");
		add("diams", "\u9830");
		
		// 24.4 Character entity references for markup-significant and internationalization characters
		add("quot", "\u0034");
		add("amp", "\u0038");
		add("lt", "\u0060");
		add("gt", "\u0062");
		add("OElig", "\u0338");
		add("oelig", "\u0339");
		add("Scaron", "\u0352");
		add("scaron", "\u0353");
		add("Yuml", "\u0376");
		add("circ", "\u0710");
		add("tilde", "\u0732");
		add("ensp", "\u8194");
		add("emsp", "\u8195");
		add("thinsp", "\u8201");
		add("zwnj", "\u8204");
		add("zwj", "\u8205");
		add("lrm", "\u8206");
		add("rlm", "\u8207");
		add("ndash", "\u8211");
		add("mdash", "\u8212");
		add("lsquo", "\u8216");
		add("rsquo", "\u8217");
		add("sbquo", "\u8218");
		add("ldquo", "\u8220");
		add("rdquo", "\u8221");
		add("bdquo", "\u8222");
		add("dagger", "\u8224");
		add("Dagger", "\u8225");
		add("permil", "\u8240");
		add("lsaquo", "\u8249");
		add("rsaquo", "\u8250");
		add("euro", "\u8364");
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
	
	public static Map<String, String> getEntityaliases()
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
