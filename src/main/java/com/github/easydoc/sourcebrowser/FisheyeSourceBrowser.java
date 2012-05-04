package com.github.easydoc.sourcebrowser;

import java.net.MalformedURLException;
import java.net.URL;

import com.github.easydoc.model.Doc;
import com.github.easydoc.param.SourceBrowserParam;

/*@@easydoc-start, belongs=easydoc-maven-source-browser, weight=50@@
<p><b>fisheye</b></p>

Fisheye source browser. To specify it, use the following snippet:<br>
<pre>
&lt;sourceBrowser&gt;
	&lt;type&gt;fisheye&lt;/type&gt;
	&lt;baseUrl&gt;https://mycompany.com/browse/myrepo/mysourcepath/&lt;/baseUrl&gt;
&lt;/sourceBrowser&gt;
</pre>
<br>
You need to check your Fisheye repository path by just going to the 'Source' section.
@@easydoc-end@@*/
public class FisheyeSourceBrowser implements SourceBrowser {
	
	private final SourceBrowserParam sbParam;

	public FisheyeSourceBrowser(SourceBrowserParam sbParam) {
		this.sbParam = sbParam;
	}

	@Override
	public URL generateUrl(Doc doc) {
		try {
			return new URL(String.format(
					"%s%s?hb=true#to%d", 
					sbParam.getBaseUrl(),
					doc.getSourceLink().getFile().getPath(),
					doc.getSourceLink().getStartLine())
			);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
