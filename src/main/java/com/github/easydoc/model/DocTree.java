package com.github.easydoc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.github.easydoc.model.criteria.DocSearchCriteria;

public class DocTree implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<Doc> roots = new LinkedList<Doc>();
	
	public void addRoots(Collection<Doc> docs) {
		roots.addAll(docs);
	}
	
	public void addRoot(Doc doc) {
		roots.add(doc);
	}
	
	public Collection<Doc> getRoots() {
		return roots;
	}
	
	public void removeRoot(Doc doc) {
		roots.remove(doc);
	}
	
	public List<Doc> find(DocSearchCriteria criteria) {
		return find(roots, criteria);
	}

	private List<Doc> find(List<Doc> docs, DocSearchCriteria criteria) {
		List<Doc> ret = new ArrayList<Doc>();
		for(Doc doc : docs) {
			if(criteria.satisfies(doc)) {
				ret.add(doc);
			}
			ret.addAll(find(doc.getChildren(), criteria));
		}
		return ret;
	}
	
	public boolean isEmpty() {
		return roots.isEmpty();
	}

	@Override
	public String toString() {
		return String.format("DocTree [\n%s\n]", printTree(new StringBuilder(), "", roots).toString());
	}
	
	private StringBuilder printTree(StringBuilder sb, String indent, List<Doc> docs) {
		for(Doc doc : docs) {
			sb.append(indent).append(doc.toShortString()).append("\n");
			sb = printTree(sb, indent + "\t", doc.getChildren());
		}
		
		return sb;
	}

	public void sort() {
		sort(roots);
	}
	
	private void sort(List<Doc> docs) {
		Collections.sort(docs);
		for(Doc doc : docs) {
			sort(doc.getChildren());
		}
	}
}
