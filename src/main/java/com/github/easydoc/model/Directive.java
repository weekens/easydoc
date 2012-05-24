package com.github.easydoc.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Directive inside the Doc
 *
 */
public class Directive implements DocItem {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Map<String, String> params = new HashMap<String, String>();
	private String text;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, String> getParams() {
		return params;
	}
	
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return String.format("Directive [name=%s, params=%s, text=%s]", name, params, text);
	}

}
