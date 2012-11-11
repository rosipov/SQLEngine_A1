package sqlengine_a1.parser;

public class ParseError extends Exception {
	public ParseError(String s) {
		super(s);
	}
}

class MaybeParseError extends ParseError {
	public MaybeParseError(String s) {
		super(s);
	}
}

class DefiniteParseError extends ParseError {
	public DefiniteParseError(String s) {
		super(s);
	}
}
