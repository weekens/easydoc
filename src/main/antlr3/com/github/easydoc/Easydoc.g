grammar Easydoc;

options {
  language = Java;
}

@header {
  package com.github.easydoc;
  
  import java.util.Map;
  import java.util.HashMap;
  import java.io.File;
  
  import com.github.easydoc.model.Doc;
  import com.github.easydoc.model.SourceLink;
}

@members {
  private File file;

  public EasydocParser(File file) throws java.io.IOException {
  	super(
  		new CommonTokenStream(
  			new EasydocLexer(
  				new ANTLRFileStream(
  					file.getAbsolutePath()
  				)
  			)
  		)
  	);
  	this.file = file;
  }
}

@lexer::header {
  package com.github.easydoc;
}

WS: (' ' | '\t' | '\n' | '\r' | '\f')+ ;

EQ: '=';

COMMA: ',';

ED_START: '@@easydoc-start';

ED_END: '@@easydoc-end@@';

CHAR: '\u0000'..'\uFFFE';

easydocStart returns [Map<String, String> params, int line]
	: { $params = new HashMap<String, String>(); }
	ED_START { $line =  $ED_START.getLine(); }
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
	(
		CHAR { sb.append($CHAR.text); }
		| '\\,' { sb.append(","); }
		| '\\\\' { sb.append("\\"); } 
	)+ 
	{ $text = sb.toString(); } ;

easydocEnd returns [int line] : 
	ED_END { $line = $ED_END.getLine(); } ;

easydocDoc returns [Doc result]
	: { Doc ret = new Doc(); }
	easydocStart { 
		ret.setParams($easydocStart.params);
	} 
	(
		easydocText { ret.appendText($easydocText.result); } 
	)?
	easydocEnd {
		ret.setSourceLink(new SourceLink(file, $easydocStart.line, $easydocEnd.line)); 
		$result=ret; 
	} 
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
	
easydocText returns [String result]
	: { StringBuffer sb = new StringBuffer(); }
	(
		CHAR { sb.append($CHAR.text); } 
		| WS { sb.append($WS.text); } 
		| EQ { sb.append($EQ.text); } 
		| COMMA { sb.append($COMMA.text); }
		| '\\@\\@' { sb.append("@@"); }
		| '\\\\' { sb.append("\\"); }
	)+ { $result = sb.toString(); } ;

document returns [List<Doc> docs]
	: 
	{ $docs = new ArrayList<Doc>(); }
	(
		simpleText 
		| easydocDoc { $docs.add($easydocDoc.result); } 
	)*;