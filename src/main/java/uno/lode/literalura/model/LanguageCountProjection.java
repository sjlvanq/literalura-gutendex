package uno.lode.literalura.model;

//https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html

public interface LanguageCountProjection {
    String getCode();
    Integer getCount();
}
