package com.github.easydoc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.easydoc.model.criteria.DocSearchCriteria;

public class Model {
	private List<Doc> docs = new ArrayList<Doc>();
	
	public List<Doc> findDocs(DocSearchCriteria criteria) {
		List<Doc> ret = new ArrayList<Doc>();
		for(Doc doc : docs) {
			if(criteria.satisfies(doc)) {
				ret.add(doc);
			}
		}
		return ret;
	}

	public List<Doc> getDocs() {
		return docs;
	}

	public void addDocs(List<Doc> docs) {
		this.docs.addAll(docs);
	}
	
	public Map<String, Object> toFreemarkerModel() {
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("docs", docs);
		return ret;
	}

}
