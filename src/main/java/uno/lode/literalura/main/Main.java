package uno.lode.literalura.main;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

import uno.lode.literalura.model.Book;
import uno.lode.literalura.model.BookData;
import uno.lode.literalura.model.LanguageCountProjection;
import uno.lode.literalura.model.Person;
import uno.lode.literalura.model.PersonType;
import uno.lode.literalura.model.SearchResultsData;
import uno.lode.literalura.repository.BookRepository;
import uno.lode.literalura.repository.LanguageRepository;
import uno.lode.literalura.repository.PersonRepository;
import uno.lode.literalura.service.BookService;
import uno.lode.literalura.service.client.BookApiClient;

public class Main {
	private final BookApiClient apiClient;
	private final BookRepository bookRepository;
	private final LanguageRepository languageRepository;
	private final PersonRepository personRepository;
	private final BookService bookService;

	private final Scanner scanner;
	
	public Main(BookApiClient apiClient, BookRepository bookRepository, PersonRepository personRepository, BookService bookService, LanguageRepository languageRepository) {
		this.apiClient = apiClient;
		this.bookRepository = bookRepository;
		this.languageRepository = languageRepository;
		this.personRepository = personRepository;
		this.bookService = bookService;
		this.scanner = new Scanner(System.in);
	}
	
	private enum SearchField {
		TITLE, AUTHOR
	}

	public void run() {
		int selectedOption = -1;
		do {
			System.out.println("""
					
					| ===== LiterAlura =====
					|
					| 1: Search books by title
					| 2: Search books by author
					|    -----
					| 3: Show saved books
					| 4: Show saved authors
					| 5: Show saved translators
					| 6: Show authors alive in a given year
					| 7: Show saved books count by language
					| 0: Exit
					|____
					""");
			try {
				selectedOption = Integer.valueOf(scanner.nextLine().trim());
			} catch (NumberFormatException e) {selectedOption = -1;}
			
			switch (selectedOption) {
				case 1:
					searchByTitleOrAuthor(SearchField.TITLE);
					break;
				case 2:
					searchByTitleOrAuthor(SearchField.AUTHOR);
					break;
				case 3:
					showSavedBooks();
					break;
				case 4:
					showSavedPersons(PersonType.AUTHOR);
					break;
				case 5:
					showSavedPersons(PersonType.TRANSLATOR);
					break;
				case 6:
					showAuthorsAliveInYear();
					break;
				case 7:
					showLanguageCounts();
					break;
			}
		} while (selectedOption != 0);
	}

