package pl.seb.czech.library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
    
    @NotNull
    @Column(name = "publication_year")
    private Integer publicationYear;
    
    @OneToMany(
            targetEntity = Book.class,
            mappedBy = "titleInfo",
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY
    )
    private List<Book> bookList = new ArrayList<>();
    
    public TitleInfo(String title, String author, Integer publicationYear) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
    }
    
    
}
