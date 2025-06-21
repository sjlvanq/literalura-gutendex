package uno.lode.literalura.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import uno.lode.literalura.model.Language;
import uno.lode.literalura.model.LanguageCountProjection;

public interface LanguageRepository  extends JpaRepository<Language, Long> {

	Optional<Language> findByCode(String code);
	
	@Query(value="SELECT COUNT(*) as count FROM books "
			+ "JOIN book_languages ON books.id = book_languages.book_id "
			+ "JOIN languages ON languages.id = book_languages.language_id "
			+ "GROUP BY languages.code ORDER BY count DESC", nativeQuery = true)
	List<Integer> findLanguageCounts();
	
	@Query(value="SELECT l.code lc FROM book_languages bl "
			+ "JOIN languages l ON bl.language_id = l.id "
			+ "GROUP BY l.code ORDER BY COUNT(bl.book_id) DESC LIMIT 1", nativeQuery = true)
	String findMostUsedLanguageCode();
	
	@Query(value="SELECT languages.code, COUNT(*) as count FROM books "
			+ "JOIN book_languages ON books.id = book_languages.book_id "
			+ "JOIN languages ON languages.id = book_languages.language_id "
			+ "GROUP BY languages.code ORDER BY count DESC", nativeQuery = true)
	List<LanguageCountProjection> findLanguageCodesAndCounts();
	
	//List<Object[]> findLanguageCounts();
}
