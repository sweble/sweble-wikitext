package org.example;

public enum SerializationMethod
{
	JSON
	{
		@Override
		public String getExt()
		{
			return "json";
		}
	},
	JAVA
	{
		@Override
		public String getExt()
		{
			return "bin";
		}
	},
	XML
	{
		@Override
		public String getExt()
		{
			return "xml";
		}
	};
	
	public abstract String getExt();
}
