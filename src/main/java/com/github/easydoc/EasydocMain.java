package com.github.easydoc;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

public class EasydocMain {
	
	private static Log log;
	
	public static void main(String[] args) {
		try {
			EasydocMojo mojo = new EasydocMojo();
			mojo.setLog(log);
			CommandLineUtils.injectMojoProperties(mojo, args);
			mojo.execute();
		}
		catch(MojoExecutionException e) {
			log.error("Error: " + e.getMessage());
			System.exit(1);
		}
		catch(CommandLineUtils.ArgException e) {
			log.error("Error: " + e.getMessage());
			System.exit(1);
		}
	}

}
