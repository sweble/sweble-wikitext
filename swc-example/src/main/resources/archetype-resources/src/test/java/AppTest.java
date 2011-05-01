package ${package};

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.sweble.wikitext.engine.CompilerException;
import org.sweble.wikitext.lazy.LinkTargetException;

public class AppTest
{
	@Test
	public void test() throws FileNotFoundException, IOException, LinkTargetException, CompilerException
	{
		String title = "Simple_Page";
		
		URL url = AppTest.class.getResource("/" + title + ".wikitext");
		
		String actual = App.run(new File(url.getFile()), title);
		
		InputStream expectedIs = AppTest.class.getResourceAsStream("/" + title + ".html");
		String expected = IOUtils.toString(expectedIs);
		
		Assert.assertEquals(expected, actual);
	}
}
