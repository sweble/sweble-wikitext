package org.sweble.wikitext.engine.dom;

import java.util.Collection;

public interface DomTemplateInvocation
        extends
            DomProcessingInstruction
{
	public String getName();
	
	public Collection<DomTemplateArgument> getArguments();
}
