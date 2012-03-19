package com.github.easydoc.semantics.paramrule;

import com.github.easydoc.model.Doc;
import com.github.easydoc.model.Model;
import com.github.easydoc.semantics.ValidationResult;

public interface ParamRule {
	boolean requiresValue();
	
	ValidationResult validate(String value, Doc doc, Model model);

	void run(String value, Doc doc, Model model, ValidationResult validationResult);
}
