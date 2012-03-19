package com.github.easydoc.model;

public class Doc {
	private StringBuilder text = new StringBuilder();

	public String getText() {
		return text.toString();
	}

	public void setText(String text) {
		this.text = new StringBuilder(text);
	}
	
	public void appendText(String value) {
		text.append(value);
	}

	@Override
	public String toString() {
		return String.format("Doc [text=%s]", text);
	}
}
