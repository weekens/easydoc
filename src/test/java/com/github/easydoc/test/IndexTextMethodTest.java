package com.github.easydoc.test;

import java.util.Arrays;
import java.util.Collections;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.easydoc.model.Doc;
import com.github.easydoc.semantics.methods.IndexTextMethod;

import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateModelException;

public class IndexTextMethodTest {
	private IndexTextMethod indexText;
	
	@Before
	public void setUp() {
		indexText = new IndexTextMethod();
	}

	@Test
	public void testDefault() throws Exception {
		Doc doc = new Doc();
		doc.setText("\n\n  Document header  \n\nDocument text");
		doc.setId("doc1");
		
		StringModel stringModel = EasyMock.createMock(StringModel.class);
		EasyMock.expect(stringModel.getWrappedObject()).andReturn(doc);
		
		EasyMock.replay(stringModel);
		
		String result = (String)indexText.exec(Collections.singletonList(stringModel));
		
		Assert.assertEquals("Document header", result);
		
		EasyMock.verify(stringModel);
	}
	
	@Test
	public void testNoEol() throws Exception {
		Doc doc = new Doc();
		doc.setText("\nDocument header");
		doc.setId("doc1");
		
		StringModel stringModel = EasyMock.createMock(StringModel.class);
		EasyMock.expect(stringModel.getWrappedObject()).andReturn(doc);
		
		EasyMock.replay(stringModel);
		
		String result = (String)indexText.exec(Collections.singletonList(stringModel));
		
		Assert.assertEquals("Document header", result);
		
		EasyMock.verify(stringModel);
	}
	
	@Test
	public void testWithHeader() throws Exception {
		Doc doc = new Doc();
		doc.setText("\n\n  <h1>Document header</h1>  \n\nDocument text");
		doc.setId("doc1");
		
		StringModel stringModel = EasyMock.createMock(StringModel.class);
		EasyMock.expect(stringModel.getWrappedObject()).andReturn(doc);
		
		EasyMock.replay(stringModel);
		
		String result = (String)indexText.exec(Collections.singletonList(stringModel));
		
		Assert.assertEquals("Document header", result);
		
		EasyMock.verify(stringModel);
	}
	
	@Test
	public void testWithMarkup() throws Exception {
		Doc doc = new Doc();
		doc.setText("\n\n  <b>Document header</b>  \n\nDocument text");
		doc.setId("doc1");
		
		StringModel stringModel = EasyMock.createMock(StringModel.class);
		EasyMock.expect(stringModel.getWrappedObject()).andReturn(doc);
		
		EasyMock.replay(stringModel);
		
		String result = (String)indexText.exec(Collections.singletonList(stringModel));
		
		Assert.assertEquals("<b>Document header</b>", result);
		
		EasyMock.verify(stringModel);
	}
	
	@Test
	public void testWithTooLongHeader() throws Exception {
		Doc doc = new Doc();
		doc.setText("\n\n  <b>Document header too long!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!</b>  \n\nDocument text");
		doc.setId("doc1");
		
		StringModel stringModel = EasyMock.createMock(StringModel.class);
		EasyMock.expect(stringModel.getWrappedObject()).andReturn(doc);
		
		EasyMock.replay(stringModel);
		
		String result = (String)indexText.exec(Collections.singletonList(stringModel));
		
		Assert.assertNull(result);
		
		EasyMock.verify(stringModel);
	}
	
	@Test(expected = TemplateModelException.class)
	public void testBadArguments() throws Exception {
		StringModel stringModel = EasyMock.createMock(StringModel.class);
		EasyMock.expect(stringModel.getWrappedObject()).andReturn("Hi there!");
		
		EasyMock.replay(stringModel);
		
		indexText.exec(Collections.singletonList(stringModel));
		
		Assert.fail("Exception should be thrown because of bad arguments");
	}
	
	@Test(expected = TemplateModelException.class)
	public void testBadArguments2() throws Exception {
		indexText.exec(Collections.singletonList("Hi again! I'm a doc!"));
		
		Assert.fail("Exception should be thrown because of bad arguments");
	}
	
	@Test(expected = TemplateModelException.class)
	public void testBadArguments3() throws Exception {
		Doc doc = new Doc();
		indexText.exec(Arrays.asList(doc, "String"));
		
		Assert.fail("Exception should be thrown because of bad arguments");
	}
}
