package com.github.easydoc.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An easydoc documentation entry (the one within 
 * @@easydoc-start@@ and @@easydoc-end@@ tags).
 * This is called a "doc".
 * 
 * @author Viktor Kazakov (weekens@gmail.com)
 *
 */
public class Doc {
	/**
	 * An actual doc text.
	 */
	private StringBuilder text = new StringBuilder();
	
	/**
	 * A raw params of this doc.
	 * 
	 * TODO: for now, we put the parse results in the model.
	 * This is wrong. We should use some other entities for that.
	 * The model shouldn't be in the transient state.
	 */
	private Map<String, String> params = new HashMap<String, String>();
	
	/**
	 * A user-defined id.
	 * This can be used afterwards to reference this doc. 
	 */
	private String id;
	
	/**
	 * A parent doc for this doc (if present).
	 */
	private Doc parent;
	
	/**
	 * A child docs for this doc.
	 */
	private Set<Doc> children = new HashSet<Doc>();
	
	/**
	 * A link to the source file, where this doc originates.
	 */
	private SourceLink sourceLink;

	public String getText() {
		return text.toString();
	}

	public void setText(String text) {
		this.text = new StringBuilder(text);
	}
	
	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public void appendText(String value) {
		text.append(value);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Doc getParent() {
		return parent;
	}

	public void setParent(Doc parent) {
		this.parent = parent;
	}
	
	public Collection<Doc> getChildren() {
		return children;
	}
	
	public void addChild(Doc child) {
		children.add(child);
	}

	public SourceLink getSourceLink() {
		return sourceLink;
	}

	public void setSourceLink(SourceLink sourceLink) {
		this.sourceLink = sourceLink;
	}

	@Override
	public String toString() {
		return String.format("Doc [text=%s, params=%s, id=%s, parent=%s, sourceLink=%s]", text, params, id,
				(parent != null ? parent.getId() : null), sourceLink);
	}
}
