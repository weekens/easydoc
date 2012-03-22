package com.github.easydoc.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.easydoc.model.Doc;
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
}
