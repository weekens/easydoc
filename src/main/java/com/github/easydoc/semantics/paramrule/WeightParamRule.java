package com.github.easydoc.semantics.paramrule;

import com.github.easydoc.model.Doc;
import com.github.easydoc.model.Model;
import com.github.easydoc.semantics.NegativeValidationResult;
import com.github.easydoc.semantics.PositiveValidationResult;
import com.github.easydoc.semantics.ValidationResult;

/*@@easydoc-start, belongs=easydoc-parameters@@
<h3>weight</h3>

Defines the weight of the doc. The greater the weight, the lower the doc goes on the resulting page
amongst the siblings.
<br><br>
The value is an integer, which may be positive, negative or zero (default). There are also 2 special
keywords for the value: <i>min</i> and <i>max</i>, which define minimal and maximal weight respectively.
<br>
If the weight of two sibling docs is the same, they go in occurence order (which is default).
 
@@easydoc-end@@*/
public class WeightParamRule implements ParamRule {

	@Override
	public boolean requiresValue() {
		return true;
	}

	@Override
	public ValidationResult validate(String value, Doc doc, Model model) {
		try {
			if(value.toLowerCase().equals("min")) {
				return new PositiveValidationResult(Integer.MIN_VALUE);
			}
			else if(value.toLowerCase().equals("max")) {
				return new PositiveValidationResult(Integer.MAX_VALUE);
			}
			else {
				return new PositiveValidationResult(Integer.parseInt(value));
			}
		}
		catch(NumberFormatException e) {
			return new NegativeValidationResult("Invalid value for 'weight' parameter: " + e.getMessage());
		}
	}

	@Override
	public void run(String value, Doc doc, Model model, ValidationResult validationResult) {
		doc.setWeight((Integer)validationResult.getData());
	}

}
