package com.github.easydoc.semantics.exception;

import com.github.easydoc.model.Directive;
import com.github.easydoc.model.Doc;
import com.github.easydoc.semantics.ValidationResult;

public class EasydocSemanticException extends Exception {
	private static final long serialVersionUID = -3893215981244910230L;
	
	public EasydocSemanticException(Doc doc, String msg) {
		super("" + doc + ": " + msg); //TODO: output doc in more human-readable format
	}

	public EasydocSemanticException(Doc doc, ValidationResult result) {
		this(doc, result.getMessage());
	}

	public EasydocSemanticException(Doc doc, Directive directive, String msg) {
		this(doc, "directive " + directive + ": " + msg);
	}
}
