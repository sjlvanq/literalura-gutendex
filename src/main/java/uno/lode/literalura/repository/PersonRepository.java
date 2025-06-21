package uno.lode.literalura.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import uno.lode.literalura.model.Person;
import uno.lode.literalura.model.PersonType;

public interface PersonRepository extends JpaRepository<Person, Long> {

	Optional<Person> findByNameAndBirthYearAndDeathYearAndType(String name, int BirthYear, int DeathYear, PersonType type);
	
	List<Person> findByType(PersonType type);
	
	@Query("SELECT p FROM Person p WHERE p.birthYear <= :year AND p.deathYear >= :year AND p.type = 'AUTHOR'")
	List<Person> findAuthorsAliveInYear(int year);

}
