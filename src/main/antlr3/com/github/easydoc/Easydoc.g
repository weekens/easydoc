grammar Easydoc;

options {
  language = Java;
}

@header {
  package com.github.easydoc;
}

@lexer::header {
  package com.github.easydoc;
}

WS: (' ' | '\t' | '\n' | '\r' | '\f')+ ;

CHAR: '\u0000'..'\uFFFE';

easydocStart: '@@' WS* 'easydoc-start' WS* '@@' ;

easydocEnd: '@@' WS* 'easydoc-end' WS* '@@' ;

easydocDoc returns [String result]
	: { StringBuilder ret = new StringBuilder(); }
	easydocStart 
	(
		CHAR { ret.append($CHAR.text); }
		| WS { ret.append($WS.text); }
	)* { $result=ret.toString(); } 
	easydocEnd ;

document returns [List<String> docs]
	: 
	{ $docs = new ArrayList<String>(); }
	(
		CHAR 
		| WS 
		| easydocDoc { $docs.add($easydocDoc.result); } 
	)*;