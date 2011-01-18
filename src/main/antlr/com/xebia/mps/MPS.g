

grammar MPS;

options {
	output=none;
}

@parser::header {
package com.xebia.mps;
}

@parser::members {
	private MPSMetaData metadata;
	
	public void setMetaData(MPSMetaData metadata) {
		this.metadata = metadata;
	}
	
}

@rulecatch {
	catch (RecognitionException e) {
	  throw e;
	}
}


@lexer::header {
package com.xebia.mps;
}

parse			: firstrow rows columns rhs ranges? bounds? endata EOF {metadata.end();}; 
firstrow		: NAMEINDICATOR name=IDENTIFIER {metadata.setName($name.text);};
rows			: ROWINDICATOR (rowdatacard)+;
columns			: COLUMNINDICATOR columndatacards;
rhs			: RHSINDICATOR rhsdatacards;	
ranges			: RANGESINDICATOR rangesdatacards;
bounds			: BOUNDSINDICATOR boundsdatacards;
endata			: ENDATAINDICATOR;		

rowdatacard		: type=ROWTYPE name=IDENTIFIER {metadata.registerRow($type.text.charAt(0), $name.text);};		
columndatacards		: columndatacard+;
rhsdatacards		: rhsdatacard+;			
rangesdatacards		: rangesdatacard+;		
boundsdatacards		: boundsdatacard+;		

columndatacard		: col=IDENTIFIER row1=IDENTIFIER val1=NUMERICALVALUE {metadata.registerValue($col.text, $row1.text, Double.parseDouble($val1.text));} (row2=IDENTIFIER val2=NUMERICALVALUE {metadata.registerValue($col.text, $row2.text, Double.parseDouble($val2.text));})?;
rhsdatacard		: col=IDENTIFIER row1=IDENTIFIER val1=NUMERICALVALUE {metadata.registerRightHandSide($col.text, $row1.text, Double.parseDouble($val1.text));} (row2=IDENTIFIER val2=NUMERICALVALUE {metadata.registerRightHandSide($col.text, $row2.text, Double.parseDouble($val2.text));})?;
rangesdatacard		: IDENTIFIER IDENTIFIER NUMERICALVALUE (IDENTIFIER NUMERICALVALUE)?;
boundsdatacard		: key=BOUNDKEY IDENTIFIER var=IDENTIFIER (val=NUMERICALVALUE)? {metadata.registerBound(MPSBoundType.valueOf($key.text), $var.text, $val == null? 0.0 :Double.parseDouble($val.text));};

NAMEINDICATOR		: 'NAME';
ROWINDICATOR 		: 'ROWS';
COLUMNINDICATOR		: 'COLUMNS';
RHSINDICATOR		: 'RHS';
RANGESINDICATOR		: 'RANGES';
BOUNDSINDICATOR		: 'BOUNDS';
ENDATAINDICATOR		: 'ENDATA';

BOUNDKEY		: ('UP' | 'LO' | 'FX' | 'FR' | 'MI' | 'PL');
ROWTYPE			: ('E' | 'L' | 'G' | 'N');
IDENTIFIER 		: LETTER TOKEN*;
NUMERICALVALUE		: '-'? DIGIT DIGIT* ('.' DIGIT*)?;

WS	 	: (' ' | '\t' | '\n' | '\r' | '\f')+  			{$channel=HIDDEN;};
LINE_COMMENT	: ('*' | '$') ~('\n'|'\r')* '\r'? '\n' 			{$channel=HIDDEN;};
fragment TOKEN	: (LETTER | DIGIT);
fragment LETTER	: ('a'..'z' | 'A'..'Z' | '_' | '/' | '#' | '@' | '(' | ')' | '.');
fragment DIGIT	: '0'..'9';




