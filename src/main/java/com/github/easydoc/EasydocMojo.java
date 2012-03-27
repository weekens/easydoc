package com.github.easydoc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.springframework.util.AntPathMatcher;

import com.github.easydoc.exception.FileActionException;
import com.github.easydoc.model.Model;
import com.github.easydoc.semantics.EasydocSemantics;
import com.github.easydoc.semantics.EasydocSemantics.CompilationResult;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * Scan for all files and generate documentation for project.
 *
 * @goal generate
 * 
 * @phase process-sources
 * 
 */
public class EasydocMojo extends AbstractMojo {
	/**
	 * Location of the file.
	 * @parameter expression="${project.build.directory}/easydoc"
	 * @required
	 */
	private File outputDirectory;

	/**
	 * Input directory.
	 * @parameter expression="${basedir}/src"
	 * @required
	 */
	private File inputDirectory;

	/**
	 * Files or directories that should be excluded from the scan.
	 * @parameter
	 */
	private List<String> excludes = new ArrayList<String>();
	
	/**
	 * Files or directories that should only be scanned. All the
	 * other files will be omited.
	 * @parameter
	 */
	private List<String> includes;

	private AntPathMatcher pathMatcher = new AntPathMatcher();

	public EasydocMojo() {
	}

	public void execute() throws MojoExecutionException {
		excludes.add("**/.*"); //skip all entries starting with '.'
		
		try {
			getLog().debug("inputDirectory = " + inputDirectory.getAbsolutePath());
			if(!inputDirectory.exists()) {
				getLog().debug("Input directory does not exist. Skipping execution.");
				return;
			}

			Configuration freemarkerCfg = new Configuration();
			freemarkerCfg.setObjectWrapper(new DefaultObjectWrapper());
			freemarkerCfg.setTemplateLoader(new ClassTemplateLoader(getClass(), "/templates"));
			
			Model model = new Model();

			ParseDocumentationFileAction fileAction = new ParseDocumentationFileAction(model, getLog());
			//try to run this action for pom.xml file
			File pomXml = new File(inputDirectory.getParentFile(), "pom.xml");
			if(pomXml.isFile() && !skipCheck(pomXml)) {
				fileAction.run(pomXml);
			}
			//and also recursively in inputDirectory
			recurseDirectory(inputDirectory, fileAction);
			
			if(model.getDocs().isEmpty()) {
				getLog().debug("No docs were found. Skipping execution.");
				return;
			}
			
			//compile the model
			EasydocSemantics semantics = new EasydocSemantics();
			CompilationResult compilationResult = semantics.compileModel(model);
			if(compilationResult.isPositive()) {
				Template template = freemarkerCfg.getTemplate("page.ftl");
				outputDirectory.mkdirs();
				BufferedWriter out = new BufferedWriter(
						new FileWriter(new File(outputDirectory, "index.html"))
						);
				try {
					template.process(
							compilationResult.getModel().toFreemarkerModel(), 
							out
					);
				}
				finally {
					out.close();
				}
			}
			else { //negative compilation result
				for(String error : compilationResult.getErrors()) {
					getLog().error(error);
				}
				throw new MojoExecutionException("Failed to compile documentation. See the error log.");
			}
		}
		catch(Exception e) {
			throw new MojoExecutionException("Execution failed", e);
		}
	}

	private void recurseDirectory(File dir, FileAction action) {
		for(File file : dir.listFiles()) {
			if(skipCheck(file)) continue;

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
	
	private boolean skipCheck(File file) {
		boolean skip = false;
		if(includes != null && !file.isDirectory()) {
			skip = true;
			for(String include : includes) {
				skip &= !pathMatcher.match("/" + include, file.getAbsolutePath());
			}
		}
		if(skip) return true;
		
		for(String exclude : excludes) {
			if(pathMatcher.match("/" + exclude, file.getAbsolutePath())) {
				return true;
			}
		}
		
		return false;
	}
}
