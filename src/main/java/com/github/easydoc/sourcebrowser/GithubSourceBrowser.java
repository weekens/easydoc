package com.github.easydoc.sourcebrowser;

import java.net.MalformedURLException;
import java.net.URL;

import com.github.easydoc.model.Doc;
import com.github.easydoc.model.SourceLink;
import com.github.easydoc.param.SourceBrowserParam;

/*@@easydoc-start, belongs=easydoc-maven-source-browser@@
 <h4>github</h4>
 
 GitHub source browser. To specify it, use the following snippet:<br>
 <pre>
&lt;sourceBrowser&gt;
	&lt;type&gt;github&lt;/type&gt;
	&lt;baseUrl&gt;https://github.com/weekens/easydoc/blob/master/&lt;/baseUrl&gt;
&lt;/sourceBrowser&gt;
 </pre>
 <br>
 Replace <i>baseUrl</i> value with the one from your GitHub repository.
 @@easydoc-end@@*/
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
