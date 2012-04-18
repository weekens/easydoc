package com.github.easydoc.test;

import java.util.List;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Assert;
import org.junit.Test;

import com.github.easydoc.EasydocLexer;
import com.github.easydoc.EasydocParser;
import com.github.easydoc.model.Doc;

public class EasydocParserTest {
	
	@Test
	public void testSingleDocWithParams() throws RecognitionException {
		String input = "@@easydoc-start,id=main-header,ignore=\\\\\\,#@@ \\\\, Doc \\@\\@ @ doc @@easydoc-end@@";
		
		EasydocLexer lexer = new EasydocLexer(new ANTLRStringStream(input));
		EasydocParser parser = new EasydocParser(new CommonTokenStream(lexer));
		List<Doc> docs = parser.document();
		
		Assert.assertEquals(1, docs.size());
		Doc doc = docs.get(0);
		Assert.assertEquals("main-header", doc.getParams().get("id"));
		Assert.assertEquals("\\,#", doc.getParams().get("ignore"));
		Assert.assertEquals(" \\, Doc @@ @ doc ", doc.getText());
		Assert.assertEquals(1, doc.getSourceLink().getStartLine());
		Assert.assertEquals(1, doc.getSourceLink().getEndLine());
		Assert.assertNull(doc.getSourceLink().getFile());
	}
	
	@Test
	public void testXmlComment() throws Exception {		
		EasydocLexer lexer = new EasydocLexer(
				new ANTLRInputStream(getClass().getResourceAsStream("/normal-pom.xml"))
		);
		EasydocParser parser = new EasydocParser(new CommonTokenStream(lexer));
		List<Doc> docs = parser.document();
		
		Assert.assertEquals(1, docs.size());
		Doc doc = docs.get(0);
		Assert.assertEquals("main-header", doc.getParams().get("belongs"));
		Assert.assertTrue(doc.getText().contains("Documentation in pom.xml"));
		Assert.assertEquals(7, doc.getSourceLink().getStartLine());
		Assert.assertEquals(11, doc.getSourceLink().getEndLine());
		Assert.assertNull(doc.getSourceLink().getFile());
	}

}
