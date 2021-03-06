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

/*@@easydoc-start, id=easydoc-format-param, belongs=easydoc-advanced-params, format=markdown@@

### format ###

Defines the format, in which the doc is written.

Currently supported:

 * html
 * [makrdown](http://daringfireball.net/projects/markdown/syntax)

The default format for the docs can be specified by the [defaultFormat](#easydoc-maven-default-format)
parameter and overriden individually for any doc.

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
			doc.setText(markdown.markdown(stripIndentation(doc.getText())));
			break;
		}
	}

	private String stripIndentation(String text) {
		int i = text.indexOf("\n");
		if(i == -1) return text;
		
		//read the indentation pattern from the start of second line
		String indentPattern = "";
		while(++i < text.length()) {
			char c = text.charAt(i);
			if(c == ' ' || c == '\t') {
				indentPattern += c;
			}
			else break;
		}
		if(indentPattern.isEmpty()) return text;
		
		//remove this pattern from all lines (except the first one)
		String ret = text.replaceAll("\n" + indentPattern, "\n");
		return ret;
	}

}
