package edu.utdallas.searchengine;

import edu.utdallas.main.Config;

enum Token {	
	LEFT_PAR(Character.toString(Config.LEFT_PARENTHESIS_SYMBOL)),
	RIGHT_PAR(Character.toString(Config.RIGHT_PARENTHESIS_SYMBOL)),
	AND_SYMBOL(Character.toString(Config.AND_SYMBOL)),
	OR_SYMBOL(Character.toString(Config.OR_SYMBOL)),
	NOT_SYMBOL(Character.toString(Config.NOT_SYMBOL)),
	WHITE_SPACE(" "),
	LITERAL("");
	
	private String tokeValue;
	
	private Token(String tokenValue) {
		this.tokeValue = tokenValue;
	}

	public String getTokeValue() {
		return tokeValue;
	}

	public void setTokeValue(String tokeValue) {
		this.tokeValue = tokeValue;
	}
}
