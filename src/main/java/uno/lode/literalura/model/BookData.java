package uno.lode.literalura.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookData(
	Long id,
	String title,
	Boolean copyright,
	@JsonAlias("media_type") String mediaType,
	@JsonAlias("download_count") Integer downloadCount,
	List<PersonData> authors,
	List<PersonData> translators,
	List<String> languages
) {}
