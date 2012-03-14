package com.github.easydoc.exception;

public class FileActionException extends Exception {
	private static final long serialVersionUID = -5521679988973977693L;
	
	public FileActionException(String msg) {
		super(msg);
	}
	
	public FileActionException(String msg, Throwable t) {
		super(msg, t);
	}

}