	public void searchByTitleOrAuthor(SearchField searchField) {
		
		System.out.print("Search: ");
		String query = scanner.nextLine().trim();
		SearchResultsData searchResults = apiClient.findByTitleOrAuthor(query);
		
		List<BookData> filteredSearchResults = null;
		if(searchField == SearchField.TITLE) {
			filteredSearchResults = searchResults.results().stream()
					.filter(b->b.title().toUpperCase().contains(query.toUpperCase()))
					.toList(); //.collect(Collectors.toList()); // Compatibilidad Java < 16
		} else if(searchField == SearchField.AUTHOR) {
			filteredSearchResults = searchResults.results().stream()
					.filter( b -> {
						return b.authors().stream()
								.anyMatch(a->a.name().toUpperCase().contains(query.toUpperCase()));
					}).toList(); //.collect(Collectors.toList());
		}
		
		//List<Long> fetchedBookIds = searchResults.results().stream()
		List<Long> fetchedBookIds = filteredSearchResults.stream()
				.map( bookData -> bookData.id())
				.toList(); //.collect(Collectors.toList());
		
		List<Long> alreadySavedBookIds = bookRepository.findByBookIdIn(fetchedBookIds).stream()
				.map(b -> b.getBookId())
				.toList();
		
		//searchResults.results().stream()
		filteredSearchResults.stream()
			.forEach( bookData -> {
				System.out.println(
					"[" + bookData.id() + "]\t" + 
					(alreadySavedBookIds.contains(bookData.id()) ? "" : "(Unsaved) ") + 
					bookData.title());
				
				//bookData.authors().forEach( a -> System.out.println("\t    "+a.name()));
				System.out.println(
						bookData.authors().stream()
							.map(a->a.name())
							.collect(Collectors.joining("; ", "\tAuthors: ", "")));
				
			});
		
		if(fetchedBookIds.size() == 0) {
			System.out.println("Nothing found!");
			return;
		}
		
		if(fetchedBookIds.size() == alreadySavedBookIds.size()) {
			System.out.println("All books in results already saved!");
			return;
		}
		
		System.out.println("ID1,...IDn = Save");
		System.out.println("A = Save all");
		String menuUserInput = scanner.nextLine().trim();
		
		if(menuUserInput.equalsIgnoreCase("A")) {
			//searchResults.results().stream()
			filteredSearchResults.stream()
				.filter( bookData -> !alreadySavedBookIds.contains(bookData.id()))
				.forEach( bookData -> bookService.saveBookFromData(bookData));
				//.forEach( bookData -> bookRepository.save(new Book(bookData)));
		} else {
			List<String> bookIdsSelected = Arrays.asList(menuUserInput.split(","));
			List<Long> validSelectedIds = bookIdsSelected.stream()
					.map(String::trim)
					.map(id -> {
						try {
							Long parsedId = Long.parseLong(id);
							return fetchedBookIds.contains(parsedId) ? parsedId : null;
						} catch (NumberFormatException e) {
							return null;
						}})
					.filter(Objects::nonNull)
					.toList();
			
			if(bookIdsSelected.size()!=validSelectedIds.size()) {
				System.out.println("Warning: Some selected IDs are invalid!");
			}
			
			if(validSelectedIds.isEmpty()) {
				System.out.println("Nothing to do");
				return;
			}
			
			List<BookData> booksToBeSaved = searchResults.results().stream()
				.filter(bookData -> validSelectedIds.contains(bookData.id()))
				.filter(bookData -> !alreadySavedBookIds.contains(bookData.id()))
				.toList();
				
			
			System.out.println("Books to be saved: ");
			booksToBeSaved.forEach(book -> System.out.println("[" + book.id() + "] " + book.title()));
			System.out.print("Continue? (y/n) > ");
			if(scanner.nextLine().trim().equalsIgnoreCase("y")) {
				//booksToBeSaved.stream().forEach(bookData -> bookRepository.save(new Book(bookData)));
				booksToBeSaved.stream().forEach(bookData -> bookService.saveBookFromData(bookData));
				System.out.println("Done");
			}
		}
	}
	
	private void showSavedBooks() {
		List<Book> savedBooks = bookRepository.findAll();
		showResultsList(savedBooks);
	}
	
	private void showSavedPersons(PersonType type) {
		List<Person> savedPerson = personRepository.findByType(type);
		showResultsList(savedPerson);
	}
	
	private void showAuthorsAliveInYear() {
		System.out.print("Year: ");
		int year = Integer.valueOf(scanner.nextLine().trim());
		List<Person> aliveAuthors = personRepository.findAuthorsAliveInYear(year);
		showResultsList(aliveAuthors);
	}
	
	private <T> void showResultsList(List<T> list) {
		if(list.isEmpty() ) {
			System.out.println("Nothing found!");
			return;
		}
		list.forEach(System.out::println);
	}
	
	private void showLanguageCounts() {
		List<Integer> languageCounts = languageRepository.findLanguageCounts();
		DoubleSummaryStatistics stat = languageCounts.stream()
				.collect(Collectors.summarizingDouble(e->e));
		System.out.println("Distinct Languages: " + stat.getCount());
		System.out.println("Avg. books per language: " + stat.getAverage());
		System.out.println("Max books per language: " + (int) stat.getMax() + 
				" (" + languageRepository.findMostUsedLanguageCode() + ")");
	}
	
	@SuppressWarnings("unused")
	private void showLanguageCounts2() {
		List<LanguageCountProjection> languageCounts = languageRepository.findLanguageCodesAndCounts();
		if(languageCounts.isEmpty()) {return;}
		//System.out.println(languageCounts);
		DoubleSummaryStatistics stat = languageCounts.stream()
				.collect(Collectors.summarizingDouble(e->e.getCount()));
		System.out.println("Distinct Languages: " + stat.getCount());
		System.out.println("Avg. books per language: " + stat.getAverage());
		System.out.println("Max books per language: " + (int) stat.getMax() + 
				" (" + languageCounts.stream().findFirst().get().getCode() + ")");
				//languageCounts.stream().filter( l -> l.getCount() == stat.getMax())
				//	.limit(1).findFirst().get().getCode()+")");
	}
}
