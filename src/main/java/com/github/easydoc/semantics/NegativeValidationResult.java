package com.github.easydoc.semantics;

public class NegativeValidationResult implements ValidationResult {

	private final String message;

	public NegativeValidationResult(String message) {
		this.message = message;
	}

	@Override
	public boolean isPositive() {
		return false;
	}

	@Override
	public Object getData() {
		return null;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
