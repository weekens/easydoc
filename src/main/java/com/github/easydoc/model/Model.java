package com.github.easydoc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.easydoc.model.criteria.DocSearchCriteria;

public class Model implements Serializable {
	private static final long serialVersionUID = 1L;
	
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
		ret.put("doctree", docTree());
		return ret;
	}

	private Collection<Doc> docTree() {
		List<Doc> ret = new ArrayList<Doc>();
		
		// Add all root docs to this list. Other docs can be traversed with getChildren()
		for(Doc doc : docs) {
			if(doc.getParent() == null) { //root doc
				ret.add(doc);
			}
		}
		
		Collections.sort(ret);
		
		return ret;
	}

	@Override
	public String toString() {
		return String.format("Model [docs=%s]", docs);
	}

}
