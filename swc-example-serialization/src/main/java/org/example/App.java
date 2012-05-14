package org.example;

import org.example.Serialization.Method;

public class App
{
	public static void main(String[] args) throws Exception
	{
		Serialization s = new Serialization();
		
		Serialization.parse("raw-Germany.wikitext");
		Serialization.parse("raw-Wallace+Neff.wikitext");
		Serialization.parse("raw-Zygmunt+Kubiak.wikitext");
		Serialization.parse("exp-Saxby+Chambliss.wikitext");
		
		//s.doSerialization(Method.JAVA);
		
		//s.doSerialization(Method.XML);
		
		s.doSerialization(Method.JSON);
		
	}
}
