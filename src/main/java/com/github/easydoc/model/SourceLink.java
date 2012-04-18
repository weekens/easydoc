package com.github.easydoc.model;

import java.io.File;

/**
 * A link to the source, where the Doc originates.
 *
 */
public class SourceLink {
	private File file;
	private int startLine;
	private final int endLine;

	public SourceLink(File file, int startLine, int endLine) {
		this.file = file;
		this.startLine = startLine;
		this.endLine = endLine;
	}
	
	public SourceLink(String fileName, int startLine, int endLine) {
		this(new File(fileName), startLine, endLine);
	}

	public File getFile() {
		return file;
	}

	public int getStartLine() {
		return startLine;
	}
	
	public int getEndLine() {
		return endLine;
	}

	@Override
	public String toString() {
		return String.format("SourceLink [file=%s, line=%s]", file, startLine);
	}
	
}
