package com.github.easydoc.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Directive inside the Doc
 *
 */
public class Directive {
	private String name;
	private Map<String, String> params = new HashMap<String, String>();
	private int line; //directive's line, relative to the doc
	private int column; //directive's column, relative to the doc
	
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
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
	
	public int computePosition(String text) {		
		String[] split = text.split(System.getProperty("line.separator"));
		if(split.length < line) {
			throw new IllegalStateException(
					"Directive is positioned at line " + line + 
					", which is more than actual number of lines the given text fragment: " + text);
		}
		
		int index = 0;
		for(int i = 0; i < line; i++) {
			index += split[i].length() + 1;
		}
		index += column;
		
		return index;
	}
	
	@Override
	public String toString() {
		return String
				.format("Directive [name=%s, params=%s, line=%s, column=%s]", name, params, line, column);
	}

}
