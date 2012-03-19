package com.github.easydoc.semantics;


public class PositiveValidationResult implements ValidationResult {	
	private static PositiveValidationResult defaultInstance = new PositiveValidationResult();
	
	private Object data;
	
	public PositiveValidationResult() {}
	
	public PositiveValidationResult(Object data) {
		this.data = data;
	}

	public static PositiveValidationResult getDefaultInstance() {
		return defaultInstance;
	}

	@Override
	public boolean isPositive() {
		return true;
	}

	@Override
	public Object getData() {
		return data;
	}
	
	@Override
	public String getMessage() {
		return null;
	}

}
