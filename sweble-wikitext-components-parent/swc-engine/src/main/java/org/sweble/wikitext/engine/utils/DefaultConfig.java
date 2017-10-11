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

import java.util.ArrayList;
import java.util.Arrays;

import org.sweble.wikitext.engine.config.EngineConfigImpl;
import org.sweble.wikitext.engine.config.NamespaceImpl;
import org.sweble.wikitext.engine.config.ParserConfigImpl;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.ext.builtin.BuiltInParserFunctions;
import org.sweble.wikitext.engine.ext.builtin.BuiltInTagExtensions;
import org.sweble.wikitext.engine.ext.core.CorePfnBehaviorSwitches;
import org.sweble.wikitext.engine.ext.core.CorePfnFunctionsFormatting;
import org.sweble.wikitext.engine.ext.core.CorePfnFunctionsLocalization;
import org.sweble.wikitext.engine.ext.core.CorePfnFunctionsMiscellaneous;
import org.sweble.wikitext.engine.ext.core.CorePfnFunctionsNamespaces;
import org.sweble.wikitext.engine.ext.core.CorePfnFunctionsUrlData;
import org.sweble.wikitext.engine.ext.core.CorePfnVariablesDateAndTime;
import org.sweble.wikitext.engine.ext.core.CorePfnVariablesNamespaces;
import org.sweble.wikitext.engine.ext.core.CorePfnVariablesPageNames;
import org.sweble.wikitext.engine.ext.core.CorePfnVariablesStatistics;
import org.sweble.wikitext.engine.ext.core.CorePfnVariablesTechnicalMetadata;
import org.sweble.wikitext.engine.ext.math.MathTagExt;
import org.sweble.wikitext.engine.ext.parser_functions.ParserFunctionsPfnExt;
import org.sweble.wikitext.engine.ext.ref.RefTagExt;
import org.sweble.wikitext.parser.WikitextWarning.WarningSeverity;

/**
 * Programmatically generate a default configuration for a Wiki.
 */
public class DefaultConfig
{
	public static WikiConfigImpl generate()
	{
		WikiConfigImpl c = new WikiConfigImpl();
		new DefaultConfig().configureWiki(c);
		return c;
	}

	protected void configureWiki(WikiConfigImpl c)
	{
		configureEngine(c);

		// --[ Properties of the wiki instance ]--

		configureSiteProperties(c);

		// --[ Namespaces, Known Wikis, Internationalization ]--

		addNamespaces(c);

		addInterwikis(c);

		addI18nAliases(c);

		// --[ Parser functions ]--

		addParserFunctions(c);

		// --[ Tag extensions ]--

		addTagExtensions(c);
	}

	protected EngineConfigImpl configureEngine(WikiConfigImpl c)
	{
		configureParser(c);

		EngineConfigImpl cc = c.getEngineConfig();

		cc.setTrimTransparentBeforeParsing(true);

		return cc;
	}

	protected ParserConfigImpl configureParser(WikiConfigImpl c)
	{
		ParserConfigImpl pc = c.getParserConfig();

		// ==[ Parser features ]================================================

		pc.setAutoCorrect(false);
		pc.setGatherRtData(true);
		pc.setMinSeverity(WarningSeverity.INFORMATIVE);
		pc.setWarningsEnabled(true);

		// --[ Link classification and parsing ]--

		pc.addUrlProtocol("http://");
		pc.addUrlProtocol("https://");
		pc.addUrlProtocol("mailto:");

		pc.setInternalLinkPrefixPattern(null);
		pc.setInternalLinkPostfixPattern(null);

		// ==[ Parsing XML elements ]===========================================

		addXmlEntities(pc);

		// ==[ Language Conversion Tags ]=======================================

		addLctMappings(pc);

		return pc;
	}

	// =========================================================================

