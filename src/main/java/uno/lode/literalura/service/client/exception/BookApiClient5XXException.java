package uno.lode.literalura.service.client.exception;

import org.springframework.http.HttpStatusCode;

public class BookApiClient5XXException extends BookApiClientException {
	private static final long serialVersionUID = 1L;
	
	public BookApiClient5XXException(HttpStatusCode statusCode, String detail) {
		super("Server error: " + statusCode + " - " + detail);
	}

}
