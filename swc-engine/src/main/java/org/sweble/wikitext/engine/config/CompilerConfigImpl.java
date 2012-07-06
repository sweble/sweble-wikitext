package org.sweble.wikitext.engine.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(
		name = "CompilerConfig",
		namespace = "org.sweble.wikitext.engine")
@XmlType(propOrder = {
		"trimTransparentBeforeParsing" })
@XmlAccessorType(XmlAccessType.NONE)
public class CompilerConfigImpl
		implements
			CompilerConfig
{
	@XmlElement()
	private boolean trimTransparentBeforeParsing;
	
	// =========================================================================
	
	@Override
	public boolean isTrimTransparentBeforeParsing()
	{
		return trimTransparentBeforeParsing;
	}
	
	public void setTrimTransparentBeforeParsing(
			boolean trimTransparentBeforeParsing)
	{
		this.trimTransparentBeforeParsing = trimTransparentBeforeParsing;
	}
	
	// =========================================================================
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (trimTransparentBeforeParsing ? 1231 : 1237);
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompilerConfigImpl other = (CompilerConfigImpl) obj;
		if (trimTransparentBeforeParsing != other.trimTransparentBeforeParsing)
			return false;
		return true;
	}
}
