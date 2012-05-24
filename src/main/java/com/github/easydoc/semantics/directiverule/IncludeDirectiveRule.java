package com.github.easydoc.semantics.directiverule;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.github.easydoc.model.Directive;
import com.github.easydoc.model.Doc;
import com.github.easydoc.model.Model;
import com.github.easydoc.model.criteria.DocSearchCriteria;
import com.github.easydoc.semantics.NegativeValidationResult;
import com.github.easydoc.semantics.PositiveValidationResult;
import com.github.easydoc.semantics.ValidationResult;

/*@@easydoc-start, belongs=easydoc-directives, format=markdown@@
 
### include ###

Copies a specified doc's text verbatim into this directive's location. This does not
prevent the included doc from normal generation into a place where it belongs.

Parameters:

 * **id** - the id of the doc to include (required)

Example:

	\@\@easydoc-start, id=to-include\@\@ I'm a doc to include. \@\@easydoc-end\@\@
	
	\@\@easydoc-start\@\@
	I'm a normal doc. \@\@include, id=to-include\@\@
	\@\@easydoc-end\@\@
	
	Result:
	
	I'm a doc to include.
	
	I'm a normal doc.  I'm a doc to include. 

@@easydoc-end@@*/
public class IncludeDirectiveRule implements DirectiveRule {
	
	@Override
	public Set<String> getRequiredParams() {
		return Collections.singleton("id");
	}

	@Override
	public ValidationResult validate(Directive directive, Doc doc, Model model) {
		final String idValue = directive.getParams().get("id");
		if(doc.getParams().containsKey("id") && idValue.equals(doc.getParams().get("id"))) {
			return new NegativeValidationResult(
					String.format(
							"The doc cannot include itself ('include' directive id and doc id are the same, '%s')", 
							idValue
					)
			);
		}
		
		List<Doc> result = model.findDocs(new DocSearchCriteria() {
			@Override
			public boolean satisfies(Doc item) {
				return item.getParams().containsKey("id") && idValue.equals(item.getParams().get("id"));
			}
		});
		
		//We know that doc ids are unique at this point
		if(result.size() < 1) {
			return new NegativeValidationResult(
					String.format("Trying to include the doc '%s', which has not been found.", idValue)
			);
		}
		else {
			return new PositiveValidationResult(result.get(0));
		}	
	}
	
	@Override
	public void run(Directive directive, Doc doc, Model model, ValidationResult result) {
		directive.setText(((Doc)result.getData()).getText());
	}

}
