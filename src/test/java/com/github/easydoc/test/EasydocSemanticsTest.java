package com.github.easydoc.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Test;

import com.github.easydoc.EasydocLexer;
import com.github.easydoc.EasydocParser;
import com.github.easydoc.model.Directive;
import com.github.easydoc.model.Doc;
import com.github.easydoc.model.DocItem;
import com.github.easydoc.model.DocTextItem;
import com.github.easydoc.model.Model;
import com.github.easydoc.semantics.EasydocSemantics;
import com.github.easydoc.semantics.EasydocSemantics.CompilationResult;

public class EasydocSemanticsTest {

	@Test
	public void testBelongs() {
		Model model = new Model();
		List<Doc> docList = new ArrayList<Doc>();
		Doc parent = new Doc();
		parent.getParams().put("id", "parent-doc");
		docList.add(parent);
		
		Doc child1 = new Doc();
		child1.getParams().put("id", "child-doc");
		child1.getParams().put("belongs", "parent-doc");
		docList.add(child1);
		
		Doc child2 = new Doc();
		child2.getParams().put("belongs", "parent-doc");
		docList.add(child2);
		model.addDocs(docList);
		
		EasydocSemantics sem = new EasydocSemantics();
		CompilationResult result = sem.compileModel(model);
		Assert.assertTrue("Compilation result is NEGATIVE: " + result, result.isPositive());
		Assert.assertTrue(child1.getParent() == parent);
		Assert.assertTrue(child2.getParent() == parent);
		Assert.assertNull(parent.getParent());
	}
	
	@Test
	public void testUnexistentParameter() {
		Model model = new Model();
		Doc doc = new Doc();
		doc.getParams().put("id", "doc-id");
		doc.getParams().put("parameter-that-will-never-exist", "value");
		model.addDocs(Collections.singletonList(doc));
		
		EasydocSemantics sem = new EasydocSemantics();
		CompilationResult result = sem.compileModel(model);
		Assert.assertFalse(result.isPositive());
		Assert.assertEquals(1, result.getErrors().size());
	}
	
	@Test
	public void testUnexistentParent() {
		Model model = new Model();
		Doc doc = new Doc();
		doc.getParams().put("belongs", "some-unexistent-id");
		model.addDocs(Collections.singletonList(doc));
		
		EasydocSemantics sem = new EasydocSemantics();
		CompilationResult result = sem.compileModel(model);
		Assert.assertFalse(result.isPositive());
		Assert.assertEquals(1, result.getErrors().size());
	}
	
	@Test
	public void testValueParameterWithoutValue() {
		Model model = new Model();
		Doc doc = new Doc();
		doc.getParams().put("id", null);
		model.addDocs(Collections.singletonList(doc));
		
		EasydocSemantics sem = new EasydocSemantics();
		CompilationResult result = sem.compileModel(model);
		Assert.assertFalse(result.isPositive());
		Assert.assertEquals(1, result.getErrors().size());
	}
	
	@Test
	public void testWeightsTree() {
		Model model = new Model();
		
		Doc root1 = new Doc();
		root1.getParams().put("weight", "1");
		
		Doc root2 = new Doc();
		root2.getParams().put("id", "root2");
		root2.getParams().put("weight", "-1");
		
		Doc root2Child1 = new Doc();
		root2Child1.getParams().put("belongs", "root2");
		root2Child1.getParams().put("weight", "22");
		
		Doc root2Child2 = new Doc();
		root2Child2.getParams().put("belongs", "root2");
		root2Child2.getParams().put("weight", "min");
		
		Doc root2Child3 = new Doc();
		root2Child3.getParams().put("belongs", "root2");
		root2Child3.getParams().put("weight", "max");
		
		Doc root2Child4 = new Doc();
		root2Child4.getParams().put("belongs", "root2");
		
		List<Doc> docList = new ArrayList<Doc>();
		Collections.addAll(docList, root1, root2, root2Child1, root2Child2, root2Child3, root2Child4);
		model.addDocs(docList);
		
		EasydocSemantics sem = new EasydocSemantics();
		CompilationResult result = sem.compileModel(model);
		Assert.assertTrue(result.isPositive());
		
		@SuppressWarnings("unchecked")
		List<Doc> tree = (List<Doc>)model.toFreemarkerModel().get("doctree");
		Assert.assertEquals(root2, tree.get(0));
		Assert.assertEquals(root1, tree.get(1));
		
		List<Doc> childTree = tree.get(0).getChildren();
		Assert.assertEquals(root2Child2, childTree.get(0));
		Assert.assertEquals(root2Child4, childTree.get(1));
		Assert.assertEquals(root2Child1, childTree.get(2));
		Assert.assertEquals(root2Child3, childTree.get(3));
	}
	
