grammar Easydoc;

options {
  language = Java;
}

@header {
  package com.github.easydoc;
  
  import java.util.Map;
  import java.util.HashMap;
  
  import com.github.easydoc.model.Doc;
}

@lexer::header {
  package com.github.easydoc;
}

WS: (' ' | '\t' | '\n' | '\r' | '\f')+ ;

CHAR: '\u0000'..'\uFFFE';

easydocStart returns [Map<String, String> params]
	: { $params = new HashMap<String, String>(); }
	'@@easydoc-start' 
	(WS* ',' WS* easydocParam { $params.put($easydocParam.name, $easydocParam.value); } )* 
	'@@' 
	;

easydocParam returns [String name, String value]
	: paramName { $name = $paramName.text; } 
	(WS* '=' WS* paramValue { $value = $paramValue.text; } );

paramName returns [String text] 
	: { StringBuilder sb = new StringBuilder(); }
	(CHAR { sb.append($CHAR.text); } )+ 
	{ $text = sb.toString(); } ;

paramValue returns [String text]
	: { StringBuilder sb = new StringBuilder(); } 
	(CHAR { sb.append($CHAR.text); } )+ { $text = sb.toString(); } ;

easydocEnd: '@@easydoc-end@@' ;

easydocDoc returns [Doc result]
	: { Doc ret = new Doc(); }
	easydocStart { ret.setParams($easydocStart.params); } 
	(
		CHAR { ret.appendText($CHAR.text); }
		| WS { ret.appendText($WS.text); }
	)* 
	easydocEnd
	{ $result=ret; } 
	;

document returns [List<Doc> docs]
	: 
	{ $docs = new ArrayList<Doc>(); }
	(
		CHAR 
		| WS 
		| easydocDoc { $docs.add($easydocDoc.result); } 
	)*;