package com.github.easydoc.exception;

public class EasydocFatalException extends RuntimeException {
	private static final long serialVersionUID = 456380749976787888L;

	public EasydocFatalException(Exception e) {
		super("If you see this error, please send the bug report to kvs16@yandex.ru" +
				" or submit a bug at https://github.com/weekens/easydoc/issues/new." +
				" Also, if you use Maven, it would be nice if you run it with -X switch" +
				" and attach the output to the bug report (this will help to fix the issue faster)." +
				" Sorry for inconvenience! Thanks for using Easydoc :)", e);
	}

}
 