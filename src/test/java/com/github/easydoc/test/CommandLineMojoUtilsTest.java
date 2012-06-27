package com.github.easydoc.test;

import java.io.File;
import java.lang.reflect.Field;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import com.github.easydoc.CommandLineMojoUtils;
import com.github.easydoc.EasydocMojo;
import com.github.easydoc.param.SourceBrowserParam;

public class CommandLineMojoUtilsTest {
	
	private Object getFieldValue(Object object, String fieldName) 
			throws NoSuchFieldException, IllegalAccessException 
	{
		Field field = object.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(object);
	}

	@Test
	public void testEmptyArgs() throws Exception {
		String[] args = new String[] {};
		EasydocMojo mojo = new EasydocMojo();
		CommandLineMojoUtils.injectMojoProperties(mojo, args);
		
		File inputDir = new File("src");
		Object inputDirectoryValue = getFieldValue(mojo, "inputDirectory");
		Assert.assertEquals(inputDir.getAbsolutePath(), ((File)inputDirectoryValue).getAbsolutePath());
		
		File outputDir = new File("build/easydoc");
		Object outputDirectoryValue = getFieldValue(mojo, "outputDirectory");
		Assert.assertEquals(outputDir.getAbsolutePath(), ((File)outputDirectoryValue).getAbsolutePath());
	}
	
	@Test
	public void testFileParameter() throws Exception {
		String customCssPath = "src/css/custom.css";
		String[] args = new String[] { "customCss=" + customCssPath };
		EasydocMojo mojo = new EasydocMojo();
		CommandLineMojoUtils.injectMojoProperties(mojo, args);
		
		Object customCssValue = getFieldValue(mojo, "customCss");
		Assert.assertEquals(new File(customCssPath).getAbsolutePath(), ((File)customCssValue).getAbsolutePath());
	}
	
	@Test
	public void testObjectParameter() throws Exception {
		String sourceBrowserJson = "{\"baseUrl\":\"http://mycompany.com/src\",\"type\":\"github\"}";
		String[] args = new String[] { "sourceBrowser=" + sourceBrowserJson };
		EasydocMojo mojo = new EasydocMojo();
		CommandLineMojoUtils.injectMojoProperties(mojo, args);
		
		ObjectMapper om = new ObjectMapper();
		SourceBrowserParam sourceBrowserExpected = om.readValue(sourceBrowserJson.getBytes(), SourceBrowserParam.class);
		
		Object sourceBrowserActual = getFieldValue(mojo, "sourceBrowser");
		Assert.assertEquals(sourceBrowserExpected, sourceBrowserActual);
	}
}
