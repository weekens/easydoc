package com.github.easydoc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.apache.maven.plugin.logging.Log;

import com.github.easydoc.exception.FileActionException;
import com.github.easydoc.model.Doc;
import com.github.easydoc.model.Model;

public class ParseDocumentationFileAction implements FileAction {
	private final Log log;
	private Model model;

	public ParseDocumentationFileAction(Model model, Log log) {
		this.model = model;
		this.log = log;
	}
	
	@Override
	public void run(File file) throws FileActionException {
		try {
			log.debug("File: " + file.getAbsolutePath());
			List<Doc> docs = parseFile(file);
			if(docs.size() > 0) {
				log.info(String.format("%d docs found in file %s", docs.size(), file.getAbsolutePath()));
			}
			log.debug("Resulting docs: " + docs);
			model.addDocs(docs);
		} catch (Exception e) {
			throw new FileActionException("Failed to process file " + file.getAbsolutePath(), e);
		} 
	}
	
	private List<Doc> parseFile(File file) throws IOException, RecognitionException {
		EasydocParser parser = new EasydocParser(file);
		List<Doc> docs = parser.document();
		log.debug(docs.toString());
		return docs;
	}

}
