package uno.lode.literalura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uno.lode.literalura.model.*;
import uno.lode.literalura.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private PersonService personService;
    
    @Autowired
    private LanguageService languageService;
    
    public List<Book> getAllBooks() {
    	return bookRepository.findAll();
    }

    @Transactional
    public Book saveBookFromData(BookData bookData) {
        if (bookRepository.existsByBookId(bookData.id())) {
            return bookRepository.findByBookId(bookData.id());
        }

        Book book = new Book(bookData);

        List<Person> authors = new ArrayList<>();
        for (PersonData personData : bookData.authors()) {
            Person author = personService.findOrCreatePerson(personData, 
            		PersonType.AUTHOR);
            authors.add(author);
        }

        List<Person> translators = new ArrayList<>();
        for (PersonData personData : bookData.translators()) {
            Person translator = personService.findOrCreatePerson(personData, 
            		PersonType.TRANSLATOR);
            translators.add(translator);
        }

        List<Language> languages = new ArrayList<>();
        for (String code : bookData.languages()) {
            Language language = languageService.findOrCreateLanguage(code);
            languages.add(language);
        }
        
        authors.forEach(book::addAuthor);
        translators.forEach(book::addTranslator);
        languages.forEach(book::addLanguage);

        return bookRepository.save(book);
    }

	public List<Book> getBooksByExternalIds(List<Long> fetchedBookIds) {
		return bookRepository.findByBookIdIn(fetchedBookIds);
	}    
}
