package uno.lode.literalura.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uno.lode.literalura.model.*;
import uno.lode.literalura.repository.PersonRepository;

@Service
public class PersonService {

	@Autowired
	private PersonRepository personRepository;

	public PersonService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	public List<Person> getPersonByType(PersonType type) {
		return personRepository.findByType(type);
	}

	public List<Person> getAuthorsAliveInYear(int year) {
		return personRepository.findAuthorsAliveInYear(year);
	}
	
	public Person findOrCreatePerson(PersonData data, PersonType type) {
		return personRepository
				.findByNameAndBirthYearAndDeathYearAndType(data.name(), data.birthYear(), data.deathYear(), type)
				.orElseGet(() -> personRepository.save(new Person(data, type)));
	}
}
