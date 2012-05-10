package com.github.easydoc.semantics.paramrule;

import java.util.EnumSet;

import com.github.easydoc.model.Doc;
import com.github.easydoc.model.Model;
import com.github.easydoc.semantics.NegativeValidationResult;
import com.github.easydoc.semantics.PositiveValidationResult;
import com.github.easydoc.semantics.ValidationResult;
import com.petebevin.markdown.MarkdownProcessor;

/*@@easydoc-start, id=easydoc-advanced, weight=max, format=markdown@@
Advanced
========

This section covers the additional Easydoc stuff. Generally, you don't need
all this to get started with your documentation. However, for advanced development,
some of the things from here you may find useful.  
@@easydoc-end@@*/

/*@@easydoc-start, id=easydoc-advanced-params, belongs=easydoc-advanced, format=markdown@@
Additional parameters
---------------------

The additional doc parameters.  
@@easydoc-end@@*/

/*@@easydoc-start, belongs=easydoc-advanced-params, format=markdown@@

### format ###

Defines the format, in which the doc is written.

Currently supported:

 * html
 * [makrdown](http://daringfireball.net/projects/markdown/syntax)

The value is case-insensitive.  
@@easydoc-end@@*/
public class FormatParamRule implements ParamRule {
	private enum Format {
		HTML,
		MARKDOWN;
	}
	
	private MarkdownProcessor markdown = new MarkdownProcessor();

	@Override
	public boolean requiresValue() {
		return true;
	}

	@Override
	public ValidationResult validate(String value, Doc doc, Model model) {
		for(Format f : EnumSet.allOf(Format.class)) {
			if(f.toString().equalsIgnoreCase(value)) {
				return new PositiveValidationResult(f);
			}
		}
		
		return new NegativeValidationResult("Unknown/unsupported format: '" + value + "'");
	}

	@Override
	public void run(String value, Doc doc, Model model, ValidationResult validationResult) {
		Format format = (Format)validationResult.getData();
		switch(format) {
		case MARKDOWN:
			doc.setText(markdown.markdown(doc.getText()));
			break;
		}
	}

}
