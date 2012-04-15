package com.github.easydoc.semantics.paramrule;

import com.github.easydoc.model.Doc;
import com.github.easydoc.model.Model;
import com.github.easydoc.semantics.PositiveValidationResult;
import com.github.easydoc.semantics.ValidationResult;

/*@@easydoc-start, belongs=easydoc-parameters@@
<h3>ignore</h3>

With this parameter you can ignore certain characters in doc's text. Those characters
won't get to the resulting HTML.
<br><br>
The value is a string of characters, which should be ignored. To ignore comma (','), escape
it with backslash ('\\,').
<br><br>
Example:<br>
<pre>
#\@\@easydoc-start, ignore=#&\\,\@\@
#
# Lock, stock & 2 smoking barrels
#
#\@\@easydoc-end\@\@
</pre><br>
will result in<br>
<pre>
Lock stock  2 smoking barrels
</pre>
 
@@easydoc-end@@*/
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
