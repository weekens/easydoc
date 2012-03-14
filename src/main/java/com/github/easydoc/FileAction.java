package com.github.easydoc;

import java.io.File;

import com.github.easydoc.exception.FileActionException;

public interface FileAction {
	void run(File file) throws FileActionException;
}
