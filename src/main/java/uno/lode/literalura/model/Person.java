package uno.lode.literalura.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	name = "persons", 
	uniqueConstraints = {
			@UniqueConstraint(columnNames = { "name", "birthYear", "deathYear", "type" })
	})
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String name;
	private int birthYear;
	private int deathYear;

	@Enumerated(EnumType.STRING)
	private PersonType type; // AUTHOR o TRANSLATOR
	
	public Person() {}
	
	public Person(PersonData personData, PersonType type) {
		this.name = personData.name();
		this.birthYear = personData.birthYear();
		this.deathYear = personData.deathYear();
		this.type = type;
	}

	@Override
	public String toString() {
		return type + " [name=" + name + ", birthYear=" + birthYear + ", deathYear=" + deathYear + "]";
	}
	
}
