package com.github.easydoc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.maven.plugin.logging.Log;

import com.github.easydoc.exception.FileActionException;
import com.github.easydoc.model.Doc;

public class ParseDocumentationFileAction implements FileAction {
	private static final String DOCS_KEY = "docs";
	private final Log log;
	private Map<String, Object> model = new HashMap<String, Object>();

	public ParseDocumentationFileAction(Log log) {
		this.log = log;
		model.put(DOCS_KEY, new ArrayList<Doc>());
	}
	
	public Map<String, Object> getModel() {
		return model;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(File file) throws FileActionException {
		try {
			log.debug("File: " + file.getAbsolutePath());
			List<Doc> docs = parseFile(file);
			((List<Doc>)model.get(DOCS_KEY)).addAll(docs);
		} catch (Exception e) {
			throw new FileActionException("Failed to process file " + file.getAbsolutePath(), e);
		} 
	}
	
	private List<Doc> parseFile(File file) throws IOException, RecognitionException {
		EasydocLexer lexer = new EasydocLexer(new ANTLRFileStream(file.getAbsolutePath()));
		EasydocParser parser = new EasydocParser(new CommonTokenStream(lexer));
		List<Doc> docs = parser.document();
		log.debug(docs.toString());
		return docs;
	}

}
