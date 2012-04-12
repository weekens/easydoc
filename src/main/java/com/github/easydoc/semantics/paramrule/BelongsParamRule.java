package com.github.easydoc.semantics.paramrule;

import java.util.List;

import com.github.easydoc.model.Doc;
import com.github.easydoc.model.Model;
import com.github.easydoc.model.criteria.DocSearchCriteria;
import com.github.easydoc.semantics.NegativeValidationResult;
import com.github.easydoc.semantics.PositiveValidationResult;
import com.github.easydoc.semantics.ValidationResult;

/*@@easydoc-start, belongs=easydoc-parameters@@
<h3>belongs</h3>

This parameter lets you join the doc to another doc as a child. Thus, this doc's content
will be generated inside the parent's HTML.
<br><br>
The value is a parent doc's id. 
<br><br>
Requirements:
<br><br>
The parent doc with the specified id should be there. If there is no such doc, easydoc
will fail.
 
@@easydoc-end@@*/
public class BelongsParamRule implements ParamRule {

	@Override
	public boolean requiresValue() {
		return true;
	}
	
	@Override
	public ValidationResult validate(final String value, Doc doc, Model model) {
		String id = doc.getParams().get("id");
		if(id != null && id.equals(value)) {
			return new NegativeValidationResult(
					String.format(
							"The doc cannot belong to itself (id and belongs are the same, '%s')", 
							value
					)
			);
		}
		
		List<Doc> result = model.findDocs(new DocSearchCriteria() {
			@Override
			public boolean satisfies(Doc item) {
				return item.getParams().containsKey("id") && value.equals(item.getParams().get("id"));
			}
		});
		if(result.size() > 1) {
			return new NegativeValidationResult(
					String.format(
							"The doc belongs to doc '%s' which is not unique (%d occurences found: %s).", 
							value,
							result.size(),
							result.toString()
					)
			);
		}
		else if(result.size() < 1) {
			return new NegativeValidationResult(
					String.format("The doc belongs to doc '%s', which has not been found.", value)
			);
		}
		else {
			return new PositiveValidationResult(result.get(0));
		}
	}

	@Override
	public void run(final String value, Doc doc, Model model, ValidationResult validationResult) {
		Doc parent = (Doc)validationResult.getData();
		doc.setParent(parent);
		parent.addChild(doc);
	}

}