	protected void addXmlEntities(ParserConfigImpl pc)
	{
		// From: http://www.w3.org/TR/html4/sgml/entities.html

		// 24 Character entity references in HTML 4

		// 24.2 Character entity references for ISO 8859-1 characters
		pc.addXmlEntity("nbsp", "\u00a0");
		pc.addXmlEntity("iexcl", "\u00a1");
		pc.addXmlEntity("cent", "\u00a2");
		pc.addXmlEntity("pound", "\u00a3");
		pc.addXmlEntity("curren", "\u00a4");
		pc.addXmlEntity("yen", "\u00a5");
		pc.addXmlEntity("brvbar", "\u00a6");
		pc.addXmlEntity("sect", "\u00a7");
		pc.addXmlEntity("uml", "\u00a8");
		pc.addXmlEntity("copy", "\u00a9");
		pc.addXmlEntity("ordf", "\u00aa");
		pc.addXmlEntity("laquo", "\u00ab");
		pc.addXmlEntity("not", "\u00ac");
		pc.addXmlEntity("shy", "\u00ad");
		pc.addXmlEntity("reg", "\u00ae");
		pc.addXmlEntity("macr", "\u00af");
		pc.addXmlEntity("deg", "\u00b0");
		pc.addXmlEntity("plusmn", "\u00b1");
		pc.addXmlEntity("sup2", "\u00b2");
		pc.addXmlEntity("sup3", "\u00b3");
		pc.addXmlEntity("acute", "\u00b4");
		pc.addXmlEntity("micro", "\u00b5");
		pc.addXmlEntity("para", "\u00b6");
		pc.addXmlEntity("middot", "\u00b7");
		pc.addXmlEntity("cedil", "\u00b8");
		pc.addXmlEntity("sup1", "\u00b9");
		pc.addXmlEntity("ordm", "\u00ba");
		pc.addXmlEntity("raquo", "\u00bb");
		pc.addXmlEntity("frac14", "\u00bc");
		pc.addXmlEntity("frac12", "\u00bd");
		pc.addXmlEntity("frac34", "\u00be");
		pc.addXmlEntity("iquest", "\u00bf");
		pc.addXmlEntity("Agrave", "\u00c0");
		pc.addXmlEntity("Aacute", "\u00c1");
		pc.addXmlEntity("Acirc", "\u00c2");
		pc.addXmlEntity("Atilde", "\u00c3");
		pc.addXmlEntity("Auml", "\u00c4");
		pc.addXmlEntity("Aring", "\u00c5");
		pc.addXmlEntity("AElig", "\u00c6");
		pc.addXmlEntity("Ccedil", "\u00c7");
		pc.addXmlEntity("Egrave", "\u00c8");
		pc.addXmlEntity("Eacute", "\u00c9");
		pc.addXmlEntity("Ecirc", "\u00ca");
		pc.addXmlEntity("Euml", "\u00cb");
		pc.addXmlEntity("Igrave", "\u00cc");
		pc.addXmlEntity("Iacute", "\u00cd");
		pc.addXmlEntity("Icirc", "\u00ce");
		pc.addXmlEntity("Iuml", "\u00cf");
		pc.addXmlEntity("ETH", "\u00d0");
		pc.addXmlEntity("Ntilde", "\u00d1");
		pc.addXmlEntity("Ograve", "\u00d2");
		pc.addXmlEntity("Oacute", "\u00d3");
		pc.addXmlEntity("Ocirc", "\u00d4");
		pc.addXmlEntity("Otilde", "\u00d5");
		pc.addXmlEntity("Ouml", "\u00d6");
		pc.addXmlEntity("times", "\u00d7");
		pc.addXmlEntity("Oslash", "\u00d8");
		pc.addXmlEntity("Ugrave", "\u00d9");
		pc.addXmlEntity("Uacute", "\u00da");
		pc.addXmlEntity("Ucirc", "\u00db");
		pc.addXmlEntity("Uuml", "\u00dc");
		pc.addXmlEntity("Yacute", "\u00dd");
		pc.addXmlEntity("THORN", "\u00de");
		pc.addXmlEntity("szlig", "\u00df");
		pc.addXmlEntity("agrave", "\u00e0");
		pc.addXmlEntity("aacute", "\u00e1");
		pc.addXmlEntity("acirc", "\u00e2");
		pc.addXmlEntity("atilde", "\u00e3");
		pc.addXmlEntity("auml", "\u00e4");
		pc.addXmlEntity("aring", "\u00e5");
		pc.addXmlEntity("aelig", "\u00e6");
		pc.addXmlEntity("ccedil", "\u00e7");
		pc.addXmlEntity("egrave", "\u00e8");
		pc.addXmlEntity("eacute", "\u00e9");
		pc.addXmlEntity("ecirc", "\u00ea");
		pc.addXmlEntity("euml", "\u00eb");
		pc.addXmlEntity("igrave", "\u00ec");
		pc.addXmlEntity("iacute", "\u00ed");
		pc.addXmlEntity("icirc", "\u00ee");
		pc.addXmlEntity("iuml", "\u00ef");
		pc.addXmlEntity("eth", "\u00f0");
		pc.addXmlEntity("ntilde", "\u00f1");
		pc.addXmlEntity("ograve", "\u00f2");
		pc.addXmlEntity("oacute", "\u00f3");
		pc.addXmlEntity("ocirc", "\u00f4");
		pc.addXmlEntity("otilde", "\u00f5");
		pc.addXmlEntity("ouml", "\u00f6");
		pc.addXmlEntity("divide", "\u00f7");
		pc.addXmlEntity("oslash", "\u00f8");
		pc.addXmlEntity("ugrave", "\u00f9");
		pc.addXmlEntity("uacute", "\u00fa");
		pc.addXmlEntity("ucirc", "\u00fb");
		pc.addXmlEntity("uuml", "\u00fc");
		pc.addXmlEntity("yacute", "\u00fd");
		pc.addXmlEntity("thorn", "\u00fe");
		pc.addXmlEntity("yuml", "\u00ff");

		// 24.3 Character entity references for symbols, mathematical symbols, and Greek letters
		pc.addXmlEntity("fnof", "\u0192");
		pc.addXmlEntity("Alpha", "\u0391");
		pc.addXmlEntity("Beta", "\u0392");
		pc.addXmlEntity("Gamma", "\u0393");
		pc.addXmlEntity("Delta", "\u0394");
		pc.addXmlEntity("Epsilon", "\u0395");
		pc.addXmlEntity("Zeta", "\u0396");
		pc.addXmlEntity("Eta", "\u0397");
		pc.addXmlEntity("Theta", "\u0398");
		pc.addXmlEntity("Iota", "\u0399");
		pc.addXmlEntity("Kappa", "\u039a");
		pc.addXmlEntity("Lambda", "\u039b");
		pc.addXmlEntity("Mu", "\u039c");
		pc.addXmlEntity("Nu", "\u039d");
		pc.addXmlEntity("Xi", "\u039e");
		pc.addXmlEntity("Omicron", "\u039f");
		pc.addXmlEntity("Pi", "\u03a0");
		pc.addXmlEntity("Rho", "\u03a1");
		pc.addXmlEntity("Sigma", "\u03a3");
		pc.addXmlEntity("Tau", "\u03a4");
		pc.addXmlEntity("Upsilon", "\u03a5");
		pc.addXmlEntity("Phi", "\u03a6");
		pc.addXmlEntity("Chi", "\u03a7");
		pc.addXmlEntity("Psi", "\u03a8");
		pc.addXmlEntity("Omega", "\u03a9");
		pc.addXmlEntity("alpha", "\u03b1");
		pc.addXmlEntity("beta", "\u03b2");
		pc.addXmlEntity("gamma", "\u03b3");
		pc.addXmlEntity("delta", "\u03b4");
		pc.addXmlEntity("epsilon", "\u03b5");
		pc.addXmlEntity("zeta", "\u03b6");
		pc.addXmlEntity("eta", "\u03b7");
		pc.addXmlEntity("theta", "\u03b8");
		pc.addXmlEntity("iota", "\u03b9");
		pc.addXmlEntity("kappa", "\u03ba");
		pc.addXmlEntity("lambda", "\u03bb");
		pc.addXmlEntity("mu", "\u03bc");
		pc.addXmlEntity("nu", "\u03bd");
		pc.addXmlEntity("xi", "\u03be");
		pc.addXmlEntity("omicron", "\u03bf");
		pc.addXmlEntity("pi", "\u03c0");
		pc.addXmlEntity("rho", "\u03c1");
		pc.addXmlEntity("sigmaf", "\u03c2");
		pc.addXmlEntity("sigma", "\u03c3");
		pc.addXmlEntity("tau", "\u03c4");
		pc.addXmlEntity("upsilon", "\u03c5");
		pc.addXmlEntity("phi", "\u03c6");
		pc.addXmlEntity("chi", "\u03c7");
		pc.addXmlEntity("psi", "\u03c8");
		pc.addXmlEntity("omega", "\u03c9");
		pc.addXmlEntity("thetasym", "\u03d1");
		pc.addXmlEntity("upsih", "\u03d2");
		pc.addXmlEntity("piv", "\u03d6");
		pc.addXmlEntity("bull", "\u2022");
		pc.addXmlEntity("hellip", "\u2026");
		pc.addXmlEntity("prime", "\u2032");
		pc.addXmlEntity("Prime", "\u2033");
		pc.addXmlEntity("oline", "\u203e");
		pc.addXmlEntity("frasl", "\u2044");
		pc.addXmlEntity("weierp", "\u2118");
		pc.addXmlEntity("image", "\u2111");
		pc.addXmlEntity("real", "\u211c");
		pc.addXmlEntity("trade", "\u2122");
		pc.addXmlEntity("alefsym", "\u2135");
		pc.addXmlEntity("larr", "\u2190");
		pc.addXmlEntity("uarr", "\u2191");
		pc.addXmlEntity("rarr", "\u2192");
		pc.addXmlEntity("darr", "\u2193");
		pc.addXmlEntity("harr", "\u2194");
		pc.addXmlEntity("crarr", "\u21b5");
		pc.addXmlEntity("lArr", "\u21d0");
		pc.addXmlEntity("uArr", "\u21d1");
		pc.addXmlEntity("rArr", "\u21d2");
		pc.addXmlEntity("dArr", "\u21d3");
		pc.addXmlEntity("hArr", "\u21d4");
		pc.addXmlEntity("forall", "\u2200");
		pc.addXmlEntity("part", "\u2202");
		pc.addXmlEntity("exist", "\u2203");
		pc.addXmlEntity("empty", "\u2205");
		pc.addXmlEntity("nabla", "\u2207");
		pc.addXmlEntity("isin", "\u2208");
		pc.addXmlEntity("notin", "\u2209");
		pc.addXmlEntity("ni", "\u220b");
		pc.addXmlEntity("prod", "\u220f");
		pc.addXmlEntity("sum", "\u2211");
		pc.addXmlEntity("minus", "\u2212");
		pc.addXmlEntity("lowast", "\u2217");
		pc.addXmlEntity("radic", "\u221a");
		pc.addXmlEntity("prop", "\u221d");
		pc.addXmlEntity("infin", "\u221e");
		pc.addXmlEntity("ang", "\u2220");
		pc.addXmlEntity("and", "\u2227");
		pc.addXmlEntity("or", "\u2228");
		pc.addXmlEntity("cap", "\u2229");
		pc.addXmlEntity("cup", "\u222a");
		pc.addXmlEntity("int", "\u222b");
		pc.addXmlEntity("there4", "\u2234");
		pc.addXmlEntity("sim", "\u223c");
		pc.addXmlEntity("cong", "\u2245");
		pc.addXmlEntity("asymp", "\u2248");
		pc.addXmlEntity("ne", "\u2260");
		pc.addXmlEntity("equiv", "\u2261");
		pc.addXmlEntity("le", "\u2264");
		pc.addXmlEntity("ge", "\u2265");
		pc.addXmlEntity("sub", "\u2282");
		pc.addXmlEntity("sup", "\u2283");
		pc.addXmlEntity("nsub", "\u2284");
		pc.addXmlEntity("sube", "\u2286");
		pc.addXmlEntity("supe", "\u2287");
		pc.addXmlEntity("oplus", "\u2295");
		pc.addXmlEntity("otimes", "\u2297");
		pc.addXmlEntity("perp", "\u22a5");
		pc.addXmlEntity("sdot", "\u22c5");
		pc.addXmlEntity("lceil", "\u2308");
		pc.addXmlEntity("rceil", "\u2309");
		pc.addXmlEntity("lfloor", "\u230a");
		pc.addXmlEntity("rfloor", "\u230b");
		pc.addXmlEntity("lang", "\u2329");
		pc.addXmlEntity("rang", "\u232a");
		pc.addXmlEntity("loz", "\u25ca");
		pc.addXmlEntity("spades", "\u2660");
		pc.addXmlEntity("clubs", "\u2663");
		pc.addXmlEntity("hearts", "\u2665");
		pc.addXmlEntity("diams", "\u2666");

		// 24.4 Character entity references for markup-significant and internationalization characters
		pc.addXmlEntity("quot", "" + '\u0022'); // Eclipse gets really confused!
		pc.addXmlEntity("amp", "\u0026");
		pc.addXmlEntity("lt", "\u003c");
		pc.addXmlEntity("gt", "\u003e");
		pc.addXmlEntity("OElig", "\u0152");
		pc.addXmlEntity("oelig", "\u0153");
		pc.addXmlEntity("Scaron", "\u0160");
		pc.addXmlEntity("scaron", "\u0161");
		pc.addXmlEntity("Yuml", "\u0178");
		pc.addXmlEntity("circ", "\u02c6");
		pc.addXmlEntity("tilde", "\u02dc");
		pc.addXmlEntity("ensp", "\u2002");
		pc.addXmlEntity("emsp", "\u2003");
		pc.addXmlEntity("thinsp", "\u2009");
		pc.addXmlEntity("zwnj", "\u200c");
		pc.addXmlEntity("zwj", "\u200d");
		pc.addXmlEntity("lrm", "\u200e");
		pc.addXmlEntity("rlm", "\u200f");
		pc.addXmlEntity("ndash", "\u2013");
		pc.addXmlEntity("mdash", "\u2014");
		pc.addXmlEntity("lsquo", "\u2018");
		pc.addXmlEntity("rsquo", "\u2019");
		pc.addXmlEntity("sbquo", "\u201a");
		pc.addXmlEntity("ldquo", "\u201c");
		pc.addXmlEntity("rdquo", "\u201d");
		pc.addXmlEntity("bdquo", "\u201e");
		pc.addXmlEntity("dagger", "\u2020");
		pc.addXmlEntity("Dagger", "\u2021");
		pc.addXmlEntity("permil", "\u2030");
		pc.addXmlEntity("lsaquo", "\u2039");
		pc.addXmlEntity("rsaquo", "\u203a");
		pc.addXmlEntity("euro", "\u20ac");
	}

