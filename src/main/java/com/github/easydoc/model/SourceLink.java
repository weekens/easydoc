package com.github.easydoc.model;

import java.io.File;

/**
 * A link to the source, where the Doc originates.
 *
 */
public class SourceLink {
	private File file;
	private int line;

	public SourceLink(File file, int line) {
		this.file = file;
		this.line = line;
	}
	
	public SourceLink(String fileName, int line) {
		this(new File(fileName), line);
	}

	public File getFile() {
		return file;
	}

	public int getLine() {
		return line;
	}

	@Override
	public String toString() {
		return String.format("SourceLink [file=%s, line=%s]", file, line);
	}
	
}
