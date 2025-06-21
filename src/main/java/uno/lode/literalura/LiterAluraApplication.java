package uno.lode.literalura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import uno.lode.literalura.main.Main;
import uno.lode.literalura.repository.BookRepository;
import uno.lode.literalura.repository.LanguageRepository;
import uno.lode.literalura.repository.PersonRepository;
import uno.lode.literalura.service.BookService;
import uno.lode.literalura.service.client.BookApiClient;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {
	@Autowired
	BookApiClient apiClient;
	@Autowired
	BookRepository bookRepository;
	@Autowired
	LanguageRepository languageRepository;
	@Autowired
	PersonRepository personRepository;
	@Autowired
	BookService bookService;
	
	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Main main = new Main(apiClient, bookRepository, personRepository, bookService, languageRepository);
		main.run();
	}

}
