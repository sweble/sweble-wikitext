package org.sweble.wikitext.engine.dom;

import java.util.Collection;

public interface DomTagExtensionInvocation
        extends
            DomProcessingInstruction
{
	public String getName();
	
	public Collection<DomTemplateArgument> getArguments();
}