	@Test
	public void testIgnore() {
		Model model = new Model();
		Doc doc = new Doc();
		doc.setText("Hi! This # is a text # with @ some hashes @nd other stuff...");
		doc.getParams().put("ignore", "#@.");
		model.addDocs(Collections.singletonList(doc));
		
		EasydocSemantics sem = new EasydocSemantics();
		CompilationResult result = sem.compileModel(model);
		Assert.assertTrue(result.isPositive());
		Assert.assertEquals("Hi! This  is a text  with  some hashes nd other stuff", doc.getText());
	}
	
	@Test
	public void testInclude() {
		Model model = new Model();
		Doc toInclude = new Doc();
		toInclude.getParams().put("id", "to-include");
		toInclude.setText("Wazzup!");
		
		Doc doc = new Doc();
		Directive includeDirective = new Directive();
		includeDirective.setName("include");
		includeDirective.getParams().put("id", "to-include");
		doc.setDirectives(Collections.singletonList(includeDirective));
		doc.setItems(
				Arrays.asList(new DocItem[] { 
						new DocTextItem("Include ->"),
						includeDirective,
						new DocTextItem("<- here")
				})
		);
		
		model.addDocs(Arrays.asList(new Doc[] { toInclude, doc }));
		
		EasydocSemantics sem = new EasydocSemantics();
		CompilationResult result = sem.compileModel(model);
		Assert.assertTrue(result.isPositive());
		Assert.assertEquals("Include ->Wazzup!<- here", doc.getText());
	}
	
	@Test
	public void testTwoIncludes() {
		Model model = new Model();
		Doc toInclude = new Doc();
		toInclude.getParams().put("id", "to-include");
		toInclude.setText("Wazzup!");
		
		Doc doc = new Doc();
		Directive includeDirective = new Directive();
		includeDirective.setName("include");
		includeDirective.getParams().put("id", "to-include");
		doc.setDirectives(Collections.singletonList(includeDirective));
		doc.setItems(
				Arrays.asList(new DocItem[] { 
						new DocTextItem("Include ->"),
						includeDirective,
						new DocTextItem("<- here and ->"),
						includeDirective,
						new DocTextItem("<- here ")
				})
		);
		
		model.addDocs(Arrays.asList(new Doc[] { toInclude, doc }));
		
		EasydocSemantics sem = new EasydocSemantics();
		CompilationResult result = sem.compileModel(model);
		Assert.assertTrue(result.isPositive());
		Assert.assertEquals("Include ->Wazzup!<- here and ->Wazzup!<- here ", doc.getText());
	}
	
	@Test
	public void testNormalPom() throws Exception {
		EasydocLexer lexer = new EasydocLexer(
				new ANTLRInputStream(getClass().getResourceAsStream("/normal-pom.xml"))
		);
		EasydocParser parser = new EasydocParser(new CommonTokenStream(lexer));
		List<Doc> docs = parser.document();
		
		Model model = new Model();
		model.addDocs(docs);
		
		EasydocSemantics semantics = new EasydocSemantics();
		CompilationResult compilationResult = semantics.compileModel(model);
		Assert.assertTrue(
				"Negative compilation result: " + compilationResult.toString(), 
				compilationResult.isPositive()
		);
		
		Assert.assertEquals(2, model.getDocs().size());
		Doc doc2 = model.getDocs().get(1);
		Assert.assertEquals(model.getDocs().get(0).getText(), doc2.getText());
	}
	
	@Test
	public void testMarkdown() throws Exception {
		Doc doc = new Doc();
		doc.setText("\tabc\n\t Second line\n\t Third line\n\n\t \tLine 4 (code)\n  Line 5 (other indentation)");
		doc.getParams().put("format", "markdown");
		
		Model model = new Model();
		model.addDocs(Collections.singletonList(doc));
		
		EasydocSemantics semantics = new EasydocSemantics();
		CompilationResult compilationResult = semantics.compileModel(model);
		Assert.assertTrue(
				"Negative compilation result: " + compilationResult.toString(), 
				compilationResult.isPositive()
		);
		
		List<Doc> docs = compilationResult.getModel().getDocs();
		Assert.assertEquals(1, docs.size());
		Assert.assertEquals(
				"<pre><code>abc\n" +
				"</code></pre>\n\n" +
				"<p>Second line\nThird line</p>\n\n" +
				"<pre><code>Line 4 (code)\n</code></pre>\n\n" +
				"<p>  Line 5 (other indentation)</p>\n", 
				docs.get(0).getText()
		);
	}
}
