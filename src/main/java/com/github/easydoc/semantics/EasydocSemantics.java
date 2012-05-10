package com.github.easydoc.semantics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.easydoc.model.Doc;
import com.github.easydoc.model.Model;
import com.github.easydoc.model.criteria.DocSearchCriteria;
import com.github.easydoc.semantics.exception.EasydocSemanticException;
import com.github.easydoc.semantics.paramrule.BelongsParamRule;
import com.github.easydoc.semantics.paramrule.FormatParamRule;
import com.github.easydoc.semantics.paramrule.IdParamRule;
import com.github.easydoc.semantics.paramrule.IgnoreParamRule;
import com.github.easydoc.semantics.paramrule.ParamRule;
import com.github.easydoc.semantics.paramrule.WeightParamRule;

public class EasydocSemantics {
	public static class CompilationResult {
		private boolean positive;
		private List<String> errors = new ArrayList<String>();
		private Model model;

		public CompilationResult(boolean positive, Model model) {
			this.positive = positive;
			this.model = model;
		}

		public boolean isPositive() {
			return positive;
		}
		
		public Model getModel() {
			return model;
		}
		
		public void setPositive(boolean value) {
			positive = value;
		}

		public void addError(String message) {
			errors.add(message);
		}
		
		public Collection<String> getErrors() {
			return errors;
		}

		@Override
		public String toString() {
			return String.format("CompilationResult [positive=%s, errors=%s, model=%s]", positive, errors,
					model);
		}
	}
	
	private Map<String, ParamRule> paramRules = new HashMap<String, ParamRule>();
	
	public EasydocSemantics() {
		paramRules.put("id", new IdParamRule());
		paramRules.put("belongs", new BelongsParamRule());
		paramRules.put("weight", new WeightParamRule());
		paramRules.put("ignore", new IgnoreParamRule());
		paramRules.put("format", new FormatParamRule());
	}
	
	public CompilationResult compileModel(Model model) {
		CompilationResult result = new CompilationResult(true, model);
		
		for(Doc doc : model.getDocs()) {
			try {
				applyParameters(doc, model);
			}
			catch(EasydocSemanticException e) {
				result.setPositive(false);
				result.addError(e.getMessage());
			}
		}
		
		List<Doc> rootDocs = model.findDocs(new DocSearchCriteria() {
			@Override
			public boolean satisfies(Doc item) {
				return item.getParent() == null;
			}
		});
		for(Doc doc : rootDocs) {
			sortChildren(doc);
		}
		
		return result;
	}

	private void applyParameters(Doc doc, Model model) throws EasydocSemanticException {
		for(Map.Entry<String, String> param : doc.getParams().entrySet()) {
			ParamRule paramRule = paramRules.get(param.getKey());
			if(paramRule == null) 
				throw new EasydocSemanticException(doc, "Unknown parameter: '" + param.getKey() + "'");
			
			if(paramRule.requiresValue() && param.getValue() == null) 
				throw new EasydocSemanticException(doc, "Parameter '" + param.getKey() + "': value is required");
			
			ValidationResult result = paramRule.validate(param.getValue(), doc, model);
			if(!result.isPositive()) {
				throw new EasydocSemanticException(doc, result); //TODO: return negative result instead
			}
			paramRule.run(param.getValue(), doc, model, result);
		}
	}
	
	private void sortChildren(Doc doc) {
		Collections.sort(doc.getChildren());
		for(Doc child : doc.getChildren()) {
			sortChildren(child);
		}
	}
}
