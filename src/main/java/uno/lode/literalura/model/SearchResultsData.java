package uno.lode.literalura.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SearchResultsData(
		int count, 
		List<BookData> results,
		boolean isNew 
) {}