	protected void addLctMappings(ParserConfigImpl pc)
	{
	}

	protected void configureSiteProperties(WikiConfigImpl c)
	{
		c.setSiteName("My Wiki");

		c.setWikiUrl("http://localhost/");

		c.setContentLang("xx");

		c.setIwPrefix("xx");
	}

	protected void addNamespaces(WikiConfigImpl c)
	{
		c.addNamespace(new NamespaceImpl(
				-2,
				"Media",
				"Media",
				false,
				false,
				new ArrayList<String>()));

		c.addNamespace(new NamespaceImpl(
				-1,
				"Special",
				"Special",
				false,
				false,
				new ArrayList<String>()));

		c.addNamespace(new NamespaceImpl(
				0,
				"",
				"",
				false,
				false,
				new ArrayList<String>()));

		c.addNamespace(new NamespaceImpl(
				1,
				"Talk",
				"Talk",
				false,
				false,
				new ArrayList<String>()));

		c.addNamespace(new NamespaceImpl(
				2,
				"User",
				"User",
				false,
				false,
				new ArrayList<String>()));

		c.addNamespace(new NamespaceImpl(
				3,
				"User talk",
				"User talk",
				false,
				false,
				new ArrayList<String>()));

		c.addNamespace(new NamespaceImpl(
				4,
				"Project",
				"Project",
				false,
				false,
				Arrays.asList("WP")));

		c.addNamespace(new NamespaceImpl(
				5,
				"Project talk",
				"Project talk",
				false,
				false,
				Arrays.asList("WT")));

		c.addNamespace(new NamespaceImpl(
				6,
				"File",
				"File",
				false,
				true,
				Arrays.asList("Image")));

		c.addNamespace(new NamespaceImpl(
				7,
				"File talk",
				"File talk",
				false,
				false,
				Arrays.asList("Image talk")));

		c.addNamespace(new NamespaceImpl(
				8,
				"MediaWiki",
				"MediaWiki",
				false,
				false,
				new ArrayList<String>()));

		c.addNamespace(new NamespaceImpl(
				9,
				"MediaWiki talk",
				"MediaWiki talk",
				false,
				false,
				new ArrayList<String>()));

		c.addNamespace(new NamespaceImpl(
				10,
				"Template",
				"Template",
				false,
				false,
				new ArrayList<String>()));

		c.addNamespace(new NamespaceImpl(
				11,
				"Template talk",
				"Template talk",
				false,
				false,
				new ArrayList<String>()));

		c.addNamespace(new NamespaceImpl(
				12,
				"Help",
				"Help",
				false,
				false,
				new ArrayList<String>()));

		c.addNamespace(new NamespaceImpl(
				13,
				"Help talk",
				"Help talk",
				false,
				false,
				new ArrayList<String>()));

		c.addNamespace(new NamespaceImpl(
				14,
				"Category",
				"Category",
				false,
				false,
				new ArrayList<String>()));

		c.addNamespace(new NamespaceImpl(
				15,
				"Category talk",
				"Category talk",
				false,
				false,
				new ArrayList<String>()));

		c.setDefaultNamespace(c.getNamespace(0));
		c.setTemplateNamespace(c.getNamespace(10));
	}

