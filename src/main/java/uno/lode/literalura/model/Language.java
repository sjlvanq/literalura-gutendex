package uno.lode.literalura.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "languages")
public class Language {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
	private String code;
	
	public Language() {}
	
	public Language(String langCode) {
		this.code = langCode;
	}

	@Override
	public String toString() {
		return code;
	}
	
}
