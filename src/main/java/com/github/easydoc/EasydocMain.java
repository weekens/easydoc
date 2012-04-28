package com.github.easydoc;

import org.apache.maven.plugin.MojoExecutionException;

public class EasydocMain {
	
	public static void main(String[] args) {
		try {
			EasydocMojo mojo = new EasydocMojo();
			//TODO: setup the log level
			CommandLineUtils.injectMojoProperties(mojo, args);
			mojo.execute();
		}
		catch(MojoExecutionException e) {
			System.err.println("Error: " + e.getMessage());
			System.exit(1);
		}
		catch(CommandLineUtils.ArgException e) {
			System.err.println("Error: " + e.getMessage());
			System.exit(1);
		}
	}

}
