package com.github.easydoc.semantics.directiverule;

import java.util.Set;

import com.github.easydoc.model.Directive;
import com.github.easydoc.model.Doc;
import com.github.easydoc.model.Model;
import com.github.easydoc.semantics.ValidationResult;

public interface DirectiveRule {

	ValidationResult validate(Directive directive, Doc doc, Model model);

	void run(Directive directive, Doc doc, Model model, ValidationResult result);

	Set<String> getRequiredParams();

}
