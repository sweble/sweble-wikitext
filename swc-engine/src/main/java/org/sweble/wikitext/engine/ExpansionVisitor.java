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

package org.sweble.wikitext.engine;

import java.util.LinkedList;
import java.util.List;

import org.sweble.wikitext.lazy.preprocessor.Redirect;
import org.sweble.wikitext.lazy.preprocessor.TagExtension;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.preprocessor.TemplateArgument;
import org.sweble.wikitext.lazy.preprocessor.TemplateParameter;
import org.sweble.wikitext.lazy.utils.StringConversionException;
import org.sweble.wikitext.lazy.utils.StringConverter;
import org.sweble.wikitext.lazy.utils.StringConverterPartial;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;
import de.fau.cs.osr.utils.Tuple2;

public final class ExpansionVisitor
        extends
            AstVisitor
{
	private final ExpansionFrame preprocessorFrame;
	
	// =========================================================================
	
	public ExpansionVisitor(ExpansionFrame preprocessorFrame)
	{
		this.preprocessorFrame = preprocessorFrame;
	}
	
	// =========================================================================
	
	public AstNode visit(AstNode n)
	{
		mapInPlace(n);
		return n;
	}
	
	public AstNode visit(Redirect n)
	{
		// FIXME: What if we don't want to redirect, but view the redirect 
		//        page itself? Then we probably don't want to return the
		//        contents...
		
		String target = n.getTarget();
		
		AstNode result = preprocessorFrame.resolveRedirect(n, target);
		if (result == null)
			result = n;
		
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AstNode visit(Template n)
	{
		AstNode result = null;
		
		Tuple2<String, NodeList> converted =
		        StringConverterPartial.convert(n.getName());
		
		String title = converted._1;
		
		NodeList tail = converted._2;
		
		List<TemplateArgument> args = (List) map(n.getArgs());
		
		int i = title.indexOf(':');
		if (i != -1)
		{
			String pfn = title.substring(0, i).trim();
			
			NodeList arg0 = new NodeList(new Text(title.substring(i + 1)), tail);
			
			LinkedList<TemplateArgument> pfnArgs = new LinkedList<TemplateArgument>(args);
			
			pfnArgs.addFirst(new TemplateArgument(arg0, false));
			
			result = preprocessorFrame.resolveParserFunction(n, pfn, pfnArgs);
		}
		
		if (result == null)
		{
			// A template cannot be resolved if the name is not fully resolvable
			if (tail == null)
			{
				result = preprocessorFrame.resolveTransclusionOrMagicWord(n, title, args);
			}
			else
			{
				preprocessorFrame.illegalTemplateName(
				        new StringConversionException(tail), n.getName());
			}
		}
		
		if (result == null)
			result = n;
		
		return result;
	}
	
	public AstNode visit(TemplateParameter n)
	{
		dispatch(n.getName());
		
		String name = null;
		try
		{
			name = StringConverter.convert(n.getName());
		}
		catch (StringConversionException e)
		{
			preprocessorFrame.illegalParameterName(e, n.getName());
		}
		
		AstNode value = null;
		if (name != null)
		{
			name = name.trim();
			value = preprocessorFrame.resolveParameter(n, name);
		}
		
		if (value == null && n.getDefaultValue() != null)
		{
			// Only the first value after the pipe is the default 
			// value. The rest is ignored.
			
			TemplateArgument arg = n.getDefaultValue();
			
			dispatch(arg.getValue());
			
			if (arg.getHasName())
			{
				// The default value cannot be separated into name and 
				// value
				
				dispatch(arg.getName());
				
				value = new NodeList();
				value.add(arg.getName());
				value.add(new Text("="));
				value.add(arg.getValue());
			}
			else
			{
				value = arg.getValue();
			}
		}
		
		if (value == null)
			value = n;
		
		return value;
	}
	
	public AstNode visit(TagExtension n)
	{
		return preprocessorFrame.resolveTagExtension(n, n.getName(), n.getXmlAttributes(), n.getBody());
	}
}
