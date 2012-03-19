grammar Easydoc;

options {
  language = Java;
}

@header {
  package com.github.easydoc;
  
  import com.github.easydoc.model.Doc;
}

@lexer::header {
  package com.github.easydoc;
}

WS: (' ' | '\t' | '\n' | '\r' | '\f')+ ;

CHAR: '\u0000'..'\uFFFE';

easydocStart: '@@easydoc-start@@' ;

easydocEnd: '@@easydoc-end@@' ;

easydocDoc returns [Doc result]
	: { Doc ret = new Doc(); }
	easydocStart 
	(
		CHAR { ret.appendText($CHAR.text); }
		| WS { ret.appendText($WS.text); }
	)* { $result=ret; } 
	easydocEnd ;

document returns [List<Doc> docs]
	: 
	{ $docs = new ArrayList<Doc>(); }
	(
		CHAR 
		| WS 
		| easydocDoc { $docs.add($easydocDoc.result); } 
	)*;