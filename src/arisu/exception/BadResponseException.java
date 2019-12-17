package arisu.exception;

public class BadResponseException extends Exception {
	private static final long serialVersionUID = 1L;

	public BadResponseException(String message) {
		super(message);
	}

	public BadResponseException(String message, Throwable cause) {
		super(message, cause);
	}

}
