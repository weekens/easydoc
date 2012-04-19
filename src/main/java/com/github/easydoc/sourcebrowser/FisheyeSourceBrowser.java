package com.github.easydoc.sourcebrowser;

import java.net.MalformedURLException;
import java.net.URL;

import com.github.easydoc.model.Doc;
import com.github.easydoc.param.SourceBrowserParam;

public class FisheyeSourceBrowser implements SourceBrowser {
	
	private final SourceBrowserParam sbParam;

	public FisheyeSourceBrowser(SourceBrowserParam sbParam) {
		this.sbParam = sbParam;
	}

	@Override
	public URL generateUrl(Doc doc) {
		try {
			return new URL(String.format(
					"%s%s?r=%d#to%d", 
					sbParam.getBaseUrl(),
					doc.getSourceLink().getFile().getPath(),
					sbParam.getRevision(),
					doc.getSourceLink().getStartLine())
			);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
