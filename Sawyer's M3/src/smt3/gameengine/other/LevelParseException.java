package smt3.gameengine.other;

public class LevelParseException extends Exception {

	private static final long serialVersionUID = 1L;

	public LevelParseException(String message) {
		super(message);
	}

	public LevelParseException(Throwable cause) {
		super(cause);
	}

	public LevelParseException(String message, Throwable cause) {
		super(message, cause);
	}

}
