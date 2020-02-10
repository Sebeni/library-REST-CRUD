package pl.seb.czech.library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Entity
@Table(name = "titles_info")
public class TitleInfo {
    
    @NotNull
    @Id
    @GeneratedValue
    private Long id;
    
    @NotNull
    private String title;
    
    @NotNull
    private String author;
    
    private BigDecimal price;
    
    @NotNull
    @Column(name = "publication_year")
    private Integer publicationYear;
    
    @OneToMany(
            targetEntity = Book.class,
            mappedBy = "titleInfo",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private Set<Book> bookList = new HashSet<>();
    
    public TitleInfo(String title, String author, Integer publicationYear, BigDecimal price) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.price = price.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public String toString() {
        return "TitleInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", publicationYear=" + publicationYear +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TitleInfo titleInfo = (TitleInfo) o;
        return title.equals(titleInfo.title) &&
                author.equals(titleInfo.author) &&
                publicationYear.equals(titleInfo.publicationYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, publicationYear);
    }
}
