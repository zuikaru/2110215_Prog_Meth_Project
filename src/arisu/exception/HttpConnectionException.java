package arisu.exception;

import java.net.HttpURLConnection;

public class HttpConnectionException extends Exception {

	private static final long serialVersionUID = 1L;
	private HttpURLConnection connection;

	public HttpConnectionException(String message, HttpURLConnection connection, Throwable cause) {
		super(message, cause);
		this.connection = connection;
	}

	public HttpURLConnection getConnection() {
		return connection;
	}
}
