package edu.utdallas.main;

public class Config {
	public static final char AND_SYMBOL = '&';
	public static final char OR_SYMBOL = '|';
	public static final char NOT_SYMBOL = '!';
	public static final char LEFT_PARENTHESIS_SYMBOL = '(';
	public static final char RIGHT_PARENTHESIS_SYMBOL = ')';
	public static final String OS_NAME = System.getProperty("os.name", "");
	public static final boolean CASE_SENSITIVE = !OS_NAME.toLowerCase().contains("windows");
}
