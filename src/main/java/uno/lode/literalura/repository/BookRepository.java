package uno.lode.literalura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uno.lode.literalura.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	
	boolean existsByBookId(Long bookId);
	
	Book findByBookId(Long bookId);
	
	List<Book> findByBookIdIn(List<Long> booksIds);
	
}
