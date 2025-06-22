package uno.lode.literalura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import uno.lode.literalura.main.Main;
import uno.lode.literalura.repository.*;
import uno.lode.literalura.service.*;
import uno.lode.literalura.service.client.BookApiClient;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {
	@Autowired
	BookService bookService;
	@Autowired
	PersonService personService;
	@Autowired
	LanguageService languageService;
	@Autowired
	BookRepository bookRepository;
	@Autowired
	PersonRepository personRepository;
	@Autowired
	LanguageRepository languageRepository;
	@Autowired
	BookApiClient apiClient;
	
	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Main main = new Main(
				apiClient,
				bookService,
				personService,
				languageService);
		main.run();
	}

}
