package com.github.easydoc.exception;

public class EasydocFatalException extends RuntimeException {
	private static final long serialVersionUID = 456380749976787888L;

	public EasydocFatalException(Exception e) {
		super("If you see this error, please send the bug report to kvs16@yandex.ru. Sorry for inconvenience!", e);
	}

}
