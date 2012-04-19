package com.github.easydoc.test;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import com.github.easydoc.param.SourceBrowserParam;
import com.github.easydoc.sourcebrowser.SourceBrowser;

public class ReflectionSanityTest {
	
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

}
