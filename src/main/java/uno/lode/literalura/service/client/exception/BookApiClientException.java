package uno.lode.literalura.service.client.exception;

public abstract class BookApiClientException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BookApiClientException(String message) {
		super(message);

	}
}
