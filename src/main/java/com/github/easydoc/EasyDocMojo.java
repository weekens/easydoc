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
 * Goal which touches a timestamp file.
 *
 * @goal generate
 * 
 * @phase process-sources
 * 
 */
public class EasyDocMojo extends AbstractMojo {
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

	private List<String> excludes = new ArrayList<String>();

	private AntPathMatcher pathMatcher = new AntPathMatcher();

	public EasyDocMojo() {
		excludes.add("**/.*"); //skip all entries starting with '.'
	}

	public void execute() throws MojoExecutionException {
		try {
			getLog().info("input directory = " + inputDirectory.getAbsolutePath());

			Configuration freemarkerCfg = new Configuration();
			freemarkerCfg.setObjectWrapper(new DefaultObjectWrapper());
			freemarkerCfg.setTemplateLoader(new ClassTemplateLoader(getClass(), "/templates"));
			
			Model model = new Model();

			ParseDocumentationFileAction fileAction = new ParseDocumentationFileAction(model, getLog());
			//try to run this action for pom.xml file
			File pomXml = new File(inputDirectory.getParentFile(), "pom.xml");
			if(pomXml.isFile()) {
				fileAction.run(pomXml);
			}
			//and also recursively in inputDirectory
			recurseDirectory(inputDirectory, fileAction);
			
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

			boolean skip = false;
			for(String exclude : excludes) {
				if(pathMatcher.match("/" + exclude, file.getAbsolutePath())) {
					skip = true;
				}
			}
			if(skip) continue;

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
