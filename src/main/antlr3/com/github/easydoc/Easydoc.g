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

EQ: '=';

COMMA: ',';

CHAR: '\u0000'..'\uFFFE';

easydocStart returns [Map<String, String> params]
	: { $params = new HashMap<String, String>(); }
	'@@easydoc-start' 
	(WS* COMMA WS* easydocParam { $params.put($easydocParam.name, $easydocParam.value); } )* 
	'@@' 
	;

easydocParam returns [String name, String value]
	: paramName { $name = $paramName.text; } 
	(WS* EQ WS* paramValue { $value = $paramValue.text; } );

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
		simpleText { ret.appendText($simpleText.result); } 
	)?
	easydocEnd
	{ $result=ret; } 
	;
	
simpleText returns [String result]
	: { StringBuffer sb = new StringBuffer(); }
	(
		CHAR { sb.append($CHAR.text); } 
		| WS { sb.append($WS.text); } 
		| EQ { sb.append($EQ.text); } 
		| COMMA { sb.append($COMMA.text); }
		| '@@' { sb.append("@@"); }
	)+ { $result = sb.toString(); } ;

document returns [List<Doc> docs]
	: 
	{ $docs = new ArrayList<Doc>(); }
	(
		simpleText 
		| easydocDoc { $docs.add($easydocDoc.result); } 
	)*;