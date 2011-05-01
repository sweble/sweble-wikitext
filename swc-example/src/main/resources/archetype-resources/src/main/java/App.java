package ${package};

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.sweble.wikitext.engine.CompiledPage;
import org.sweble.wikitext.engine.Compiler;
import org.sweble.wikitext.engine.CompilerException;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.utils.HtmlPrinter;
import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration;
import org.sweble.wikitext.lazy.LinkTargetException;

public class App
{
	public static void main(String[] args) throws FileNotFoundException, IOException, LinkTargetException, CompilerException
	{
		if (args.length < 1)
		{
			System.err.println("Usage: java -jar scm-example.jar TITLE");
			System.err.println();
			System.err.println("  The program will look for a file called `TITLE.wikitext',");
			System.err.println("  parse the file and write an HTML version to `TITLE.html'.");
			return;
		}
		
		String fileTitle = args[0];
		
		String html = run(new File(fileTitle + ".wikitext"), fileTitle);
		
		FileUtils.writeStringToFile(
		        new File(fileTitle + ".html"),
		        html);
	}
	
	static String run(File file, String fileTitle) throws FileNotFoundException, IOException, LinkTargetException, CompilerException
	{
		// Set-up a simple wiki configuration
		SimpleWikiConfiguration config = new SimpleWikiConfiguration(
		        "classpath:/org/sweble/wikitext/engine/SimpleWikiConfiguration.xml");
		
		// Instantiate a compiler for wiki pages
		Compiler compiler = new Compiler(config);
		
		// Retrieve a page
		PageTitle pageTitle = PageTitle.make(config, fileTitle);
		
		PageId pageId = new PageId(pageTitle, -1);
		
		String wikitext = FileUtils.readFileToString(file);
		
		// Compile the retrieved page
		CompiledPage cp = compiler.postprocess(pageId, wikitext, null);
		
		// Render the compiled page as HTML
		StringWriter w = new StringWriter();
		
		HtmlPrinter p = new HtmlPrinter(w, pageTitle.getFullTitle());
		p.setCssResource("/org/sweble/wikitext/engine/utils/HtmlPrinter.css", "");
		p.setStandaloneHtml(true, "");
		
		p.go(cp.getPage());
		
		return w.toString();
	}
}
