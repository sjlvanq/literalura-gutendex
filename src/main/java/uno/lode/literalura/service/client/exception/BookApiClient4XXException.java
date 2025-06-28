package uno.lode.literalura.service.client.exception;

import org.springframework.http.HttpStatusCode;

public class BookApiClient4XXException extends BookApiClientException  {
	private static final long serialVersionUID = 1L;
	
	public BookApiClient4XXException(HttpStatusCode statusCode, String detail) {
		super("Client error: " + statusCode + " - " + detail);
	}
}
