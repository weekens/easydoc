package com.github.easydoc.model;

public class DocTextItem implements DocItem {
	
	private final String text;

	public DocTextItem(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}

}
