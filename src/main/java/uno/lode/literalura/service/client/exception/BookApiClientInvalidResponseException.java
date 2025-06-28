package uno.lode.literalura.service.client.exception;

public class BookApiClientInvalidResponseException extends BookApiClientException {
	private static final long serialVersionUID = 1L;

	public BookApiClientInvalidResponseException() {
		super("Invalid server response");
	}
}
