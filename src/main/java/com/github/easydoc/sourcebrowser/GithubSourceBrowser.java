package com.github.easydoc.sourcebrowser;

import java.net.MalformedURLException;
import java.net.URL;

import com.github.easydoc.model.Doc;
import com.github.easydoc.model.SourceLink;
import com.github.easydoc.param.SourceBrowserParam;

public class GithubSourceBrowser implements SourceBrowser {	
	private final String baseUrl;
	
	public GithubSourceBrowser(SourceBrowserParam sbParam) {
		this.baseUrl = sbParam.getBaseUrl();
	}

	@Override
	public URL generateUrl(Doc doc) {
		try {
			SourceLink sourceLink = doc.getSourceLink();
			return new URL(
					String.format(
							"%s%s#L%d-%d", 
							baseUrl,
							sourceLink.getFile().getPath(),
							sourceLink.getStartLine(),
							sourceLink.getEndLine())
			);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
