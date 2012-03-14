package com.github.easydoc;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import com.github.easydoc.exception.FileActionException;

/**
 * Goal which touches a timestamp file.
 *
 * @goal generate
 * 
 * @phase process-sources
 */
public class EasyDocMojo extends AbstractMojo {
    /**
     * Location of the file.
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File outputDirectory;
    
    /**
     * Input directory.
     * @parameter expression="${basedir}/src"
     * @required
     */
    private File inputDirectory;

    public void execute() throws MojoExecutionException {
        getLog().info("input directory = " + inputDirectory.getAbsolutePath());
        
        recurseDirectory(inputDirectory, new ParseDocumentationFileAction() {
			public void run(File file) throws FileActionException {
				getLog().info("File: " + file.getAbsolutePath());
				super.run(file);
			}
		});
    }
    
    private void recurseDirectory(File dir, FileAction action) {
    	for(File file : dir.listFiles()) {
    		if(!file.canRead()) {
    			getLog().warn("File " + file.getAbsolutePath() + " is not readable.");
    			continue;
    		}
    		
    		if(file.isFile()) {
    			try {
    				action.run(file);
    			}
    			catch(FileActionException e) {
    				getLog().warn("Error", e);
    			}
    		}
    		else if(file.isDirectory()) {
    			recurseDirectory(file, action);
    		}
    	}
    }
}
