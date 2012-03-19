package com.github.easydoc.test;

import java.util.List;

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
		String input = "@@easydoc-start,id=main-header@@ Doc @@easydoc-end@@";
		
		EasydocLexer lexer = new EasydocLexer(new ANTLRStringStream(input));
		EasydocParser parser = new EasydocParser(new CommonTokenStream(lexer));
		List<Doc> docs = parser.document();
		
		Assert.assertEquals(1, docs.size());
		Doc doc = docs.get(0);
		Assert.assertEquals("main-header", doc.getParams().get("id"));
		Assert.assertEquals(" Doc ", doc.getText());
	}

}
