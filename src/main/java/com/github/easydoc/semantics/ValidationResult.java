package com.github.easydoc.semantics;


public interface ValidationResult {

	boolean isPositive();

	Object getData();

	String getMessage();

}
