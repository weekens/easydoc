package com.github.easydoc.test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import com.github.easydoc.CommandLineMojoUtils;
import com.github.easydoc.EasydocMojo;
import com.github.easydoc.param.SourceBrowserParam;
import com.github.easydoc.sourcebrowser.SourceBrowser;

public class ReflectionSanityTest {
	
	private Object getFieldValue(Object object, String fieldName) 
			throws NoSuchFieldException, IllegalAccessException 
	{
		Field field = object.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(object);
	}
	
	@Test
	public void testGithubSourceBrowser() throws Exception {
		for(SourceBrowserParam.Type type : EnumSet.allOf(SourceBrowserParam.Type.class)) {
			SourceBrowserParam sbParam = new SourceBrowserParam();
			sbParam.setType(type.toString());
			sbParam.setBaseUrl("http://company.com");
			
			SourceBrowser sourceBrowser = sbParam.getType()
				.getSourceBrowserClass()
				.getConstructor(SourceBrowserParam.class)
				.newInstance(sbParam);
			Assert.assertNotNull(sourceBrowser);
		}
	}
	
	@Test
	public void testCommandLineUtilsEmptyArgs() throws Exception {
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
	public void testCommandLineUtilsCustomCss() throws Exception {
		String customCssPath = "src/css/custom.css";
		String[] args = new String[] { "customCss=" + customCssPath };
		EasydocMojo mojo = new EasydocMojo();
		CommandLineMojoUtils.injectMojoProperties(mojo, args);
		
		Object customCssValue = getFieldValue(mojo, "customCss");
		Assert.assertEquals(new File(customCssPath).getAbsolutePath(), ((File)customCssValue).getAbsolutePath());
	}

}
