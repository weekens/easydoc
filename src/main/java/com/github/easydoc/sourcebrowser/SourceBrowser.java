package com.github.easydoc.sourcebrowser;

import java.net.URL;

import com.github.easydoc.model.Doc;

public interface SourceBrowser {

	URL generateUrl(Doc doc);
}
