package uno.lode.literalura.service.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient; //spring-boot-starter-web

import uno.lode.literalura.model.SearchResultsData;

// https://certidevs.com/tutorial-spring-boot-rest-client-y-rest-template

@Service
public class BookApiClient {
	private final RestClient restClient;
	public BookApiClient(RestClient restClient) {
		this.restClient = restClient;
	}
	public SearchResultsData findByTitleOrAuthor(String term) {
		
		return restClient.get()
				.uri("/books/?search={term}", term)
				.retrieve()
				.body(SearchResultsData.class);
				//.body(String.class);
	}
}
