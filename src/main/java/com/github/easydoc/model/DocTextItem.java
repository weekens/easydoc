package com.github.easydoc.model;

public class DocTextItem implements DocItem {
	private static final long serialVersionUID = 1L;
	
	private final String text;

	public DocTextItem(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return getText();
	}

}
