package uno.lode.literalura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uno.lode.literalura.model.*;
import uno.lode.literalura.repository.BookRepository;
import uno.lode.literalura.repository.LanguageRepository;
import uno.lode.literalura.repository.PersonRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LanguageRepository languageRepository;
    
    @Autowired
    private PersonRepository personRepository;

    @Transactional
    public Book saveBookFromData(BookData bookData) {
        if (bookRepository.existsByBookId(bookData.id())) {
            return bookRepository.findByBookId(bookData.id());
        }

        Book book = new Book(bookData);

        List<Person> authors = new ArrayList<>();
        for (PersonData personData : bookData.authors()) {
            Person author = findOrCreatePerson(personData, PersonType.AUTHOR);
            authors.add(author);
        }

        List<Person> translators = new ArrayList<>();
        for (PersonData personData : bookData.translators()) {
            Person translator = findOrCreatePerson(personData, PersonType.TRANSLATOR);
            translators.add(translator);
        }

        List<Language> languages = new ArrayList<>();
        for (String code : bookData.languages()) {
            Language language = findOrCreateLanguage(code);
            languages.add(language);
        }
        
        authors.forEach(book::addAuthor);
        translators.forEach(book::addTranslator);
        languages.forEach(book::addLanguage);

        return bookRepository.save(book);
    }

    private Person findOrCreatePerson(PersonData data, PersonType type) {
        return personRepository
                .findByNameAndBirthYearAndDeathYearAndType(
                        data.name(), data.birthYear(), data.deathYear(), type)
                .orElseGet(() -> personRepository.save(
                        new Person(data, type)
                ));
    }
    
    private Language findOrCreateLanguage(String langCode) {
        return languageRepository
                .findByCode(langCode)
                .orElseGet(() -> languageRepository.save(
                        new Language(langCode)
                ));
    }
    
}
