package com.github.easydoc;

import java.io.File;
import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import com.github.easydoc.exception.FileActionException;

public class ParseDocumentationFileAction implements FileAction {

	@Override
	public void run(File file) throws FileActionException {
		try {
			parseFile(file);
		} catch (Exception e) {
			throw new FileActionException("Failed to process file " + file.getAbsolutePath(), e);
		} 
	}
	
	private void parseFile(File file) throws IOException, RecognitionException {
		EasydocLexer lexer = new EasydocLexer(new ANTLRFileStream(file.getAbsolutePath()));
		EasydocParser parser = new EasydocParser(new CommonTokenStream(lexer));
		System.out.println(parser.document().toString());
	}

}
