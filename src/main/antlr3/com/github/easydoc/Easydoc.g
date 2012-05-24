grammar Easydoc;

options {
  language = Java;
}

@header {
  package com.github.easydoc;
  
  import java.util.Map;
  import java.util.HashMap;
  import java.util.List;
  import java.util.ArrayList;
  import java.io.File;
  
  import com.github.easydoc.model.Doc;
  import com.github.easydoc.model.SourceLink;
  import com.github.easydoc.model.Directive;
  import com.github.easydoc.model.DocItem;
  import com.github.easydoc.model.DocTextItem;
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
  
  public EasydocParser(File file, String encoding) throws java.io.IOException {
  	super(
  		new CommonTokenStream(
  			new EasydocLexer(
  				new ANTLRFileStream(
  					file.getAbsolutePath(),
  					encoding
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

DOUBLEAT: '@@';

ED_START: '@@easydoc-start';

ED_END: '@@easydoc-end@@';

CHAR: '\u0000'..'\uFFFE';

easydocStart returns [Map<String, String> params, int line, int startColumn, int endColumn] : 
	{ $params = new HashMap<String, String>(); }
	ED_START { 
		$line =  $ED_START.getLine();
		$startColumn =  $ED_START.getCharPositionInLine();
	}
	easydocParams { $params = $easydocParams.params; } 
	DOUBLEAT { $endColumn = $DOUBLEAT.getCharPositionInLine() + $DOUBLEAT.getText().length(); } 
	;
	
easydocParams returns [Map<String, String> params] :
	{ $params = new HashMap<String, String>(); }
	(WS* COMMA WS* easydocParam { $params.put($easydocParam.name, $easydocParam.value); } )*
	; 

easydocParam returns [String name, String value] : 
	paramName { $name = $paramName.text; } 
	(WS* EQ WS* paramValue { $value = $paramValue.text; } );

paramName returns [String text] : 
	{ StringBuilder sb = new StringBuilder(); }
	(CHAR { sb.append($CHAR.text); } )+ 
	{ $text = sb.toString(); } ;

paramValue returns [String text] : 
	{ StringBuilder sb = new StringBuilder(); } 
	(
		CHAR { sb.append($CHAR.text); }
		| '\\,' { sb.append(","); }
		| '\\\\' { sb.append("\\"); } 
	)+ 
	{ $text = sb.toString(); } ;

easydocEnd returns [int line] : 
	ED_END { $line = $ED_END.getLine(); } ;

easydocDoc returns [Doc result]
	: { $result = new Doc(); }
	easydocStart { 
		$result.setParams($easydocStart.params);
	} 
	(
		easydocText { 
			$result.setItems($easydocText.items);			
			$result.setDirectives($easydocText.directives);
		} 
	)?
	easydocEnd {
		$result.setSourceLink(new SourceLink(file, $easydocStart.line, $easydocEnd.line)); 
	} 
	;

easydocDirective returns [Directive result] : 
	{ $result = new Directive(); }
	DOUBLEAT
	paramName { $result.setName($paramName.text); }
	easydocParams { $result.setParams($easydocParams.params); }
	DOUBLEAT;	
	
simpleText returns [String result] : 
	{ StringBuffer sb = new StringBuffer(); }
	(
		CHAR { sb.append($CHAR.text); } 
		| WS { sb.append($WS.text); } 
		| EQ { sb.append($EQ.text); } 
		| COMMA { sb.append($COMMA.text); }
		| DOUBLEAT { sb.append("@@"); }
	)+ { $result = sb.toString(); } ;
	
easydocText returns [List<DocItem> items, List<Directive> directives] : 
	{ 
		StringBuffer sb = new StringBuffer();
		$directives = new ArrayList<Directive>();
		$items = new ArrayList<DocItem>(); 
	}
	(
		CHAR { sb.append($CHAR.text); } 
		| WS { sb.append($WS.text); } 
		| EQ { sb.append($EQ.text); } 
		| COMMA { sb.append($COMMA.text); }
		| '\\@\\@' { sb.append("@@"); }
		| '\\\\' { sb.append("\\"); }
		| easydocDirective {
		 	$items.add(new DocTextItem(sb.toString()));
		 	sb.setLength(0);
		 	$items.add($easydocDirective.result);
			$directives.add($easydocDirective.result); 
		}
	)+ { $items.add(new DocTextItem(sb.toString())); } ;

document returns [List<Doc> docs]
	: 
	{ $docs = new ArrayList<Doc>(); }
	(
		simpleText 
		| easydocDoc { $docs.add($easydocDoc.result); } 
	)*;