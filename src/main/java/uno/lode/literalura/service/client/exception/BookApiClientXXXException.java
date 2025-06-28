package uno.lode.literalura.service.client.exception;

import org.springframework.http.HttpStatusCode;

public class BookApiClientXXXException extends BookApiClientException {
	private static final long serialVersionUID = 1L;

	public BookApiClientXXXException(HttpStatusCode httpStatusCode) {
		super(httpStatusCode.toString());
	}
}