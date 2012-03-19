package com.github.easydoc.semantics.paramrule;

import java.util.List;

import com.github.easydoc.model.Doc;
import com.github.easydoc.model.Model;
import com.github.easydoc.model.criteria.DocSearchCriteria;
import com.github.easydoc.semantics.NegativeValidationResult;
import com.github.easydoc.semantics.PositiveValidationResult;
import com.github.easydoc.semantics.ValidationResult;

public class IdParamRule implements ParamRule {

	@Override
	public boolean requiresValue() {
		return true;
	}
	
	@Override
	public ValidationResult validate(final String value, final Doc doc, Model model) {
		List<Doc> result = model.findDocs(new DocSearchCriteria() {
			@Override
			public boolean satisfies(Doc item) {
				return item != doc && value.equals(item.getParams().get("id"));
			}
		});
		
		if(result.size() > 0) {
			return new NegativeValidationResult(
					String.format(
							"Id '%s' is not unique. Found %d doc(s) with the same id (%s)", 
							value,
							result.size(),
							result.toString()
					)
			);
		}
		
		return PositiveValidationResult.getDefaultInstance();
	}

	@Override
	public void run(String value, Doc doc, Model model, ValidationResult validationResult) {
		doc.setId(value);
	}

}
