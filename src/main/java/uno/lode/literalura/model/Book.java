package uno.lode.literalura.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true)
    private Long bookId;
    private String title;
    private Boolean copyright;
    private String mediaType;
    private Integer downloadCount;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "book_authors",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private List<Person> authors;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "book_translators",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private List<Person> translators;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "book_languages",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language> languages;
    
    //[] summaries;
    //[] subjects;
    //[] bookshelves;
    //[] formats;

    public Book(){
        this.authors = new ArrayList<>();
        this.translators = new ArrayList<>();
        this.languages = new ArrayList<>();
    }

    public Book(BookData bookData){
    	this();
        this.bookId = bookData.id();
        this.title = bookData.title();
        this.copyright = bookData.copyright();
        this.mediaType = bookData.mediaType();
        this.downloadCount = bookData.downloadCount();
    }

	@Override
	public String toString() {
		return "Book [id=" + id + ", bookId=" + bookId + ", title=" + title + ", copyright=" + copyright
				+ ", mediaType=" + mediaType + ", downloadCount=" + downloadCount + ", languages=" + languages + ", authors=" + authors + ", translators=" + translators + "]";
	}
	
	public List<Person> getAuthors() {
		return this.authors;
	}

	public Long getBookId() {
		return bookId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public List<Person> getTranslators() {
		return this.translators;
	}
	
    public void addAuthor(Person author) {
        this.authors.add(author);
    }

    public void addTranslator(Person translator) {
        this.translators.add(translator);
    }

    public void addLanguage(Language lang) {
        this.languages.add(lang);
    }
    
}