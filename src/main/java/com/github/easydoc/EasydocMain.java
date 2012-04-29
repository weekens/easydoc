package com.github.easydoc;

import org.apache.maven.plugin.MojoExecutionException;

/*@@easydoc-start, id=easydoc-commandline@@
<h1>Running from command line</h1>

Easydoc can be run as a simple binary from command line.<br>

<pre>
java -jar easydoc.jar [inputDirectory=mysrc customCss=src/resources/my.css ...other params...]
</pre>
<br>
Parameters, that you can optionally specify, are the same as in Maven plugin.
<br><br>
<b>Note:</b> Currently, only the simple string parameters are supported. Array parameters (such as
includes and excludes) and structured parameters (such as sourceBrowser) support is coming soon.
@@easydoc-end@@*/
public class EasydocMain {
	
	public static void main(String[] args) {
		try {
			EasydocMojo mojo = new EasydocMojo();
			//TODO: setup the log level
			CommandLineMojoUtils.injectMojoProperties(mojo, args);
			mojo.execute();
		}
		catch(MojoExecutionException e) {
			System.err.println("Error: " + e.getMessage());
			System.exit(1);
		}
		catch(CommandLineMojoUtils.ArgException e) {
			System.err.println("Error: " + e.getMessage());
			System.exit(1);
		}
	}

}
