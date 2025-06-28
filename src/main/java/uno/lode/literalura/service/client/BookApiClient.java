package uno.lode.literalura.service.client;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient; //spring-boot-starter-web

import com.fasterxml.jackson.databind.ObjectMapper;

import uno.lode.literalura.model.Error400Data;
import uno.lode.literalura.model.SearchResultsData;
import uno.lode.literalura.service.client.exception.*;

// https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-restclient
// https://certidevs.com/tutorial-spring-boot-rest-client-y-rest-template

@Service
public class BookApiClient {
	private final RestClient restClient;
	private final ObjectMapper objectMapper;

	public BookApiClient(RestClient restClient, ObjectMapper objectMapper) {
	    this.restClient = restClient;
	    this.objectMapper = objectMapper;
	}

	public SearchResultsData findByTitleOrAuthor(String term) throws BookApiClientException, ResourceAccessException {
		return restClient.get()
				.uri("/books/?search={term}", term)
				.accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
					MediaType contentType = response.getHeaders().getContentType();
					if (contentType == null || !MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
					    throw new BookApiClientInvalidResponseException();
					} else if (response.getStatusCode().is2xxSuccessful()) {
						return parseBody(response.getBody(), SearchResultsData.class);
					} else if(response.getStatusCode().is4xxClientError()) {
						Error400Data error = parseBody(response.getBody(), Error400Data.class);
						throw new BookApiClient4XXException(response.getStatusCode(), error.detail());
					} else  if(response.getStatusCode().is5xxServerError()) {
						throw new BookApiClient5XXException(response.getStatusCode(), response.getStatusText());
					} else {
						throw new BookApiClientXXXException(response.getStatusCode());
					}
				});
	}

	private <T> T parseBody(InputStream body, Class<T> type) {
	    try {
	        return objectMapper.readValue(body, type);
	    } catch (IOException e) {
	        throw new BookApiClientInvalidResponseException();
	    }
	}
}
