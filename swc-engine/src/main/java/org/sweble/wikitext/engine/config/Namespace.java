package org.sweble.wikitext.engine.config;

import java.util.TreeSet;

public interface Namespace
{
	
	public abstract int getId();
	
	public abstract String getName();
	
	public abstract boolean isCanHaveSubpages();
	
	public abstract String getCanonical();
	
	public abstract TreeSet<String> getAliases();
	
	public abstract boolean isFileNs();
	
	public abstract boolean isMediaNs();
	
	public abstract boolean isTalkNamespace();
	
	public abstract boolean isSubjectNamespace();
	
	public abstract int getTalkspaceId();
	
	public abstract int getSubjectspaceId();
	
}
