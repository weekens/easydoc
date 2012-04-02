package com.github.easydoc.semantics.paramrule;

import com.github.easydoc.model.Doc;
import com.github.easydoc.model.Model;
import com.github.easydoc.semantics.PositiveValidationResult;
import com.github.easydoc.semantics.ValidationResult;

public class IgnoreParamRule implements ParamRule {

	@Override
	public boolean requiresValue() {
		return true;
	}

	@Override
	public ValidationResult validate(String value, Doc doc, Model model) {
		return PositiveValidationResult.getDefaultInstance();
	}

	@Override
	public void run(String value, Doc doc, Model model, ValidationResult validationResult) {
		for(int i = 0; i < value.length(); i++) {
			doc.setText(
					doc.getText().replace("" + value.charAt(i), "")
			);
		}
	}

}
