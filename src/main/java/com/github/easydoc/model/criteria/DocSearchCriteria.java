package com.github.easydoc.model.criteria;

import com.github.easydoc.model.Doc;

public interface DocSearchCriteria {

	boolean satisfies(Doc item);

}
