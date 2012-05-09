package com.github.easydoc.semantics.methods;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.easydoc.model.Doc;

import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/*@@easydoc-start, id=easydoc-index, belongs=easydoc-usage@@
 <h2>Index generation</h2>
 
 By default, easydoc will generate index (contents) for your documentation
 containing all the docs with ids, having a string less than 50 characters 
 in their first line.
 <br><br>
 Index generation can be disabled by setting parameter <b>generateIndex</b>=<b>false</b>. 
 @@easydoc-end@@*/
public class IndexTextMethod implements TemplateMethodModelEx {

	private static final int maxIndexTextLength = 50;

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arguments) throws TemplateModelException {
		if(arguments.size() != 1) throw new TemplateModelException("indexText: invalid arguments: " + arguments);
		Object arg1 = arguments.get(0);
		
		try {
			Doc doc = (Doc)((StringModel)arg1).getWrappedObject();
			String text = doc.getText().trim();
			int eolInd = text.indexOf("\n");
			String ret;
			if(eolInd > 0) {
				ret = text.substring(0, eolInd);
			}
			else {
				ret = text;
			}
			
			ret = extractSimpleString(ret.trim());
			
			if(ret.length() <= maxIndexTextLength) return ret;
			
			return null;
		}
		catch(ClassCastException e) {
			throw new TemplateModelException("indexText: invalid arguments: " + arguments, e);
		}
	}
	
	private String extractSimpleString(String htmlString) {
		Pattern pattern = Pattern.compile("^<h[1-5]>(.+)</h[1-5]>");
		Matcher matcher = pattern.matcher(htmlString);
		if(matcher.find()) {
			return matcher.group(1);
		}
		return htmlString;
	}

}
