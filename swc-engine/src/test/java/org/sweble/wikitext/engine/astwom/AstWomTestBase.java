package org.sweble.wikitext.engine.astwom;

import org.sweble.wikitext.engine.CompiledPage;
import org.sweble.wikitext.engine.CompilerException;
import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.utils.EntityReferences;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomPage;
import org.sweble.wikitext.lazy.LinkTargetException;
import org.sweble.wikitext.lazy.utils.XmlEntityResolver;

public abstract class AstWomTestBase
{
	protected AstWomTestCommon common;
	
	protected final WomNodeFactory f =
			new AstWomNodeFactory(new XmlEntityResolver()
			{
				@Override
				public String resolveXmlEntity(String name)
				{
					return EntityReferences.resolve(name);
				}
			});
	
	protected WomPage parseToWom(String wt) throws LinkTargetException, CompilerException
	{
		final String TITLE = "-";
		
		PageTitle title = PageTitle.make(common.getConfig(), TITLE);
		
		PageId id = new PageId(title, -1);
		
		wt = String.format("<b>%s</b>", wt);
		
		FullPage fullPage = new FullPage(id, wt);
		
		CompiledPage astPage = common.getCompiler().postprocess(
				fullPage.getId(),
				fullPage.getText(),
				null);
		
		DefaultAstToWomNodeFactory wnf =
				new DefaultAstToWomNodeFactory(common.getConfig(), TITLE);
		
		WomNode womPage = wnf.create(null, astPage.getPage());
		
		return (WomPage) womPage;
	}
}
