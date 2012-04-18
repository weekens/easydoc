package com.github.easydoc.test;

import org.junit.Assert;
import org.junit.Test;

import com.github.easydoc.param.SourceBrowserParam;
import com.github.easydoc.sourcebrowser.SourceBrowser;

public class ReflectionSanityTest {
	
	@Test
	public void testGithubSourceBrowser() throws Exception {
		SourceBrowserParam sbParam = new SourceBrowserParam();
		sbParam.setType("github");
		sbParam.setBaseUrl("http://github.com");
		
		SourceBrowser sourceBrowser = sbParam.getType()
			.getSourceBrowserClass()
			.getConstructor(SourceBrowserParam.class)
			.newInstance(sbParam);
		Assert.assertNotNull(sourceBrowser);
	}

}
