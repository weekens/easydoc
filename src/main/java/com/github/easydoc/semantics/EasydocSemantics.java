package com.github.easydoc.semantics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import com.github.easydoc.model.Directive;
import com.github.easydoc.model.Doc;
import com.github.easydoc.model.Model;
import com.github.easydoc.model.criteria.DocSearchCriteria;
import com.github.easydoc.semantics.directiverule.DirectiveRule;
import com.github.easydoc.semantics.directiverule.IncludeDirectiveRule;
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
	
	private enum Param {
		ID("id", new IdParamRule()),
		BELONGS("belongs", new BelongsParamRule()),
		WEIGHT("weight", new WeightParamRule()),
		IGNORE("ignore", new IgnoreParamRule()),
		FORMAT("format", new FormatParamRule());
		
		private final String name;
		private final ParamRule rule;
		private String defaultValue;

		private Param(String name, ParamRule paramRule) {
			this.name = name;
			this.rule = paramRule;
		}
		
		public static Param getByName(String name) {
			for(Param e : EnumSet.allOf(Param.class)) {
				if(e.name.equals(name)) {
					return e;
				}
			}
			return null;
		}
		
		public static EnumSet<Param> getParamsWithDefaultValues() {
			EnumSet<Param> ret = EnumSet.noneOf(Param.class);
			for(Param e : EnumSet.allOf(Param.class)) {
				if(e.getDefaultValue() != null) {
					ret.add(e);
				}
			}
			return ret;
		}

		public String getDefaultValue() {
			return defaultValue;
		}

		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

		public String getName() {
			return name;
		}

		public ParamRule getRule() {
			return rule;
		}
	}
	
	private enum DirectiveDef {
		INCLUDE("include", new IncludeDirectiveRule());
		
		private final String name;
		private final DirectiveRule rule;

		private DirectiveDef(String name, DirectiveRule rule) {
			this.name = name;
			this.rule = rule;
		}
		
		public static DirectiveDef getByName(String name) {
			for(DirectiveDef d : EnumSet.allOf(DirectiveDef.class)) {
				if(d.name.equals(name)) {
					return d;
				}
			}
			return null;
		}
		
		public DirectiveRule getRule() {
			return rule;
		}
	}
	
	public void setDefaultFormat(String format) {
		Param.FORMAT.setDefaultValue(format);
	}
	
	public CompilationResult compileModel(Model model) {
		CompilationResult result = new CompilationResult(true, model);
		EnumSet<Param> defaultValueParams = Param.getParamsWithDefaultValues();
		
		for(Doc doc : model.getDocs()) {
			try {
				//try to apply default values
				for(Param p : defaultValueParams) {
					if(!doc.getParams().containsKey(p.getName())) {
						doc.getParams().put(p.getName(), p.getDefaultValue());
					}
				}
				applyDirectives(doc, model);
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

	private void applyDirectives(Doc doc, Model model) throws EasydocSemanticException {
		for(Directive directive : doc.getDirectives()) {
			DirectiveDef ddef = DirectiveDef.getByName(directive.getName());
			if(ddef == null) 
				throw new EasydocSemanticException(doc, "Unknown directive: '" + directive.getName() + "'");
			
			DirectiveRule drule = ddef.getRule();
			for(String rparam : drule.getRequiredParams()) {
				if(!directive.getParams().containsKey(rparam)) {
					throw new EasydocSemanticException(doc, directive, "Required parameter '" + rparam + "' is absent.");
				}
			}
			
			//TODO: check for undefined params
			
			ValidationResult result = drule.validate(directive, doc, model);
			if(!result.isPositive()) {
				throw new EasydocSemanticException(doc, result); //TODO: return negative result instead
			}
			drule.run(directive, doc, model, result);
		}
	}

	private void applyParameters(Doc doc, Model model) throws EasydocSemanticException {
		for(Map.Entry<String, String> paramEntry : doc.getParams().entrySet()) {
			Param param = Param.getByName(paramEntry.getKey());
			if(param == null) 
				throw new EasydocSemanticException(doc, "Unknown parameter: '" + paramEntry.getKey() + "'");
			
			ParamRule paramRule = param.getRule();
			
			if(paramRule.requiresValue() && paramEntry.getValue() == null) 
				throw new EasydocSemanticException(doc, "Parameter '" + paramEntry.getKey() + "': value is required");
			
			ValidationResult result = paramRule.validate(paramEntry.getValue(), doc, model);
			if(!result.isPositive()) {
				throw new EasydocSemanticException(doc, result); //TODO: return negative result instead
			}
			paramRule.run(paramEntry.getValue(), doc, model, result);
		}
	}
	
	private void sortChildren(Doc doc) {
		Collections.sort(doc.getChildren());
		for(Doc child : doc.getChildren()) {
			sortChildren(child);
		}
	}
}