	protected void addInterwikis(WikiConfigImpl c)
	{
	}

	protected void addI18nAliases(WikiConfigImpl c)
	{
	}

	protected void addParserFunctions(WikiConfigImpl c)
	{
		c.addParserFunctionGroup(BuiltInParserFunctions.group(c));

		c.addParserFunctionGroup(CorePfnBehaviorSwitches.group(c));

		c.addParserFunctionGroup(CorePfnFunctionsFormatting.group(c));
		c.addParserFunctionGroup(CorePfnFunctionsLocalization.group(c));
		c.addParserFunctionGroup(CorePfnFunctionsMiscellaneous.group(c));
		c.addParserFunctionGroup(CorePfnFunctionsNamespaces.group(c));
		c.addParserFunctionGroup(CorePfnFunctionsUrlData.group(c));

		c.addParserFunctionGroup(CorePfnVariablesDateAndTime.group(c));
		c.addParserFunctionGroup(CorePfnVariablesNamespaces.group(c));
		c.addParserFunctionGroup(CorePfnVariablesPageNames.group(c));
		c.addParserFunctionGroup(CorePfnVariablesStatistics.group(c));
		c.addParserFunctionGroup(CorePfnVariablesTechnicalMetadata.group(c));

		c.addParserFunctionGroup(ParserFunctionsPfnExt.group(c));
	}

	protected void addTagExtensions(WikiConfigImpl c)
	{
		c.addTagExtensionGroup(BuiltInTagExtensions.group(c));

		c.addTagExtensionGroup(MathTagExt.group(c));

		c.addTagExtensionGroup(RefTagExt.group(c));
	}
}
