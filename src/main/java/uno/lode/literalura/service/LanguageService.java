package uno.lode.literalura.service;

import java.util.List;

import org.springframework.stereotype.Service;
import uno.lode.literalura.model.*;
import uno.lode.literalura.repository.LanguageRepository;

@Service
public class LanguageService {
	private LanguageRepository languageRepository;

	public LanguageService(LanguageRepository languageRepository) {
		this.languageRepository = languageRepository;
	}

	public List<Integer> getLanguageCounts() {
		return languageRepository.findLanguageCounts();
	}
	
	public String getMostUsedLanguageCode() {
		return languageRepository.findMostUsedLanguageCode();
	}

	//https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html
	public List<LanguageCountProjection> getLanguageCodesAndCounts() {
		return languageRepository.findLanguageCodesAndCounts();
	}
	
	public Language findOrCreateLanguage(String langCode) {
		return languageRepository.findByCode(langCode)
				.orElseGet(() -> languageRepository.save(new Language(langCode)));
	}
}
