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

package org.sweble.wikitext.parser.encval;

import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.nodes.WtIllegalCodePoint;
import org.sweble.wikitext.parser.nodes.WtIllegalCodePoint.IllegalCodePointType;
import org.sweble.wikitext.parser.nodes.WtNodeFactory;

import de.fau.cs.osr.ptk.common.ast.AstLocation;


%% /**************************************************************** Options **/


%type Object
%class EncodingValidatorLexer

%public
%unicode
%line
%column


/***************************************************************** Java Code **/


%{
  private StringBuilder text = new StringBuilder();

  private WtEntityMap entityMap;

  private String file;

  private WtNodeFactory nf;

  // ===========================================================================

  public void setEntityMap(WtEntityMap entityMap)
  {
    this.entityMap = entityMap;
  }

  public void setFile(String file)
  {
    this.file = file;
  }

  public void setNodeFactory(WtNodeFactory nodeFactory)
  {
    this.nf = nodeFactory;
  }

  private void wrapIllegalCodePoint(int line, int column, String codePoint, IllegalCodePointType type)
  {
    WtIllegalCodePoint p = nf.illegalCp(codePoint, type);
    p.setNativeLocation(new AstLocation(
        file,
        line,
        column));

    int id = entityMap.registerEntity(p);

    text.append('\uE000');
    text.append(id);
    text.append('\uE001');
  }

  public String getWikitext()
  {
    return text.toString();
  }
%}


/******************************************************************** Macros **/


IC_HIGH_NO_LOW      = [\uD800-\uDBFF][\u0000-\uDBFF\uE000-\uFFFF]
IC_NO_HIGH_LOW      = [\u0000-\uD7FF\uDC00-\uFFFF][\uDC00-\uDFFF]

NC_00FDD0_to_00FDEF = [\uFDD0-\uFDEF]
NC_00FFFE_or_00FFFF = [\uFFFE\uFFFF]
NC_xxFFFE_or_xxFFFF = [\uD83F\uD87F\uD8BF\uD8FF\uD93F\uD97F\uD9BF\uD9FF\uDA3F\uDA7F\uDABF\uDAFF\uDB3F\uDB7F\uDBBF\uDBFF][\uDFFE\uDFFF]
NON_CHAR            = {NC_00FDD0_to_00FDEF}|{NC_00FFFE_or_00FFFF}|{NC_xxFFFE_or_xxFFFF}

PU_00E000_to_00F8FF = [\uE000-\uF8FF]
PU_0F0000_to_0FFFFD = [\uDB80-\uDBBE][\uDC00-\uDFFF]|\uDBBF[\uDC00-\uDFFD]
PU_100000_to_10FFFD = [\uDBC0-\uDBFE][\uDC00-\uDFFF]|\uDBFF[\uDC00-\uDFFD]
PRIVATE_USE_CHAR    = {PU_00E000_to_00F8FF}|{PU_0F0000_to_0FFFFD}|{PU_100000_to_10FFFD}

CONTROL_CHAR        = [\u0000-\u0008\u000B\u000C\u000E-\u0019\u007F]


%% /****************************************************************** Rules **/


/* Isolated surrogates
 */
{IC_HIGH_NO_LOW}      {
                        String match = yytext();
                        wrapIllegalCodePoint(
                            yyline,
                            yycolumn,
                            match.substring(0, 1),
                            IllegalCodePointType.ISOLATED_SURROGATE);
                        text.append(match.substring(1));
                      }
{IC_NO_HIGH_LOW}      {
                        String match = yytext();
                        text.append(match.substring(0, 1));
                        wrapIllegalCodePoint(
                            yyline,
                            yycolumn + 1,
                            match.substring(1),
                            IllegalCodePointType.ISOLATED_SURROGATE);
                      }


/* Non-character
 */
{NON_CHAR}            {
                        wrapIllegalCodePoint(
                            yyline, 
                            yycolumn, 
                            yytext(), 
                            IllegalCodePointType.NON_CHARACTER);
                      }


/* Private-use character
 */
{PRIVATE_USE_CHAR}    {
                        wrapIllegalCodePoint(
                            yyline, 
                            yycolumn, 
                            yytext(), 
                            IllegalCodePointType.PRIVATE_USE_CHARACTER);
                      }


/* Private-use character
 */
{CONTROL_CHAR}        {
                        wrapIllegalCodePoint(
                            yyline, 
                            yycolumn, 
                            yytext(), 
                            IllegalCodePointType.CONTROL_CHARACTER);
                      }


/* Everything else
 */
.                     |
\n                    {
                        text.append(yytext());
                      }


/************************************************************** End of file. **/
