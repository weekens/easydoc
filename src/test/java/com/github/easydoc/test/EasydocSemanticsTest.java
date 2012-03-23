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
}
