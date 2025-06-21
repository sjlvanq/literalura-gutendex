package uno.lode.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record PersonData (
	String name,
	@JsonAlias("birth_year") int birthYear,
	@JsonAlias("death_year") int deathYear
	)
{}
